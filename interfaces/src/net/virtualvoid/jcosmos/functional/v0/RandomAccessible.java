package net.virtualvoid.jcosmos.functional.v0;

public interface RandomAccessible<T>{
	T get(int index);
	int length();
}
