package net.virtualvoid.functional;

import net.virtualvoid.functional.Functions.Function1;
import net.virtualvoid.functional.Tuples.Tuple2;

public interface IRichSequence<T> extends ISequence<T>{
	T[] asArray();
	int length();
	IRichSequence<Tuple2<Integer,T>> withIndex();
	public <U> IRichSequence<U> map(Function1<? super T, U> func);
	public IRichSequence<T> select(Function1<? super T, Boolean> predicate);
}
