package net.virtualvoid.functional;

import net.virtualvoid.functional.Functions.Function1;
import net.virtualvoid.functional.Functions.Function2;

public interface ISequence<T> {
	<U> ISequence<U> map(Function1<? super T,U> func);
	<U> U fold(Function2<? super U,? super T,U> func,U start);
	T reduce(Function2<? super T,? super T,T> func,T start);
	ISequence<T> select(Function1<? super T,Boolean> predicate);
}
