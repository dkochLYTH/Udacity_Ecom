package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {
	
	public static void injectObject(Object target, String fieldName, Object toInject) {
		
		
		try {
			Field f = target.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			f.set(target, toInject);
			
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
