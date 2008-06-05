package net.virtualvoid.functional;

import java.util.Comparator;

import net.virtualvoid.functional.Predicates.Predicate;
import net.virtualvoid.functional.Tuples.Tuple2;
import net.virtualvoid.functional.mutable.Array;

public interface Seq<T> extends IFoldable<T>{
	T[] asNativeArray(Class<T> elementClass);
	Array<T> asArray();
	int length();
	Seq<Tuple2<Integer,T>> withIndex();
	<U> Seq<U> map(Function1<? super T, U> func);
	void foreach(Function1<? super T,?> func);
	Seq<T> select(Predicate<? super T> predicate);
	T reduce(Function2<? super T,? super T,T> func);
	T reduceThreaded(Function2<? super T,? super T,T> func);
	Array<T> sort(Comparator<T> comparator);
	T first();
	Seq<T> join(Seq<? extends T> other);

	Seq<T> append(T last);
	Seq<T> prepend(T first);
}
