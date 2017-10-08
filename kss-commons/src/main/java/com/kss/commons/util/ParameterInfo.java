package com.kss.commons.util;


import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * 
 * @author 黄永俊
 * @version 2012-04-01
 *
 */
public class ParameterInfo {
    private final int index;
    private final String name;
    private final Class<?> type;
    private final Annotation[] annotations; 
    
    public ParameterInfo(int index, Class<?> type, String name) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.annotations = null;       
    }

    public ParameterInfo(int index, Class<?> type, String name, Annotation[] annotations) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.annotations = annotations;       
    }
    
    public ParameterInfo(int index, Class<?> type, Annotation[] annotations) {
        this.index = index;
        this.name = null;
        this.type = type;
        this.annotations = annotations;       
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

  

    public int getIndex() {
        return index;
    }

    public Class<?> getType() {
        return type;
    }

    /**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ParameterInfo");
        sb.append("[index=").append(index);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", annotations=").append(
        		annotations == null ? "null" : Arrays.asList(annotations).toString());        
        sb.append(']');
        return sb.toString();
    }
}
