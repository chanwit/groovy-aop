package org.codehaus.groovy.gjit.soot;

import java.util.HashMap;

public class CallSiteArrayPack extends HashMap<String,String[]>{

	private static final long serialVersionUID = 1L;
	
	private static CallSiteArrayPack instance;

	private CallSiteArrayPack() {}
	
	public static CallSiteArrayPack v() {
		if(instance==null) {
			instance = new CallSiteArrayPack();
		}
		return instance;
	}	

}
