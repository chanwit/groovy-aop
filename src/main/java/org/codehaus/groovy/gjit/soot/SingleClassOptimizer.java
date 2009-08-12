package org.codehaus.groovy.gjit.soot;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import soot.Body;
import soot.BodyTransformer;
import soot.CompilationDeathException;
import soot.Pack;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import soot.Transform;
import soot.baf.Baf;
import soot.jimple.JimpleBody;
import soot.options.Options;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.util.JasminOutputStream;

/**
 *
 * This class is preparing a Jimple output. Setting via_shimple to get an SSA
 * form.
 *
 * @author chanwit
 *
 */
public class SingleClassOptimizer {

	private int format = Options.output_format_class;

	private boolean produceJimple = true;
	private boolean produceBaf = true;
	private boolean produceShimple = false;
	private List<SootMethod> methodList = null;
	//
	// set this optimisation to go via shimple to get SSA form
	//
	private boolean viaShimple = false;
	private List<BodyTransformer> transformers = null;

	public enum Phase {
		SHIMPLE,
		JIMPLE,
		ALL
	}

	public boolean isProduceJimple() {
		return produceJimple;
	}

	public void setProduceJimple(boolean produceJimple) {
		this.produceJimple = produceJimple;
	}

	public boolean isProduceBaf() {
		return produceBaf;
	}

	public void setProduceBaf(boolean produceBaf) {
		this.produceBaf = produceBaf;
	}

	public boolean isProduceShimple() {
		return produceShimple;
	}

	public void setProduceShimple(boolean produceShimple) {
		this.produceShimple = produceShimple;
	}

	public List getTransformers() {
		return transformers;
	}

	public void setTransformers(BodyTransformer[] transformers) throws Throwable {
		this.transformers = new ArrayList<BodyTransformer>();
		for (int i = 0; i < transformers.length; i++) {
			this.transformers.add(transformers[i]);
		}
	}

	public void setTransformers(List<?> transformers) throws Throwable {
		this.transformers = new ArrayList<BodyTransformer>();
		for (Iterator<?> iterator = transformers.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			if (object instanceof Class<?>) {
				Class<?> c = (Class<?>) object;
				this.transformers.add((BodyTransformer) c.newInstance());
			} else if (object instanceof BodyTransformer) {
				this.transformers.add((BodyTransformer) object);
			}
		}
	}

	public boolean isViaShimple() {
		return viaShimple;
	}

	public void setViaShimple(boolean viaShimple) {
		this.viaShimple = viaShimple;
	}

	/**
	 * The main entry of optimisation.
	 *
	 * @param c
	 *            a class. Actually this method uses only its name to perform
	 *            optimization.
	 * @return a byte array containing optimized class
	 */
	public byte[] optimize(Class<?> c) {
		return optimize(c, Phase.ALL);
	}

	public byte[] optimize(Class<?> c, Phase toPhase) {
		SootClass sc = Scene.v().loadClassAndSupport(c.getName());
		try {
			//
			// CK:
			// re-sorting all methods to get <cinit> and <init> become first.
			//
			methodList = sc.getMethods();
			Collections.sort(methodList, new Comparator<SootMethod>() {
				public int compare(SootMethod m1, SootMethod m2) {
					return m1.getName().compareTo(m2.getName());
				}
			});

			runBodyPacks(sc, toPhase);
			return writeClass(sc);
		} finally {
			Scene.v().removeClass(sc);
		}
	}

	private void runBodyPacks(SootClass c, Phase stopPhase) {
		switch (format) {
			case Options.output_format_none:
			case Options.output_format_xml:
			case Options.output_format_jimple:
			case Options.output_format_jimp:
				break;
			case Options.output_format_shimp:
			case Options.output_format_shimple:
				produceShimple = true;
				// FLIP produceJimple
				produceJimple = false;
				break;
			case Options.output_format_baf:
			case Options.output_format_b:
				produceBaf = true;
				break;
			case Options.output_format_jasmin:
			case Options.output_format_class:
				produceBaf = true;
				break;
			default:
				throw new RuntimeException();
		}

		if (this.viaShimple)
			produceShimple = true;

		shimplePhase();
		if(stopPhase == Phase.SHIMPLE) return;
		jimplePhase();
		if(stopPhase == Phase.JIMPLE) return;
		bafPhase();
	}

	private void bafPhase() {
		Iterator<SootMethod> methodIt = methodList.iterator();
		if (!produceBaf) return;
		while (methodIt.hasNext()) {
			SootMethod m = (SootMethod) methodIt.next();
			m.setActiveBody(Baf.v().newBody(
				(JimpleBody) m.getActiveBody())
			);
			PackManager.v().getPack("bop").apply(m.getActiveBody());
			PackManager.v().getPack("tag").apply(m.getActiveBody());
		}
	}

	private void jimplePhase() {
		Iterator<SootMethod> methodIt = methodList.iterator();
		if (!produceJimple) return;
		while (methodIt.hasNext()) {
			SootMethod m = (SootMethod) methodIt.next();
			JimpleBody body = (JimpleBody) m.retrieveActiveBody();
			PackManager.v().getPack("jtp").apply(body);
			if (transformers != null) {
				for (Iterator<BodyTransformer> iterator = transformers.iterator(); iterator.hasNext();) {
					BodyTransformer t = iterator.next();
					t.transform(body);
				}
			}
			PackManager.v().getPack("jop").apply(body);
			PackManager.v().getPack("jap").apply(body);
		}
	}

	private void shimplePhase() {
		Iterator<SootMethod> methodIt = methodList.iterator();
		while (methodIt.hasNext()) {
			SootMethod m = (SootMethod) methodIt.next();

			if (!m.isConcrete())
				continue;

			if (produceShimple) {
				ShimpleBody sBody = null;
				// whole shimple or not?
				{
					Body body = m.retrieveActiveBody();

					if (body instanceof ShimpleBody) {
						sBody = (ShimpleBody) body;
						if (!sBody.isSSA())
							sBody.rebuild();
					} else {
						sBody = Shimple.v().newBody(body);
					}
				}

				m.setActiveBody(sBody);
				PackManager.v().getPack("stp").apply(sBody);
				PackManager.v().getPack("sop").apply(sBody);

				if (produceJimple)
					m.setActiveBody(sBody.toJimpleBody());
			}
		}
	}

	private byte[] writeClass(SootClass c) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		String fileName = SourceLocator.v().getFileNameFor(c, format);
		JasminOutputStream streamOut = new JasminOutputStream(bout);
		PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));

		if (c.containsBafBody())
			new soot.baf.JasminClass(c).print(writerOut);
		else
			new soot.jimple.JasminClass(c).print(writerOut);

		try {
			writerOut.flush();
			streamOut.close();
			byte[] bytes = bout.toByteArray();
			dumpToFile(bytes, "SCODump" + System.currentTimeMillis() + ".class");
			return bytes;
		} catch (IOException e) {
			throw new CompilationDeathException("Cannot close output file " + fileName);
		}
	}


	private void dumpToFile(byte[] bytes, String string) {
		FileOutputStream f;
		try {
			f = new FileOutputStream(string);
			f.write(bytes);
			f.flush();
			f.close();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}


	private static void initClasses() {
		String classes[] = {
			"groovy.lang.Closure",
			"org.codehaus.groovy.grails.web.metaclass.RenderDynamicMethod",
			"sun.reflect.GroovyAOPMagic"
		};

		for (int i = 0; i < classes.length; i++) {
			Scene.v().addBasicClass(classes[i], SootClass.SIGNATURES);
		}
	}

	static {
		Scene.v().setPhantomRefs(true);
		initClasses();
		Scene.v().loadBasicClasses();
	}

}
