package net.virtualvoid.functional;

import java.util.Comparator;

import net.virtualvoid.functional.Functions.Function1;
import net.virtualvoid.functional.Functions.Function2;
import net.virtualvoid.functional.Tuples.Tuple2;
import net.virtualvoid.functional.mutable.Array;

public interface ISequence<T> extends IFoldable<T>{
	T[] asNativeArray(Class<T> elementClass);
	Array<T> asArray();
	int length();
	ISequence<Tuple2<Integer,T>> withIndex();
	<U> ISequence<U> map(Function1<? super T, U> func);
	ISequence<T> select(Function1<? super T, Boolean> predicate);
	T reduce(Function2<? super T,? super T,T> func);
	T reduceThreaded(Function2<? super T,? super T,T> func);
	Array<T> sort(Comparator<T> comparator);
	T first();
	ISequence<T> join(ISequence<? extends T> other);

	ISequence<T> append(T last);
	ISequence<T> prepend(T first);
}
