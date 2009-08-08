package soot;

import java.util.*;
import java.io.*;
import java.util.zip.*;
import soot.util.*;
import soot.util.queue.*;
import soot.jimple.*;
import soot.shimple.*;
import soot.grimp.*;
import soot.baf.*;
import soot.jimple.toolkits.invoke.*;
import soot.jimple.toolkits.base.*;
import soot.shimple.toolkits.scalar.*;
import soot.grimp.toolkits.base.*;
import soot.baf.toolkits.base.*;
import soot.jimple.toolkits.typing.*;
import soot.jimple.toolkits.scalar.*;
import soot.jimple.toolkits.scalar.pre.*;
import soot.jimple.toolkits.annotation.arraycheck.*;
import soot.jimple.toolkits.annotation.profiling.*;
import soot.jimple.toolkits.annotation.callgraph.*;
import soot.jimple.toolkits.annotation.parity.*;
import soot.jimple.toolkits.annotation.methods.*;
import soot.jimple.toolkits.annotation.fields.*;
import soot.jimple.toolkits.annotation.qualifiers.*;
import soot.jimple.toolkits.annotation.nullcheck.*;
import soot.jimple.toolkits.annotation.tags.*;
import soot.jimple.toolkits.annotation.defs.*;
import soot.jimple.toolkits.annotation.liveness.*;
import soot.jimple.toolkits.annotation.logic.*;
import soot.jimple.toolkits.annotation.purity.*; // [AM]

import soot.jimple.toolkits.annotation.*;
import soot.jimple.toolkits.pointer.*;
import soot.jimple.toolkits.callgraph.*;
import soot.jimple.toolkits.thread.synchronization.LockAllocator;
import soot.tagkit.*;
import soot.options.Options;
import soot.toolkits.scalar.*;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.paddle.PaddleHook;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.spark.fieldrw.*;
import soot.dava.*;
import soot.dava.toolkits.base.AST.interProcedural.InterProceduralAnalyses;
import soot.dava.toolkits.base.AST.transformations.RemoveEmptyBodyDefaultConstructor;
import soot.dava.toolkits.base.AST.transformations.VoidReturnRemover;
import soot.dava.toolkits.base.misc.*;
import soot.xml.*;
import soot.toolkits.graph.interaction.*;

/** Manages the Packs containing the various phases and their options. */
public class PackManager {

	public static boolean DEBUG=false;

    public PackManager( Singletons.Global g ) {
    	PhaseOptions.v().setPackManager(this);
    	init();
    }

    private boolean onlyStandardPacks = false;
    public boolean onlyStandardPacks() {
    	return onlyStandardPacks;
    }

    private Options options;
	public void setOptions(Options options) {
		this.options = options;
	}

	void notifyAddPack() {
        onlyStandardPacks = false;
    }

    private void init() {
        Pack p;

        // Jimple body creation
        addPack(p = new JimpleBodyPack());
        {
            p.add(new Transform("jb.tt", soot.toolkits.exceptions.TrapTightener.v()));
            p.add(new Transform("jb.ls", LocalSplitter.v()));
            p.add(new Transform("jb.a", Aggregator.v()));
            p.add(new Transform("jb.ule", UnusedLocalEliminator.v()));
            p.add(new Transform("jb.tr", TypeAssigner.v()));
            p.add(new Transform("jb.ulp", LocalPacker.v()));
            p.add(new Transform("jb.lns", LocalNameStandardizer.v()));
            p.add(new Transform("jb.cp", CopyPropagator.v()));
            p.add(new Transform("jb.dae", DeadAssignmentEliminator.v()));
            p.add(new Transform("jb.cp-ule", UnusedLocalEliminator.v()));
            p.add(new Transform("jb.lp", LocalPacker.v()));
            p.add(new Transform("jb.ne", NopEliminator.v()));
            p.add(new Transform("jb.uce", UnreachableCodeEliminator.v()));
        }

        // Call graph pack
        addPack(p = new CallGraphPack("cg"));
        {
            p.add(new Transform("cg.cha", CHATransformer.v()));
            p.add(new Transform("cg.spark", SparkTransformer.v()));
            p.add(new Transform("cg.paddle", PaddleHook.v()));
        }

        // Whole-Shimple transformation pack
        addPack(p = new ScenePack("wstp"));

        // Whole-Shimple Optimization pack
        addPack(p = new ScenePack("wsop"));

        // Whole-Jimple transformation pack
        addPack(p = new ScenePack("wjtp"));
        {
	    	p.add(new Transform("wjtp.tn", LockAllocator.v()));
        }

        // Whole-Jimple Optimization pack
        addPack(p = new ScenePack("wjop"));
        {
            p.add(new Transform("wjop.smb", StaticMethodBinder.v()));
            p.add(new Transform("wjop.si", StaticInliner.v()));
        }

        // Give another chance to do Whole-Jimple transformation
        // The RectangularArrayFinder will be put into this package.
        addPack(p = new ScenePack("wjap"));
        {
            p.add(new Transform("wjap.ra", RectangularArrayFinder.v()));
            p.add(new Transform("wjap.umt", UnreachableMethodsTagger.v()));
            p.add(new Transform("wjap.uft", UnreachableFieldsTagger.v()));
            p.add(new Transform("wjap.tqt", TightestQualifiersTagger.v()));
            p.add(new Transform("wjap.cgg", CallGraphGrapher.v()));
            p.add(new Transform("wjap.purity", PurityAnalysis.v())); // [AM]
        }

        // Shimple pack
        addPack(p = new BodyPack(Shimple.PHASE));

        // Shimple transformation pack
        addPack(p = new BodyPack("stp"));

        // Shimple optimization pack
        addPack(p = new BodyPack("sop"));
        {
            p.add(new Transform("sop.cpf", SConstantPropagatorAndFolder.v()));
        }

        // Jimple transformation pack
        addPack(p = new BodyPack("jtp"));

        // Jimple optimization pack
        addPack(p = new BodyPack("jop"));
        {
            p.add(new Transform("jop.cse", CommonSubexpressionEliminator.v()));
            p.add(new Transform("jop.bcm", BusyCodeMotion.v()));
            p.add(new Transform("jop.lcm", LazyCodeMotion.v()));
            p.add(new Transform("jop.cp", CopyPropagator.v()));
            p.add(new Transform("jop.cpf", ConstantPropagatorAndFolder.v()));
            p.add(new Transform("jop.cbf", ConditionalBranchFolder.v()));
            p.add(new Transform("jop.dae", DeadAssignmentEliminator.v()));
            p.add(new Transform("jop.nce", new NullCheckEliminator()));
            p.add(new Transform("jop.uce1", UnreachableCodeEliminator.v()));
            p.add(new Transform("jop.ubf1", UnconditionalBranchFolder.v()));
            p.add(new Transform("jop.uce2", UnreachableCodeEliminator.v()));
            p.add(new Transform("jop.ubf2", UnconditionalBranchFolder.v()));
            p.add(new Transform("jop.ule", UnusedLocalEliminator.v()));
        }

        // Jimple annotation pack
        addPack(p = new BodyPack("jap"));
        {
            p.add(new Transform("jap.npc", NullPointerChecker.v()));
            p.add(new Transform("jap.npcolorer", NullPointerColorer.v()));
            p.add(new Transform("jap.abc", ArrayBoundsChecker.v()));
            p.add(new Transform("jap.profiling", ProfilingGenerator.v()));
            p.add(new Transform("jap.sea", SideEffectTagger.v()));
            p.add(new Transform("jap.fieldrw", FieldTagger.v()));
            p.add(new Transform("jap.cgtagger", CallGraphTagger.v()));
            p.add(new Transform("jap.parity", ParityTagger.v()));
            p.add(new Transform("jap.pat", ParameterAliasTagger.v()));
            p.add(new Transform("jap.rdtagger", ReachingDefsTagger.v()));
            p.add(new Transform("jap.lvtagger", LiveVarsTagger.v()));
            p.add(new Transform("jap.che", CastCheckEliminatorDumper.v()));
            p.add(new Transform("jap.umt", new UnreachableMethodTransformer()));
            p.add(new Transform("jap.lit", LoopInvariantFinder.v()));
            p.add(new Transform("jap.aet", AvailExprTagger.v()));
            p.add(new Transform("jap.dmt", DominatorsTagger.v()));

        }

        // CFG Viewer
        /*addPack(p = new BodyPack("cfg"));
        {
            p.add(new Transform("cfg.output", CFGPrinter.v()));
        }*/

        // Grimp body creation
        addPack(p = new BodyPack("gb"));
        {
            p.add(new Transform("gb.a1", Aggregator.v()));
            p.add(new Transform("gb.cf", ConstructorFolder.v()));
            p.add(new Transform("gb.a2", Aggregator.v()));
            p.add(new Transform("gb.ule", UnusedLocalEliminator.v()));
        }

        // Grimp optimization pack
        addPack(p = new BodyPack("gop"));

        // Baf body creation
        addPack(p = new BodyPack("bb"));
        {
            p.add(new Transform("bb.lso", LoadStoreOptimizer.v()));
            p.add(new Transform("bb.pho", PeepholeOptimizer.v()));
            p.add(new Transform("bb.ule", UnusedLocalEliminator.v()));
            p.add(new Transform("bb.lp", LocalPacker.v()));
        }

        // Baf optimization pack
        addPack(p = new BodyPack("bop"));

        // Code attribute tag aggregation pack
        addPack(p = new BodyPack("tag"));
        {
            p.add(new Transform("tag.ln", LineNumberTagAggregator.v()));
            p.add(new Transform("tag.an", ArrayNullTagAggregator.v()));
            p.add(new Transform("tag.dep", DependenceTagAggregator.v()));
            p.add(new Transform("tag.fieldrw", FieldTagAggregator.v()));
        }

        // Dummy Dava Phase
        /*
         * Nomair A. Naeem 13th Feb 2006
         * Added so that Dava Options can be added as phase options rather
         * than main soot options since they only make sense when decompiling
         * The db phase options are added in soot_options.xml
         */
        addPack(p = new BodyPack("db"));
        {
        	p.add(new Transform("db.transformations", null));
        	p.add(new Transform("db.renamer", null));
        	p.add(new Transform("db.deobfuscate", null));
        	p.add(new Transform("db.force-recompile", null));
        }



        onlyStandardPacks = true;
    }

    public static PackManager v() {
        return G.v().soot_PackManager();
    }

    private final Map<String, Pack> packNameToPack = new HashMap<String, Pack>();
    private final List<Pack> packList = new LinkedList<Pack>();

    private void addPack( Pack p ) {
        if( packNameToPack.containsKey( p.getPhaseName() ) )
            throw new RuntimeException( "Duplicate pack "+p.getPhaseName() );
        packNameToPack.put( p.getPhaseName(), p );
        packList.add( p );
    }

    public boolean hasPack(String phaseName) {
        return getPhase( phaseName ) != null;
    }

    public Pack getPack(String phaseName) {
        Pack p = packNameToPack.get(phaseName);
        return p;
    }

    public boolean hasPhase(String phaseName) {
        return getPhase(phaseName) != null;
    }

    public HasPhaseOptions getPhase(String phaseName) {
        int index = phaseName.indexOf( "." );
        if( index < 0 ) return getPack( phaseName );
        String packName = phaseName.substring(0,index);
        if( !hasPack( packName ) ) return null;
        return getPack( packName ).get( phaseName );
    }

    public Transform getTransform(String phaseName) {
        return (Transform) getPhase( phaseName );
    }


    public Collection<Pack> allPacks() {
        return Collections.unmodifiableList( packList );
    }

    public void runPacks() {
        if (options.src_prec() == Options.src_prec_class && options.keep_line_number()){
            LineNumberAdder lineNumAdder = LineNumberAdder.v();
            lineNumAdder.internalTransform("", null);
        }

        if (options.whole_program() || options.whole_shimple()) {
            runWholeProgramPacks();
        }
        retrieveAllBodies();

        // if running coffi cfg metrics, print out results and exit
        if (soot.jbco.Main.metrics) {
          coffiMetrics();
          System.exit(0);
        }

		Iterator classes = Scene.v().getApplicationClasses().iterator();
        while( classes.hasNext() ) {
		    SootClass cl = (SootClass) classes.next();
		    runBodyPacks( cl );
		}
        handleInnerClasses();
    }

    public void coffiMetrics() {
      int tV = 0, tE = 0, hM = 0;
      double aM = 0;
      HashMap<SootMethod, int[]> hashVem = soot.coffi.CFG.methodsToVEM;
      Iterator<SootMethod> it = hashVem.keySet().iterator();
      while (it.hasNext()) {
        int vem[] = hashVem.get(it.next());
        tV+= vem[0];
        tE+= vem[1];
        aM+= vem[2];
        if (vem[2]>hM) hM = vem[2];
      }
      if (hashVem.size()>0)
        aM/=hashVem.size();

      G.v().out.println("Vertices, Edges, Avg Degree, Highest Deg:    "+tV+"  "+tE+"  "+aM+"  "+hM);
    }

    public void runBodyPacks() {
        runBodyPacks( reachableClasses() );
    }

    private ZipOutputStream jarFile = null;
    public void writeOutput() {
        if( options.output_jar() ) {
            String outFileName = SourceLocator.v().getOutputDir();
            try {
                jarFile = new ZipOutputStream(new FileOutputStream(outFileName));
            } catch( FileNotFoundException e ) {
                throw new CompilationDeathException("Cannot open output Jar file " + outFileName);
            }
        } else {
            jarFile = null;
        }
        if(options.verbose())
            PhaseDumper.v().dumpBefore("output");
            writeOutput( reachableClasses() );
            postProcessXML( reachableClasses() );
            releaseBodies( reachableClasses() );
        if(options.verbose())
            PhaseDumper.v().dumpAfter("output");
    }

    private void runWholeProgramPacks() {
        if (options.whole_shimple()) {
            ShimpleTransformer.v().transform();
            getPack("cg").apply();
            getPack("wstp").apply();
            getPack("wsop").apply();
        } else {
            getPack("cg").apply();
            getPack("wjtp").apply();
            getPack("wjop").apply();
            getPack("wjap").apply();
        }
        PaddleHook.v().finishPhases();
    }

    private void runBodyPacks( Iterator classes ) {
        while( classes.hasNext() ) {
            SootClass cl = (SootClass) classes.next();
            runBodyPacks( cl );
        }
    }

    private void handleInnerClasses(){
       InnerClassTagAggregator agg = InnerClassTagAggregator.v();
       agg.internalTransform("", null);
    }

    private void writeOutput( Iterator classes ) {
        while( classes.hasNext() ) {
            SootClass cl = (SootClass) classes.next();
            writeClass( cl );
        }
        try {
            if(jarFile != null) jarFile.close();
        } catch( IOException e ) {
            throw new CompilationDeathException( "Error closing output jar: "+e );
        }
    }

    private void releaseBodies( Iterator classes ) {
        while( classes.hasNext() ) {
            SootClass cl = (SootClass) classes.next();
            releaseBodies( cl );
        }
    }

    private Iterator reachableClasses() {
    	return Scene.v().getApplicationClasses().iterator();
    }

    private void runBodyPacks(SootClass c) {
        final int format = options.output_format();

        boolean produceJimple  = true;
        boolean produceBaf     = false;
        boolean produceGrimp   = false;
        boolean produceShimple = false;

        switch (format) {
            case Options.output_format_none :
            case Options.output_format_xml :
            case Options.output_format_jimple :
            case Options.output_format_jimp :
                break;
            case Options.output_format_shimp:
            case Options.output_format_shimple:
                produceShimple = true;
                // FLIP produceJimple
                produceJimple = false;
                break;
            case Options.output_format_grimp :
            case Options.output_format_grimple :
                produceGrimp = true;
                break;
            case Options.output_format_baf :
            case Options.output_format_b :
                produceBaf = true;
                break;
            case Options.output_format_jasmin :
            case Options.output_format_class :
                produceGrimp = options.via_grimp();
                produceBaf = !produceGrimp;
                break;
            default :
                throw new RuntimeException();
        }

        soot.xml.TagCollector tc = new soot.xml.TagCollector();

        boolean wholeShimple = options.whole_shimple();
        if( options.via_shimple() ) produceShimple = true;

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

            if(DEBUG){
            	if(m.getExceptions().size()!=0)
            		System.out.println("PackManager printing out jimple body exceptions for method "+m.toString()+" " + m.getExceptions().toString());
            }

            if (!m.isConcrete()) continue;

            if (produceShimple || wholeShimple) {
                ShimpleBody sBody = null;

                // whole shimple or not?
                {
                    Body body = m.retrieveActiveBody();

                    if(body instanceof ShimpleBody){
                        sBody = (ShimpleBody) body;
                        if(!sBody.isSSA())
                            sBody.rebuild();
                    }
                    else{
                        sBody = Shimple.v().newBody(body);
                    }
                }

                m.setActiveBody(sBody);
                PackManager.v().getPack("stp").apply(sBody);
                PackManager.v().getPack("sop").apply(sBody);

                if( produceJimple || (wholeShimple && !produceShimple) )
                    m.setActiveBody(sBody.toJimpleBody());
            }

            if (produceJimple) {
                JimpleBody body =(JimpleBody) m.retrieveActiveBody();
                PackManager.v().getPack("jtp").apply(body);
                if( options.validate() ) {
                    body.validate();
                }
                PackManager.v().getPack("jop").apply(body);
                PackManager.v().getPack("jap").apply(body);
                if (options.xml_attributes() && options.output_format() != Options.output_format_jimple) {
                    tc.collectBodyTags(body);
                }
            }

            if (produceGrimp) {
                m.setActiveBody(Grimp.v().newBody(m.getActiveBody(), "gb"));
                PackManager.v().getPack("gop").apply(m.getActiveBody());
            } else if (produceBaf) {
                m.setActiveBody(Baf.v().newBody((JimpleBody) m.getActiveBody()));
                PackManager.v().getPack("bop").apply(m.getActiveBody());
                PackManager.v().getPack("tag").apply(m.getActiveBody());
                if( options.validate() ) {
                    m.getActiveBody().validate();
                }
            }
        }
    }

    private void writeClass(SootClass c) {
        final int format = options.output_format();
        if( format == Options.output_format_none ) return;
        if( format == Options.output_format_dava ) return;

        OutputStream streamOut = null;
        PrintWriter writerOut = null;
        boolean noOutputFile = false;

        String fileName = SourceLocator.v().getFileNameFor(c, format);
        if( options.gzip() ) fileName = fileName+".gz";

        try {
            if( jarFile != null ) {
                ZipEntry entry = new ZipEntry(fileName);
                jarFile.putNextEntry(entry);
                streamOut = jarFile;
            } else {
                new File(fileName).getParentFile().mkdirs();
                streamOut = new FileOutputStream(fileName);
            }
            if( options.gzip() ) {
                streamOut = new GZIPOutputStream(streamOut);
            }
            if(format == Options.output_format_class) {
                streamOut = new JasminOutputStream(streamOut);
            }
            writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
            G.v().out.println( "Writing to "+fileName );
        } catch (IOException e) {
            throw new CompilationDeathException("Cannot output file " + fileName);
        }

        if (options.xml_attributes()) {
            Printer.v().setOption(Printer.ADD_JIMPLE_LN);
        }
        switch (format) {
            case Options.output_format_class :
            case Options.output_format_jasmin :
                if (c.containsBafBody())
                    new soot.baf.JasminClass(c).print(writerOut);
                else
                    new soot.jimple.JasminClass(c).print(writerOut);
                break;
            case Options.output_format_jimp :
            case Options.output_format_shimp :
            case Options.output_format_b :
            case Options.output_format_grimp :
                Printer.v().setOption(Printer.USE_ABBREVIATIONS);
                Printer.v().printTo(c, writerOut);
                break;
            case Options.output_format_baf :
            case Options.output_format_jimple :
            case Options.output_format_shimple :
            case Options.output_format_grimple :
                writerOut =
                    new PrintWriter(
                        new EscapedWriter(new OutputStreamWriter(streamOut)));
                Printer.v().printTo(c, writerOut);
                break;
            case Options.output_format_xml :
                writerOut =
                    new PrintWriter(
                        new EscapedWriter(new OutputStreamWriter(streamOut)));
                XMLPrinter.v().printJimpleStyleTo(c, writerOut);
                break;
            default :
                throw new RuntimeException();
        }

        try {
            writerOut.flush();
            streamOut.close();
        } catch (IOException e) {
            throw new CompilationDeathException("Cannot close output file " + fileName);
        }
    }

    private void postProcessXML( Iterator classes ) {
        if (!options.xml_attributes()) return;
        if (options.output_format() != Options.output_format_jimple) return;
        while( classes.hasNext() ) {
            SootClass c = (SootClass) classes.next();
            processXMLForClass(c);
        }
    }

    private void processXMLForClass(SootClass c, TagCollector tc){
        final int format = options.output_format();
        String fileName = SourceLocator.v().getFileNameFor(c, format);
        XMLAttributesPrinter xap = new XMLAttributesPrinter(fileName,
               SourceLocator.v().getOutputDir());
        xap.printAttrs(c, tc);
    }

    private void processXMLForClass(SootClass c){
        final int format = options.output_format();
        String fileName = SourceLocator.v().getFileNameFor(c, format);
        XMLAttributesPrinter xap = new XMLAttributesPrinter(fileName,
               SourceLocator.v().getOutputDir());
        xap.printAttrs(c);
    }

    private void releaseBodies( SootClass cl ) {
        Iterator methodIt = cl.methodIterator();
        while (methodIt.hasNext()) {
            SootMethod m = (SootMethod) methodIt.next();

            if (m.hasActiveBody())
                m.releaseActiveBody();
        }
    }

    private void retrieveAllBodies() {
        Iterator clIt = reachableClasses();
        while( clIt.hasNext() ) {
            SootClass cl = (SootClass) clIt.next();
            Iterator methodIt = cl.methodIterator();
            while (methodIt.hasNext()) {
                SootMethod m = (SootMethod) methodIt.next();
                if(DEBUG && cl.isApplicationClass()){
                	if(m.getExceptions().size()!=0)
                		System.out.println("PackManager printing out from within retrieveAllBodies exceptions for method "+m.toString()+" " + m.getExceptions().toString());
                	else
                		System.out.println("in retrieveAllBodies......Currently Method "+ m.toString() +" has no exceptions ");
                }

                if( m.isConcrete() ) {
                    m.retrieveActiveBody();
                }
            }
        }
    }
}
