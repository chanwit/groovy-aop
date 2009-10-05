package org.codehaus.groovy.gjit.soot.fannkuch;

import org.codehaus.groovy.runtime.callsite.AbstractCallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;

public class Fannkuch$fannkuch extends AbstractCallSite {

	private static final String[] NAMES = new String[]{
		"fannkuch","fannkuch","fannkuch",
		"fannkuch","fannkuch","fannkuch",
		"fannkuch","fannkuch","fannkuch",
	};

    public Fannkuch$fannkuch() {
        super(new CallSiteArray(Fannkuch.class, NAMES), 9, "fannkuch");
    }

    public Fannkuch$fannkuch(int index) {
        super(new CallSiteArray(Fannkuch.class, NAMES), index, NAMES[index]);
    }
}
