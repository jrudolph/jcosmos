package net.virtualvoid.jcosmos.functional.v0;

import java.util.Comparator;

public interface Seq<T> extends Foldable<T>{
	T[] asNativeArray(Class<T> elementClass);
	RASeq<T> asArray();
	int length();
	Seq<Tuple2<Integer,T>> withIndex();
	<U> Seq<U> map(F1<? super T, U> func);
	void foreach(F1<? super T,?> func);
	Seq<T> select(Predicate<? super T> predicate);
	T reduce(F2<? super T,? super T,T> func);
	T reduceThreaded(F2<? super T,? super T,T> func);
	RASeq<T> sort(Comparator<T> comparator);
	T first();
	Seq<T> join(Seq<? extends T> other);

	Seq<T> append(T last);
	Seq<T> prepend(T first);
}
