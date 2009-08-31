package org.codehaus.groovy.gjit.soot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.codehaus.groovy.gjit.soot.transformer.Utils;
import org.codehaus.groovy.runtime.callsite.CallSite;

import soot.Body;
import soot.Local;
import soot.Modifier;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;
import soot.jimple.internal.JIdentityStmt;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;

public class TypePropagation {

    static final class Result {

        final private SootMethod method;
        final private byte[] body;

        public Result(SootMethod newMethod, byte[] bytes) {
            this.method = newMethod;
            this.body   = bytes;
        }

        public SootMethod getMethod() {
            return method;
        }

        public byte[] getBody() {
            return body;
        }
    }

    Class<?>   advisedReturnType;
    Class<?>[] advisedTypes;
    SootClass  targetClass;
    SootMethod targetMethod;

    public Result typePropagate(String callSiteClassName) {
        //
        // the name of call site class is in this format:
        //   Class$method
        // so that it.split('$') will be resulting into:
        //   [Class, method]
        //
        String[] targetNames = callSiteClassName.split("\\$");
        SootClass targetSc = Scene.v().loadClass(targetNames[0], SootClass.BODIES);

        //
        // TODO: This should be
        // SootMethod  targetSm = targetSc.getMethod("sub signature");
        //
        SootMethod targetSm = targetSc.getMethodByName(targetNames[1]);

        //
        // Retrieve body from the original target method.
        // We need a jimple body.
        //
        Body body = targetSm.retrieveActiveBody();
        ShimpleBody sBody;
        if (body instanceof ShimpleBody) {
            sBody = (ShimpleBody)body;
            if (!sBody.isSSA()) sBody.rebuild();
        } else {
            sBody = Shimple.v().newBody(body);
        }
        JimpleBody jBody = sBody.toJimpleBody();

        //
        // Create a new class using the same name with "$x".
        // CallSite in groovy is created using the pattern:
        //   Class$method.
        // We append $x to it.
        // So a new class will be named:
        //   Class$method$x.
        //
        String newClassName = callSiteClassName + "$x";
        SootClass newSc = new SootClass(newClassName, Modifier.PUBLIC);

        //
        // Need a magic super type for bypassing security check
        //
        newSc.setSuperclass(RefType.v("sun.reflect.GroovyAOPMagic").getSootClass());

        ArrayList<Type> typeList = new ArrayList<Type>();

        //
        // targetSc is the target class obtained from targetNames[0]
        // it is added to be the first argument to simulate "this".
        //
        typeList.add(targetSc.getType());

        //
        // Then advised types are added to the list
        // for preparing the signature for newMethod
        //
        for (int i = 0; i < advisedTypes.length; i++) {
            Class<?> advisedParamType = advisedTypes[i];
            if(advisedParamType == null)
                typeList.add( targetSm.getParameterType(i) );
            else
                typeList.add( Utils.v().classToSootType(advisedParamType) );
        }

        //
        // Advise return type, if available
        //
        Type returnType = targetSm.getReturnType();
        if(advisedReturnType != null) {
            returnType = Utils.v().classToSootType(advisedReturnType);
        }

        //
        // Doing type propagation through the method's body
        //
        PatchingChain<Unit> units = jBody.getUnits();
        Iterator<Unit> stmts = units.iterator();
        while(stmts.hasNext()) {
            Unit s = stmts.next();
            if(s instanceof IdentityStmt) continue;

            JIdentityStmt a = (JIdentityStmt)s;
            Value right = a.getRightOp();
            //
            // If found "this", change it to parameter:0
            // for this simulation.
            //
            if(right instanceof ThisRef) {
                a.setRightOp(
                    Jimple.v().newParameterRef(right.getType(), 0)
                );
            }
            //
            // Shifting every variable index to be + 1
            // Because we add the simulated this to parameter:0
            //
            else if(right instanceof ParameterRef) {
                ParameterRef p = (ParameterRef)right;
                a.setRightOp(
                    Jimple.v().newParameterRef(
                        typeList.get(p.getIndex() + 1),
                        p.getIndex() + 1
                    )
                );
                //
                // $r_i.type = typeof(rhs)
                //
                ((Local)a.getLeftOp()).setType(a.getRightOp().getType());
            }
        }

        SootMethod newMethod = new SootMethod(targetSm.getName(), typeList, returnType);
        newMethod.setModifiers(Modifier.STATIC + Modifier.PUBLIC);
        newMethod.setDeclaringClass(newSc);
        newSc.addMethod(newMethod);
        newMethod.setActiveBody(jBody);
        byte[] bytes = Utils.v().writeClass(newSc);
        // Utils.v().defineClass(newClassName, bytes);

        //
        // TODO: for D E B U G G I N G
        //
//        FileOutputStream fos;
//        try {
//            new File("Dump_03.class").delete();
//            fos = new FileOutputStream("Dump_03.class");
//            fos.write(bytes);
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return new Result(newMethod, bytes);
    }
}
