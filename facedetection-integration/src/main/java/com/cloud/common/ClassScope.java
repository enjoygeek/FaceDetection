package com.cloud.common;

import java.util.Vector;

public class ClassScope {
    private static java.lang.reflect.Field LIBRARIES;
    static {
    	try{
        LIBRARIES = ClassLoader.class.getDeclaredField("loadedLibraryNames");
        LIBRARIES.setAccessible(true);
    	}catch(Exception e){
    		
    	}
    }
    public static String[] getLoadedLibraries(final ClassLoader loader) throws IllegalArgumentException, IllegalAccessException {    	
        @SuppressWarnings("unchecked")
		final Vector<String> libraries = (Vector<String>) LIBRARIES.get(loader);
        return libraries.toArray(new String[] {});
    }
}