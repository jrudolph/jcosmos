package net.virtualvoid.functional;

import java.util.Comparator;

import net.virtualvoid.functional.mutable.Array;
import net.virtualvoid.jcosmos.functional.v0.F1;
import net.virtualvoid.jcosmos.functional.v0.F2;
import net.virtualvoid.jcosmos.functional.v0.Tuple2;

public interface Seq<T> extends IFoldable<T>{
	T[] asNativeArray(Class<T> elementClass);
	Array<T> asArray();
	int length();
	Seq<Tuple2<Integer,T>> withIndex();
	<U> Seq<U> map(F1<? super T, U> func);
	void foreach(F1<? super T,?> func);
	Seq<T> select(Predicate<? super T> predicate);
	T reduce(F2<? super T,? super T,T> func);
	T reduceThreaded(F2<? super T,? super T,T> func);
	Array<T> sort(Comparator<T> comparator);
	T first();
	Seq<T> join(Seq<? extends T> other);

	Seq<T> append(T last);
	Seq<T> prepend(T first);
}
