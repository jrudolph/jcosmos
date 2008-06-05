package net.virtualvoid.functional;

import net.virtualvoid.functional.Functions.Function2;

public interface IFoldable<T> extends ITypedContainer<T>{
	<U> U fold(Function2<? super U, ? super T, U> func, U start);
}