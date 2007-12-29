package net.virtualvoid.functional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public abstract class TypeRef<T> {
	@SuppressWarnings("unchecked")
	public Class<T> clazz(){
		Type t=getClass().getGenericSuperclass();
		if (t instanceof Class<?>)
			throw new Error("not overridden");
		Type realType=((ParameterizedType)t).getActualTypeArguments()[0];
		if (realType instanceof Class<?>)
			return (Class<T>) realType;
		else if (realType instanceof TypeVariable<?>)
			return (Class<T>) ((TypeVariable<?>)realType).getBounds()[0];
		else
			return (Class<T>) ((ParameterizedType)realType).getRawType();
	}
}
