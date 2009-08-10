package org.codehaus.groovy.gjit.soot;

import java.util.concurrent.ConcurrentHashMap;

import soot.SootClass;

public class CallSiteNameHolder extends ConcurrentHashMap<String, String[]> {

	private static CallSiteNameHolder instance;

	public static ConcurrentHashMap<String, String[]> v() {
		if(instance == null)
			instance = new CallSiteNameHolder();

		return instance;
	}

}
