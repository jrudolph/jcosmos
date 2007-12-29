package net.virtualvoid.functional.mutable;

import static java.text.MessageFormat.format;
import net.virtualvoid.functional.AbstractRandomAccessSequence;

public class Array<T> extends AbstractRandomAccessSequence<T>{
	private final T[] array;
	public Array(T...els){
		array = els;
	}
	public static <T> Array<T> instance(T... els){
		return new Array<T>(els);
	}
	@SuppressWarnings("unchecked")
	@Override
	public T[] asArray() {
		int len = array.length;
		T[] res = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), len);
		System.arraycopy(array, 0, res, 0, len);
		return res;
	}
	@Override
	public int length() {
		return array.length;
	}
	public T get(int index){
		return array[index];
	}
	@Override
	public String toString() {
		return format("Array<{0}> length = {1}",array.getClass().getComponentType().getSimpleName(),array.length);
	}
}
