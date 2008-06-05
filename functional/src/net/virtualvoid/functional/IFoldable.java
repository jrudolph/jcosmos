package net.virtualvoid.functional;


public interface IFoldable<T> {
	<U> U fold(Function2<? super U, ? super T, U> func, U start);
}