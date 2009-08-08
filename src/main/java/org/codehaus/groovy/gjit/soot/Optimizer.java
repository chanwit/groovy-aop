package org.codehaus.groovy.gjit.soot;

import org.codehaus.groovy.gjit.soot.transformer.CallSiteRecorder;
import org.codehaus.groovy.gjit.soot.transformer.ConstantRecorder;
import org.codehaus.groovy.gjit.soot.transformer.PrimitiveBinOps;

import soot.Pack;
import soot.PackManager;
import soot.Transform;

public class Optimizer {

    public static void main(String[] args) {
        Pack jtp = PackManager.v().getPack("jtp");
        jtp.add(new Transform("jtp.groovy.constantRecorder", ConstantRecorder.v()));
        jtp.add(new Transform("jtp.groovy.callsiteRecorder", CallSiteRecorder.v()));

//		jtp.add(new Transform("jtp.groovy.primitiveLeafs", PrimitiveLeafs.v()));
        jtp.add(new Transform("jtp.groovy.pbinop", new PrimitiveBinOps()));
//		jtp.add(new Transform("jtp.groovy.unwrap.compare", new UnwrapCompare()));
//		jtp.add(new Transform("jtp.groovy.boxcastunbox.eliminator", new BoxCastUnboxEliminator()));
        soot.Main.main(args);
    }

}
