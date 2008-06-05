package net.virtualvoid.functional;


public interface IFoldable<T> {
	<U> U fold(F2<? super U, ? super T, U> func, U start);
}