package net.virtualvoid.jcosmos.functional.v0;

public interface RandomAccessable<T>{
	T get(int index);
	int length();
}
