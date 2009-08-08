package org.codehaus.groovy.gjit.soot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import soot.Body;
import soot.CompilationDeathException;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import soot.baf.Baf;
import soot.jimple.JimpleBody;
import soot.options.Options;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.util.JasminOutputStream;

public class SingleClassOptimizer {

	private int format = Options.output_format_class;
	private boolean via_shimple   = false;

	public byte[] optimize(Class<?> c) {
		SootClass sc = Scene.v().loadClassAndSupport(c.getName());
		runBodyPacks(sc);
		return writeClass(sc);
	}

	private void runBodyPacks(SootClass c) {
		boolean produceJimple = true;
		boolean produceBaf = false;
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

		if (this.via_shimple)
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
    	ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
        String fileName = SourceLocator.v().getFileNameFor(c, format);

        streamOut = new JasminOutputStream(streamOut);
        PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));

        if (c.containsBafBody())
            new soot.baf.JasminClass(c).print(writerOut);
        else
            new soot.jimple.JasminClass(c).print(writerOut);

        try {
            writerOut.flush();
            streamOut.close();
            return streamOut.toByteArray();
        } catch (IOException e) {
            throw new CompilationDeathException("Cannot close output file " + fileName);
        }
    }

}
