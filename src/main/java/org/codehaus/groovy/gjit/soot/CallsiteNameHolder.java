package org.codehaus.groovy.gjit.soot;

import java.util.concurrent.ConcurrentHashMap;

import soot.SootClass;

public class CallsiteNameHolder extends ConcurrentHashMap<String, String[]> {

	private static CallsiteNameHolder instance;

	public static ConcurrentHashMap<String, String[]> v() {
		if(instance == null)
			instance = new CallsiteNameHolder();

		return instance;
	}

}
