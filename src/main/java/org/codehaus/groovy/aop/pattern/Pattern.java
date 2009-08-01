package org.codehaus.groovy.aop.pattern;

import java.util.*;

public class Pattern {
	
	private MethodPattern methodPattern;	
	Map<String, Boolean> modifiers = new HashMap<String, Boolean>();
    private TypePattern returnTypePattern;
    private boolean anyModifier = true;
    private boolean anyReturnType = true;

    public boolean getAnyModifier() {
        return anyModifier;
    }
    
    public Map<String, Boolean> getModifiers() {
		return modifiers;
	}

	public void addModifier(String modifierName) {
        anyModifier = false;
        modifiers.put(modifierName, true);
	}
	
	public TypePattern getReturnTypePattern() {
		return returnTypePattern;
	}

	public MethodPattern getMethodPattern() {
		return methodPattern;
	}

    public void setModifiers(List<String> mod) {
        if(mod == null) return;
        if(mod.size()==0) return;
        anyModifier = false;
        for(String s: mod) {
            modifiers.put(s, true);
        }
    }

    public void setReturnTypePattern(TypePattern rtp) {
        anyReturnType = rtp == null;
        this.returnTypePattern = rtp;
    }

    public void setMethodPattern(MethodPattern mep) {
        this.methodPattern = mep;
        
        if(this.methodPattern==null) return;
        if(!this.methodPattern.isMayBeProperty()) return;

        if(returnTypePattern != null && returnTypePattern.getClassPattern().equals("void")) {
            this.methodPattern.setMayBeProperty(false);
        }
    }

    public boolean getAnyReturnType() {
        return anyReturnType;
    }
}
