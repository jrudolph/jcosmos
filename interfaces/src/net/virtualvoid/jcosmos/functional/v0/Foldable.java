package net.virtualvoid.jcosmos.functional.v0;



public interface Foldable<T> {
	<U> U fold(F2<? super U, ? super T, U> func, U start);
}