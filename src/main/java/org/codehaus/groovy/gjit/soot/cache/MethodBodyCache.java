package org.codehaus.groovy.gjit.soot.cache;

import java.util.concurrent.ConcurrentHashMap;

import soot.Body;

public class MethodBodyCache extends ConcurrentHashMap<String, Body> {

	private static final long serialVersionUID = 1L;
	private static MethodBodyCache _instance;

	private MethodBodyCache() { }

	public static MethodBodyCache instance() {
		if(_instance == null) _instance = new MethodBodyCache();
		return _instance;
	}

}
