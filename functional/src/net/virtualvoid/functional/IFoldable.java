package net.virtualvoid.functional;

import net.virtualvoid.jcosmos.functional.v0.F2;


public interface IFoldable<T> {
	<U> U fold(F2<? super U, ? super T, U> func, U start);
}