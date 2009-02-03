package org.codehaus.groovy.aop.abstraction.pcd;

import java.util.regex.Pattern;

import org.codehaus.groovy.aop.abstraction.Joinpoint;

public class BlockPCD extends AbstractPCD {

	@Override
	protected boolean doMatches(Pattern pt, Joinpoint jp) {
		return false;
	}

}
