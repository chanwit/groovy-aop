public class AdviceInvoker {
    
    private CallSite delegate;
    private EffectiveAdvices ea;
    
    public AdviceInvoker (CallSite delegate, EffectiveAdvices effectiveAdviceCodes) {
        this.delegate = delegate;
        this.ea = effectiveAdviceCodes;
    }
    
    //
    // Template
    //
    public call() {
        for(int i = 0; i < before.count; i++) {
            before[i].call(context);
        }
        // context.proceed = delegate;
        // around(context);
        delegate.call();
        for(int i = 0; i < after.count; i++) {
            after[i].call(context);
        }        
    }
    
}
