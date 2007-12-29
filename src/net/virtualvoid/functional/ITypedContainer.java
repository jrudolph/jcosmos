package net.virtualvoid.functional;

public interface ITypedContainer<T> {
	Class<? super T> getElementClass();
}
