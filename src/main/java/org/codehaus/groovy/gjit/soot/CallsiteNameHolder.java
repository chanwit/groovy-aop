package org.codehaus.groovy.gjit.soot;

import java.util.concurrent.ConcurrentHashMap;

import soot.SootClass;

public class CallsiteNameHolder extends ConcurrentHashMap<SootClass, String[]> {

	private static CallsiteNameHolder instance;

	public static ConcurrentHashMap<SootClass, String[]> v() {
		if(instance != null)
			instance = new CallsiteNameHolder();

		return instance;
	}

}
