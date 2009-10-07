export JAVA_HOME="c:\jdk"
cd dist

# Groovy GT
$JAVA_HOME/bin/java -javaagent:./groovy-aop-0.6.1.jar -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.heapsort.HeapSortTest  > gt_c_w_he.log
$JAVA_HOME/bin/java -javaagent:./groovy-aop-0.6.1.jar -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.fibbonacci.FibTest     > gt_c_w_fi.log
$JAVA_HOME/bin/java -javaagent:./groovy-aop-0.6.1.jar -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.sieve.SieveTest        > gt_c_w_si.log
$JAVA_HOME/bin/java -javaagent:./groovy-aop-0.6.1.jar -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.fannkuch.FannkuchTest  > gt_c_w_fa.log

# Groovy
$JAVA_HOME/bin/java -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.heapsort.HeapSortTest  > g_c_w_he.log
$JAVA_HOME/bin/java -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.fibbonacci.FibTest     > g_c_w_fi.log
$JAVA_HOME/bin/java -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.sieve.SieveTest        > g_c_w_si.log
$JAVA_HOME/bin/java -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.fannkuch.FannkuchTest  > g_c_w_fa.log

# Java
$JAVA_HOME/bin/java -cp "benchmarks-0.1.jar" thesis.java.HeapSortTest > j_c_w_he.log
$JAVA_HOME/bin/java -cp "benchmarks-0.1.jar" thesis.java.Fib          > j_c_w_fi.log
$JAVA_HOME/bin/java -cp "benchmarks-0.1.jar" thesis.java.Sieve        > j_c_w_si.log
$JAVA_HOME/bin/java -cp "benchmarks-0.1.jar" thesis.java.Fannkuch     > j_c_w_fa.log

# Groovy GT
$JAVA_HOME/bin/java -server -javaagent:./groovy-aop-0.6.1.jar -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.heapsort.HeapSortTest  > gt_s_w_he.log
$JAVA_HOME/bin/java -server -javaagent:./groovy-aop-0.6.1.jar -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.fibbonacci.FibTest     > gt_s_w_fi.log
$JAVA_HOME/bin/java -server -javaagent:./groovy-aop-0.6.1.jar -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.sieve.SieveTest        > gt_s_w_si.log
$JAVA_HOME/bin/java -server -javaagent:./groovy-aop-0.6.1.jar -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.fannkuch.FannkuchTest  > gt_s_w_fa.log

# Groovy
$JAVA_HOME/bin/java -server -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.heapsort.HeapSortTest  > g_s_w_he.log
$JAVA_HOME/bin/java -server -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.fibbonacci.FibTest     > g_s_w_fi.log
$JAVA_HOME/bin/java -server -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.sieve.SieveTest        > g_s_w_si.log
$JAVA_HOME/bin/java -server -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.fannkuch.FannkuchTest  > g_s_w_fa.log

# Java
$JAVA_HOME/bin/java -server -cp "benchmarks-0.1.jar" thesis.java.HeapSortTest > j_s_w_he.log
$JAVA_HOME/bin/java -server -cp "benchmarks-0.1.jar" thesis.java.Fib          > j_s_w_fi.log
$JAVA_HOME/bin/java -server -cp "benchmarks-0.1.jar" thesis.java.Sieve        > j_s_w_si.log
$JAVA_HOME/bin/java -server -cp "benchmarks-0.1.jar" thesis.java.Fannkuch     > j_s_w_fa.log