package org.codehaus.groovy.aop.pattern;

import java.util.List;

public class MethodPattern {

    public static final TypePattern[] EMPTY_TYPE_PATTERN = new TypePattern[]{};
        
    private TypePattern typePattern;
	private String namePattern;
	private TypePattern[] argTypePatterns;
    private boolean anyArgument=false;
    private boolean mayBeProperty=true;

    public boolean isMayBeProperty() {
        return mayBeProperty;
    }

    public void setMayBeProperty(boolean mayBeProperty) {
        this.mayBeProperty = mayBeProperty;
    }

    public TypePattern getTypePattern() {
		return typePattern;
	}
	public String getNamePattern() {
		return namePattern;
	}
	public TypePattern[] getArgTypePatterns() {
		return argTypePatterns;
	}

    public void setTypePattern(TypePattern c) {
        this.typePattern = c;
    }

    public void setNamePattern(String s) {
        this.namePattern = s;
    }

    public void setArgTypePatterns(List<TypePattern> p) {
        this.argTypePatterns = EMPTY_TYPE_PATTERN;
        this.mayBeProperty = false;

        if(p==null) return;

        if(p.size()==1 && p.get(0).getAny()) {
            this.anyArgument = true;
        } else {
            this.argTypePatterns = p.toArray(new TypePattern[p.size()]);
        }
    }

    public boolean getAnyArgument() {
        return anyArgument;
    }
}
