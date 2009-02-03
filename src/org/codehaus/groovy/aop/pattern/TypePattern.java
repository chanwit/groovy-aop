package org.codehaus.groovy.aop.pattern;

public class TypePattern {
	
	private boolean primitive=false;
	private boolean subClass=false;
	private String classPattern;
	
	public TypePattern(boolean primitive, String token) {
		this.primitive = primitive;
		this.classPattern = token;
	}

    public TypePattern() {
    }

    public TypePattern(String[] names) {
        if(names.length==1) {
            classPattern = null;            
        } else {
            StringBuffer sb = new StringBuffer();
            for(int i=0;i<names.length-1;i++) {
                sb.append(names[i]);
                sb.append(".");
            }
            sb.deleteCharAt(sb.length()-1);
            classPattern = sb.toString();
        }
    }

    public boolean isPrimitive() {
		return primitive;
	}

	public boolean isSubClass() {
		return subClass;
	}

	public String getClassPattern() {
		return classPattern;
	}

    public void setSubClass(boolean b) {
        this.subClass = b;
    }

    public void setClassPattern(String s) {
        if(s.equals("def")) {
            this.classPattern = "java.lang.Object";
        } else {
            this.classPattern = s;
        }
    }

    public void setPrimitive(boolean b) {
        this.primitive = b;
    }

    public boolean getAny() {
        return classPattern.equals("..");
    }
}