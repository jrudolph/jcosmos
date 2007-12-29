package net.virtualvoid.functional;

public interface IRandomAccessable<T> {
	T get(int index);
	int length();
}
