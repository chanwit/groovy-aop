/*
 * Copyright 2003-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.groovy.runtime;

import groovy.lang.Closure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;

import org.codehaus.groovy.aop.Weaver;
import org.codehaus.groovy.geelab.jama.util.MagicSquare;
import org.codehaus.groovy.geelab.linearalgebra.Complex;
import org.codehaus.groovy.geelab.linearalgebra.ComplexMatrix;
import org.codehaus.groovy.geelab.linearalgebra.Matrix;
import org.codehaus.groovy.geelab.linearalgebra.MatrixFactory;
import org.codehaus.groovy.geelab.osp.numerics.FFT;
import org.codehaus.groovy.geelab.osp.numerics.FFT2D;
import org.codehaus.groovy.geelab.osp.numerics.FFTReal;
import org.codehaus.groovy.reflection.ReflectionUtils;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

/**
 * This class defines all the new static groovy methods which appear on normal
 * JDK classes inside the Groovy environment. Static methods are used with the
 * first parameter as the destination class.
 *
 * @author Guillaume Laforge
 * @author Dierk Koenig
 * @author Joachim Baumann
 * @author Paul King
 * @version $Revision: 15217 $
 */
public class DefaultGroovyStaticMethods {

    /**
     * Start a Thread with the given closure as a Runnable instance.
     *
     * @param self    placeholder variable used by Groovy categories; ignored for default static methods
     * @param closure the Runnable closure
     * @return the started thread
     * @since 1.0
     */
    public static Thread start(Thread self, Closure closure) {
        return createThread(null, false, closure);
    }

    /**
     * Start a Thread with a given name and the given closure
     * as a Runnable instance.
     *
     * @param self    placeholder variable used by Groovy categories; ignored for default static methods
     * @param name    the name to give the thread
     * @param closure the Runnable closure
     * @return the started thread
     * @since 1.6
     */
    public static Thread start(Thread self, String name, Closure closure) {
        return createThread(name, false, closure);
    }

    /**
     * Start a daemon Thread with the given closure as a Runnable instance.
     *
     * @param self    placeholder variable used by Groovy categories; ignored for default static methods
     * @param closure the Runnable closure
     * @return the started thread
     * @since 1.0
     */
    public static Thread startDaemon(Thread self, Closure closure) {
        return createThread(null, true, closure);
    }

    /**
     * Start a daemon Thread with a given name and the given closure as
     * a Runnable instance.
     *
     * @param self    placeholder variable used by Groovy categories; ignored for default static methods
     * @param name    the name to give the thread
     * @param closure the Runnable closure
     * @return the started thread
     * @since 1.6
     */
    public static Thread startDaemon(Thread self, String name, Closure closure) {
        return createThread(name, true, closure);
    }

    private static Thread createThread(String name, boolean daemon, Closure closure) {
        Thread thread = name != null ? new Thread(closure, name) : new Thread(closure);
        if (daemon) thread.setDaemon(true);
        thread.start();
        return thread;
    }

    /**
     * Get the last hidden matcher that the system used to do a match.
     *
     * @param self placeholder variable used by Groovy categories; ignored for default static methods
     * @return the last regex matcher
     * @since 1.0
     */
    public static Matcher getLastMatcher(Matcher self) {
        return RegexSupport.getLastMatcher();
    }

    /**
     * This method is used by both sleep() methods to implement sleeping
     * for the given time even if interrupted
     *
     * @param millis  the number of milliseconds to sleep
     * @param closure optional closure called when interrupted
     *                as long as the closure returns false the sleep continues
     */
    private static void sleepImpl(long millis, Closure closure) {
        long start = System.currentTimeMillis();
        long rest = millis;
        long current;
        while (rest > 0) {
            try {
                Thread.sleep(rest);
                rest = 0;
            } catch (InterruptedException e) {
                if (closure != null) {
                    if (DefaultTypeTransformation.castToBoolean(closure.call(e))) {
                        return;
                    }
                }
                current = System.currentTimeMillis(); // compensate for closure's time
                rest = millis + start - current;
            }
        }
    }

    /**
     * Sleep for so many milliseconds, even if interrupted.
     *
     * @param self         placeholder variable used by Groovy categories; ignored for default static methods
     * @param milliseconds the number of milliseconds to sleep
     * @since 1.0
     */
    public static void sleep(Object self, long milliseconds) {
        sleepImpl(milliseconds, null);
    }

    /**
     * Sleep for so many milliseconds, using a given closure for interrupt processing.
     *
     * @param self         placeholder variable used by Groovy categories; ignored for default static methods
     * @param milliseconds the number of milliseconds to sleep
     * @param onInterrupt  interrupt handler, InterruptedException is passed to the Closure
     *                     as long as it returns false, the sleep continues
     * @since 1.0
     */
    public static void sleep(Object self, long milliseconds, Closure onInterrupt) {
        sleepImpl(milliseconds, onInterrupt);
    }

    /**
     * Parse a String into a Date instance using the given pattern.
     * This convenience method acts as a wrapper for {@link SimpleDateFormat}.
     *
     * <p>Note that a new SimpleDateFormat instance is created for every
     * invocation of this method (for thread safety).</p>
     *
     * @see SimpleDateFormat#parse(String)
     * @param self         placeholder variable used by Groovy categories; ignored for default static methods
     * @param format       pattern used to parse the input string.
     * @param input        String to be parsed to create the date instance
     * @return             a new Date instance representing the parsed input string
     * @throws ParseException if there is a parse error
     * @since 1.5.7
     */
    public static Date parse( Date self, String format, String input ) throws ParseException {
        return new SimpleDateFormat( format ).parse( input );
    }

    /**
     * Works exactly like ResourceBundle.getBundle(String).  This is needed
     * because the java method depends on a particular stack configuration that
     * is not guaranteed in Groovy when calling the Java method.
     *
     * @see ResourceBundle#getBundle(String)
     * @param self         placeholder variable used by Groovy categories; ignored for default static methods
     * @param bundleName   the name of the bundle.
     * @return the resource bundle
     * @since 1.6.0
     */
    public static ResourceBundle getBundle(ResourceBundle self, String bundleName) {
        return getBundle(self,bundleName,Locale.getDefault());
    }

    /**
     * Works exactly like ResourceBundle.getBundle(String, Locale).  This is needed
     * because the java method depends on a particular stack configuration that
     * is not guaranteed in Groovy when calling the Java method.
     *
     * @see ResourceBundle#getBundle(String, Locale)
     * @param self         placeholder variable used by Groovy categories; ignored for default static methods
     * @param bundleName   the name of the bundle.
     * @param locale       the speficic locale
     * @return the resource bundle
     * @since 1.6.0
     */
    public static ResourceBundle getBundle(ResourceBundle self, String bundleName, Locale locale) {
        ClassLoader targetCL = ReflectionUtils.getCallingClass().getClassLoader();
        if (targetCL == null) targetCL = ClassLoader.getSystemClassLoader();
        return ResourceBundle.getBundle(bundleName, locale, targetCL);
    }

    /**
     * Weave an apsect into the global aspect registry.
     * @param aspectClass
     * @since 1.6.4-AOP
     */
    public static Object weave(Object self, Class<?> aspectClass) throws Throwable {
        return Weaver.install(aspectClass);
    }

    /**
     * Unweave an apsect out of the global aspect registry.
     * @param aspectClass
     * @since 1.6.4-AOP
     */
    public static void unweave(Object self, Class<?> aspectClass) throws Throwable {
        Weaver.uninstall(aspectClass);
    }

    /**
     * Matlab-like commands
     *
     * @param self
     * @return
     */
    public static Object getTic(Object self) {
        TicTocStack.tic();
        return null;
    }

    /**
     * Matlab-like commands
     *
     * @param self
     * @return
     */
    public static Object getToc(Object self) {
        return TicTocStack.toc();
    }

    /**
     * Matlab-like commands
     *
     * @param self
     * @return
     */
    public static void disp(Object self, Object str) {
        System.out.println(str.toString());
    }

    public static Object zeros(Object self, Integer n) {
        ComplexMatrix c = new ComplexMatrix(n, n, new double[n*n*2]);
        c.fill(0, 0);
        return c;
    }

    public static Object zeros(Object self, Integer n, Integer m) {
        ComplexMatrix c = new ComplexMatrix(n, m, new double[n*m*2]);
        c.fill(0, 0);
        return c;
    }

    public static Object ones(Object self, Integer n) {
        ComplexMatrix c = new ComplexMatrix(n, n, new double[n*n*2]);
        c.fill(1, 0);
        return c;
    }

    public static Object ones(Object self, Integer n, Integer m) {
        return ComplexMatrixHelper.ones(n, m);
    }

    public static Object magic(Object self, Integer n) {
        double[] data = MagicSquare.magic(n).getRowPackedCopy();
        double[] complex = new double[data.length*2];
        for(int i=0,j=0;i<complex.length;i+=2,j++) {
            complex[i] = data[j];
        }
        return new ComplexMatrix(n, n, complex);
    }

    public static Object exp(Object self, double o) {
        return Math.exp(o);
    }

    public static Object exp(Object self, Complex c) {
        return c.exp();
    }

    public static Object exp(Object self, ComplexMatrix c) {
        return c.exp();
    }

    public static Object fft(Object self, ComplexMatrix mat) {
        return FFTHelper.fft(mat);
    }

    public static Object ifft(Object self, ComplexMatrix mat) {
        return FFTHelper.ifft(mat);
    }

    public static Object fft2(Object self, ComplexMatrix mat) {
        return FFTHelper.fft2(mat);
    }

    public static Object ifft2(Object self, ComplexMatrix mat) {
        return FFTHelper.ifft2(mat);
    }

}
