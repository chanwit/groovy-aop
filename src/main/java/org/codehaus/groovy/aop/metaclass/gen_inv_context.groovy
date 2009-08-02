#!/usr/bin/env groovy
//
// each normal call will gen [],0,1,2,3,4
//
def normal = ['','Constructor','Current','Safe','Static']

def callIndex = 0;
//
// cv is call convention
//
normal.each { cv ->
  for(n in -1..4) {
    def args = []
    def params = []
    for(i in 0..n)  {
        // special case, for Object[]
        if(i == -1) {
            args << 'Object[] arg1'
            params << 'args'
        } else {
            if(cv == 'Current' && i == 0) {
                args << "GroovyObject arg${i}"
                params << "(GroovyObject)target"
                // params << "(GroovyObject)args[${i}]"
            } else if (cv == 'Static' && i == 0) {
                args << "Class arg${i}"
                params << "(Class)target"
                // params << "(Class)args[${i}]"
            } else {
                args << "Object arg${i}"
                if(i==0) 
                    params << "target"
                else
                    params << "args[${i-1}]"
            }
        }
    }

    //
    // special case
    // when called from N arguments
    //
    // def context_SetArgs = "context.setArgs(new Object[]{${params.join(", ")}});"
    // if(n == -1) {
    //     context_SetArgs = "context.setArgs(arg1);"
    // }
    
    callIndex++
    def text=""
    if(n == -1) {
        text = "case ${callIndex}: return proceedCallSite.call${cv}(${params[0]}, (Object[])arguments);"
    } else {
        text = "case ${callIndex}: return proceedCallSite.call${cv}(${params.join(", ")});"
    }
    
// def text = """
//     public Object call${cv}(${args.join(", ")}) throws Throwable {
//         InvocationContext context = new InvocationContext();
//         ${context_SetArgs}
//         if(before != null) {
//             // System.out.println("doing before ...");
//             for(int i = 0; i < before.length; i++) {
//                 try {
//                     before[i].call(context);
//                 } catch(InvokerInvocationException e) {
//                     if (e.getCause() instanceof MissingMethodException) {
//                         if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
//                             throw new ProceedNotAllowedException();
//                         }
//                     }
//                     throw e;
//                 }
//             }
//         }
//         
//         Object result = null;
//         if(around != null) {
//             if(this.callIndex != ${callIndex}) {
//                 throw new RuntimeException("Code generation broken");
//             }
//             context.callIndex = ${callIndex};
//             context.proceedCallSite = delegate;
//             around[0].call(context);
//         } else {
//             result = delegate.call${cv}(${params.join(", ")});   
//         }
//                  
//         if(after != null) {
//             // System.out.println("doing after ...");
//             for(int i = 0; i < after.length; i++) {
//                 try {
//                     after[i].call(context);
//                 } catch(InvokerInvocationException e) {
//                     if (e.getCause() instanceof MissingMethodException) {
//                         if (((MissingMethodException)e.getCause()).getMethod().equals("proceed")) {
//                             throw new ProceedNotAllowedException();
//                         }
//                     }
//                     throw e;
//                 }
//             }
//         }
//         return result;
//     }
// """
     println text
   }
}

//
// another set to gen *property with arg0
//
// def special_0 = ['callGetProperty',
//                  'callGetPropertySafe',
//                  'callGroovyObjectGetProperty',
//                  'callGroovyObjectGetPropertySafe']
// special_0.each { m ->
// def text = """
//     public Object ${m}(Object arg0) throws Throwable {
//         //
//         //  TODO replace this with a real invocation context object
//         //
//         Object context = null;
//         for(int i = 0; i < before.length; i++) {
//             before[i].call(context);
//         }
//         Object result = delegate.${m}(arg0);
//         for(int i = 0; i < after.length; i++) {
//             after[i].call(context);
//         }
//         return result;
//     }
// """
//     print text
// }

