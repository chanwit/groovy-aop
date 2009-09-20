package org.codehaus.groovy.geelab;

import java.util.Iterator;
import java.util.List;

import org.codehaus.groovy.geelab.linearalgebra.Complex;
import org.codehaus.groovy.geelab.linearalgebra.ComplexMatrix;

import groovy.lang.Binding;
import groovy.lang.Range;

public class GeeBinding extends Binding {

    @Override
    public void setProperty(String property, Object newValue) {
        super.setProperty(property, newValue);
    }

    @Override
    public void setVariable(String name, Object value) {
        //
        // try to convert List<List> to matrix
        // if anything goes wrong, just store it as it is
        //
        try {
            if(value instanceof Range) {
                Range r = (Range)value;
                double[] result = new double[r.size()*2];
                int i = 0;
                for (Iterator iterator = r.iterator(); iterator.hasNext();) {
                    Number n = (Number) iterator.next();
                    result[i] = n.doubleValue();
                    i = i + 2;
                }
                value = new ComplexMatrix(1, r.size(), result);
            } else {
                value = tryConvertToMatrix(value);
            }
        } catch (Throwable e) { }
        super.setVariable(name, value);
    }

    private Object tryConvertToMatrix(Object value) {
        if(value instanceof List) {
            List list = (List)value;
            if(list.size() > 0 && list.get(0) instanceof List) {
                int rows = list.size();
                int cols = ((List) list.get(0)).size();
                int rowspan = cols * 2;
                double[] data = new double[rows * cols * 2];
                for(int i=0;i<rows;i++) {
                    List<List> row = (List<List>)list.get(i);
                    for(int j=0;j<rows;j++) {
                        if(row.get(j) instanceof Number) {
                            Number number = (Number) row.get(j);
                            data[i*rowspan + (2*j)] = number.doubleValue();
                        } else if(row.get(j) instanceof Complex) {
                            Complex complex = (Complex) row.get(j);
                            data[(i*rowspan) + (2*j)] = complex.getReal();
                            data[(i*rowspan) + (2*j) + 1] = complex.getImaginary();
                        }
                    }
                }
                value = new ComplexMatrix(rows, cols, data);
            }
        }
        return value;
    }

}
