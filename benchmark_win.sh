# Groovy GT
java -javaagent:./groovy-aop-0.6.1.jar -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.heapsort.HeapSortTest  > gt_c_w_he.log
java -javaagent:./groovy-aop-0.6.1.jar -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.fibbonacci.FibTest     > gt_c_w_fi.log
java -javaagent:./groovy-aop-0.6.1.jar -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.sieve.SieveTest        > gt_c_w_si.log
java -javaagent:./groovy-aop-0.6.1.jar -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.fannkuch.FannkuchTest  > gt_c_w_fa.log

# Groovy
java -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.heapsort.HeapSortTest  > g_c_w_he.log
java -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.fibbonacci.FibTest     > g_c_w_fi.log
java -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.sieve.SieveTest        > g_c_w_si.log
java -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.fannkuch.FannkuchTest  > g_c_w_fa.log

# Java
java -cp "benchmarks-0.1.jar" thesis.java.HeapSortTest > j_c_w_he.log
java -cp "benchmarks-0.1.jar" thesis.java.Fib          > j_c_w_fi.log
java -cp "benchmarks-0.1.jar" thesis.java.Sieve        > j_c_w_si.log
java -cp "benchmarks-0.1.jar" thesis.java.Fannkuch     > j_c_w_fa.log

# Groovy GT
java -server -javaagent:./groovy-aop-0.6.1.jar -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.heapsort.HeapSortTest  > gt_s_w_he.log
java -server -javaagent:./groovy-aop-0.6.1.jar -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.fibbonacci.FibTest     > gt_s_w_fi.log
java -server -javaagent:./groovy-aop-0.6.1.jar -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.sieve.SieveTest        > gt_s_w_si.log
java -server -javaagent:./groovy-aop-0.6.1.jar -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.fannkuch.FannkuchTest  > gt_s_w_fa.log

# Groovy
java -server -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.heapsort.HeapSortTest  > g_s_w_he.log
java -server -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.fibbonacci.FibTest     > g_s_w_fi.log
java -server -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.sieve.SieveTest        > g_s_w_si.log
java -server -cp "groovy-aop-0.6.1.jar;*" org.codehaus.groovy.gjit.asm.fannkuch.FannkuchTest  > g_s_w_fa.log

# Java
java -server -cp "benchmarks-0.1.jar" thesis.java.HeapSortTest > j_s_w_he.log
java -server -cp "benchmarks-0.1.jar" thesis.java.Fib          > j_s_w_fi.log
java -server -cp "benchmarks-0.1.jar" thesis.java.Sieve        > j_s_w_si.log
java -server -cp "benchmarks-0.1.jar" thesis.java.Fannkuch     > j_s_w_fa.log