package com.ozstrategy.model;

import java.lang.reflect.Field;
import java.util.Map;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReflectHelper {

	public static Object isPage(Object obj, String fieldName) {
		if (obj instanceof Map) {
			Map map = (Map) obj;
			return map.get(fieldName);
		} else {
			return getFieldByFieldName(obj, fieldName);
		}
	}

	public static Field getFieldByFieldName(Object obj, String fieldName) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				;
			}
		}
		return null;
	}

	public static Object getValueByFieldName(Object obj, String fieldName) throws Exception {
		Field field = getFieldByFieldName(obj, fieldName);
		Object value = null;
		if (field == null) {
			return value;
		}
		if (field.isAccessible()) {
			value = field.get(obj);
		} else {
			field.setAccessible(true);
			value = field.get(obj);
			field.setAccessible(false);
		}
		return value;
	}

	public static void setValueByFieldName(Object obj, String fieldName, Object value) throws Exception {
		if (obj instanceof Map) {
			Map map = (Map) obj;
			map.put(fieldName, value);
			return;
		}
		Field field = obj.getClass().getDeclaredField(fieldName);
		if (field.isAccessible()) {
			field.set(obj, value);
		} else {
			field.setAccessible(true);
			field.set(obj, value);
			field.setAccessible(false);
		}
	}
}
