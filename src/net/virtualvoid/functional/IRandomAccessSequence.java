package net.virtualvoid.functional;

public interface IRandomAccessSequence<T> extends ISequence<T>{
	T get(int index);
	IRandomAccessSequence<T> sublist(int from, int length);
}
