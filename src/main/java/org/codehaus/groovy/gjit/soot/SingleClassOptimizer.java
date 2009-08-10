package org.codehaus.groovy.gjit.soot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.gjit.soot.transformer.CallsiteNameCollector;

import soot.Body;
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
 * This class is preparing a Jimple output.
 * Setting via_shimple to get an SSA form.
 *
 * @author chanwit
 *
 */
public class SingleClassOptimizer {

	private int format = Options.output_format_class;

	//
	// set this optimisation to go via shimple to get SSA form
	//
	private boolean viaShimple = false;

	public boolean isViaShimple() {
		return viaShimple;
	}

	public void setViaShimple(boolean viaShimple) {
		this.viaShimple = viaShimple;
	}

	/**
	 * The main entry of optimisation.
	 *
	 * @param c a class. Actually this method uses only its name to
	 * perform optimization.
	 * @return a byte array containing optimized class
	 */
	public byte[] optimize(Class<?> c) {
		SootClass sc = Scene.v().loadClassAndSupport(c.getName());
		runBodyPacks(sc);
		return writeClass(sc);
	}

	private void runBodyPacks(SootClass c) {
		boolean produceJimple = true;
		boolean produceBaf = true;
		boolean produceShimple = false;

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

		//
		// CK:
		// re-sorting all methods to get <cinit> and <init> become first.
		//
		List<SootMethod> methodList = c.getMethods();
		Collections.sort(methodList, new Comparator<SootMethod>() {
			public int compare(SootMethod m1, SootMethod m2) {
				return m1.getName().compareTo(m2.getName());
			}
		});

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

			if (produceJimple) {
				JimpleBody body = (JimpleBody) m.retrieveActiveBody();
				PackManager.v().getPack("jtp").apply(body);
				//if (options.validate()) {
				//	body.validate();
				//}
				PackManager.v().getPack("jop").apply(body);
				PackManager.v().getPack("jap").apply(body);
			}

			if (produceBaf) {
				m.setActiveBody(Baf.v().newBody(
					(JimpleBody) m.getActiveBody()
				));
				PackManager.v().getPack("bop").apply(m.getActiveBody());
				PackManager.v().getPack("tag").apply(m.getActiveBody());
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
            return bout.toByteArray();
        } catch (IOException e) {
            throw new CompilationDeathException("Cannot close output file " + fileName);
        }
    }

    private static void initClasses() {
        String classes[] = {
            "groovy.lang.Closure",
            "org.codehaus.groovy.grails.web.metaclass.RenderDynamicMethod"
        };

        for (int i = 0; i < classes.length; i++) {
            Scene.v().addBasicClass(classes[i], SootClass.SIGNATURES);
        }
    }

    static {
        Scene.v().setPhantomRefs(true);
        Pack jtp = PackManager.v().getPack("jtp");

        //
        // TODO add required transformers here
        //
         jtp.add(new Transform("jtp.callsite_name_collector",
                new CallsiteNameCollector()
         ));
        // jtp.add(new Transform("jtp.render_declaration",
        //        new RenderIntroduction()
        // ));
        // jtp.add(new Transform("jtp.closure_detector",
        //        new ClosureDetector()
        //));
        // jtp.add(new Transform("jtp.prototype",
        //        new Prototype_2()
        //));
        initClasses();
        Scene.v().loadBasicClasses();
    }

}
