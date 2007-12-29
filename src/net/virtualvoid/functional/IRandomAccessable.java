package net.virtualvoid.functional;

public interface IRandomAccessable<T> extends ITypedContainer<T>{
	T get(int index);
	int length();
}
