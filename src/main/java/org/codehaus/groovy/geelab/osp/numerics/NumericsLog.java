package org.codehaus.groovy.geelab.osp.numerics;
import java.lang.reflect.Method;

/**
 * Logs numerics messages to the OSPLog using reflection.
 * Messages are not logged if the OSPLog class is not available.
 *
 * @author W. Christian
 * @version 1.0
 */
public class NumericsLog {
   static String logName = "org.opensourcephysics.controls.OSPLog";
   static Class logClass;
   private NumericsLog() {} // private to prohibit instantiation

   static {
      try {
         logClass = Class.forName(logName);
      } catch(ClassNotFoundException ex) {
         logClass = null;
      }
   }

   /**
    * Logs a fine debugging message in the OSPLog.
    *
    * @param msg the message
    */
   public static void fine(String msg) {
      if(logClass==null) {
         return;
      }
      try {
        Method m = logClass.getMethod("fine", new Class[]{String.class});
        // target is null because the fine method in the OSPLog class is static
        m.invoke(null, new Object[] {msg});
      } catch(Exception ex) {}
   }
}
