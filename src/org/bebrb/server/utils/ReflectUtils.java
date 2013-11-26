package org.bebrb.server.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public class ReflectUtils {
	
	@SuppressWarnings("rawtypes")
	public static <T extends Object, Y extends Object> void copyFields(T from, Y to) throws Exception {
		if(from==null || to==null) return;

		// getters
	    Class<? extends Object> fromClass = from.getClass();
	    Method[] fromMethods = fromClass.getDeclaredMethods();

	    Class<? extends Object> tooClass = to.getClass();
	    Field[] tooFields = tooClass.getDeclaredFields();
	    try {
		    if (fromMethods!=null && tooFields != null) {
		        for (Field tooF : tooFields) {
	            	String prefix = "get";
	            	if(tooF.getType().isAssignableFrom(Boolean.class) || tooF.getType() == boolean.class) prefix = "is";
	            	String fname = prefix + 
	            			String.valueOf(tooF.getName().charAt(0)).toUpperCase()+
	            			tooF.getName().substring(1);
	            	// only from getters
	            	Method fromM = findMethod(fromMethods,fname);
	            	
	            	if(fromM!=null && !tooF.isAnnotationPresent(NoCopy.class))
	            		if(Collection.class.isAssignableFrom(tooF.getType()) && Collection.class.isAssignableFrom(fromM.getReturnType())) {
	            			Collection<?> sourcelist = (Collection<?>) fromM.invoke(from);
		                	tooF.setAccessible(true);
		                	Collection col =  (Collection) tooF.get(to);
		                	if(col==null) {
		                		col = sourcelist.getClass().newInstance();
		            			tooF.set(to,col);
		                	}
		                	if(tooF.isAnnotationPresent(CopyInDepth.class)) {
		                		for (Object object : sourcelist) {
		                			ParameterizedType param = (ParameterizedType)tooF.getGenericType();
		                			Type clsparam = param.getActualTypeArguments()[0];
		                			Object obj = ((Class<? extends Object>) clsparam).newInstance();
									copyFields(object, obj);
		                			col.add(obj);
								}
		                	}
		                	
	            		} else
		            	if(tooF.isAnnotationPresent(CopyInDepth.class)) {
		                	tooF.setAccessible(true);
		                	Object retObj = fromM.invoke(from);
		                	if(retObj!=null) {
			            		Y to1 = (Y) tooF.get(to);
			            		if(to1==null) {
			            			to1 = (Y) tooF.getType().newInstance();
			            			tooF.set(to,to1);
			            		}
								copyFields(retObj, to1);
		                	}
		            	} else
		                if (tooF.getType().isAssignableFrom(String.class) || tooF.getType().isAssignableFrom(fromM.getReturnType())
		                	|| isBoxing(tooF.getType(),fromM.getReturnType())) {
		                	
		                	Object retObj = fromM.invoke(from);
		                	
		                	tooF.setAccessible(true);
		                	// Object->String
		                	if(tooF.getType().isAssignableFrom(String.class) && !(retObj instanceof String))
		                		retObj = retObj.toString();
		                	// assign
		                    tooF.set(to, retObj);
		                }
		        }
		    }
		} catch (InvocationTargetException e) {
			throw (Exception)e.getTargetException();
		}
	}

	private static boolean isBoxing(Class<?> class1, Class<?> class2) {
		boolean prim = class1.isPrimitive() || class2.isPrimitive();
		if(!prim) return false;
		Class<?> primClass = class1.isPrimitive()?class1:class2;
		Class<?> otherClass = class1.isPrimitive()?class2:class1;
		
		return (primClass == boolean.class && otherClass == Boolean.class) ||
			   (primClass == int.class && otherClass == Integer.class) ||	
			   (primClass == char.class && otherClass == Character.class) ||	
			   (primClass == double.class && otherClass == Double.class) ||	
			   (primClass == float.class && otherClass == Float.class) ||	
			   (primClass == long.class && otherClass == Long.class) ||	
			   (primClass == short.class && otherClass == Short.class) ||	
			   (primClass == byte.class && otherClass == Byte.class);	
	}

	private static Field findField(Field[] fromFields, String fname) {
		for (Field field : fromFields) {
			if(field.getName().equals(fname)) return field;
		}
		return null;
	};	    

	private static Method findMethod(Method[] fromMethods, String fname) {
		for (Method m : fromMethods) {
			if(m.getName().equals(fname)) return m;
		}
		return null;
	};	    
	
}
