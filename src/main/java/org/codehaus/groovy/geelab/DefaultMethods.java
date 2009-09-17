package org.codehaus.groovy.geelab;

import groovy.lang.Script;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class DefaultMethods {

    private static final int TAB_SIZE = 10;

    public static Object getWhos(Script self) {
        who(self, null);
        return null;
    }

    public static void who(Script self, Object name) {
        Map vars = self.getBinding().getVariables();
        if(name == null) {
            System.out.println("Name\t\t\tType");
            String line="";
            for(int i=0;i<80;i++) line+="-";
            System.out.println(line);
            System.out.println();
            Set<Entry> entries = vars.entrySet();
            for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
                Entry entry = (Entry) iterator.next();
                String keyName = entry.getKey().toString();
                if(keyName.equals("_outputTransforms")) continue;
                if(keyName.equals("_")) continue;
                if(keyName.equals("__")) continue;
                if(keyName.equals("args")) continue;
                System.out.print(keyName);
                int tabs = 3 - (keyName.length()/TAB_SIZE);
                if(keyName.length() % TAB_SIZE == 0) System.out.print("\t");
                for (int i = 0; i < tabs ; i++) 	 System.out.print("\t");
                System.out.println(entry.getValue());
            }
        } else {
            System.out.println(name);
        }
    }

}
