package net.virtualvoid.functional;

import net.virtualvoid.functional.Functions.Function1;
import net.virtualvoid.functional.Functions.Function2;
import net.virtualvoid.functional.Tuples.Tuple2;

public interface ISequence<T> extends IFoldable<T>{
	T[] asArray();
	int length();
	ISequence<Tuple2<Integer,T>> withIndex();
	<U> ISequence<U> map(Function1<? super T, U> func);
	ISequence<T> select(Function1<? super T, Boolean> predicate);
	T reduce(Function2<? super T,? super T,T> func,T start);
}
