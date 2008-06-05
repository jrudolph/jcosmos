package net.virtualvoid.functional.util;

import java.util.Comparator;

import net.virtualvoid.functional.Predicates;
import net.virtualvoid.functional.Functions.RichFunction1;
import net.virtualvoid.functional.Predicates.AbstractPredicate;
import net.virtualvoid.functional.Tuples.Tuple2Impl;
import net.virtualvoid.jcosmos.functional.v0.F1;
import net.virtualvoid.jcosmos.functional.v0.F2;
import net.virtualvoid.jcosmos.functional.v0.Predicate;
import net.virtualvoid.jcosmos.functional.v0.Seq;
import net.virtualvoid.jcosmos.functional.v0.Tuple2;

public class Comparators {
	public static <T extends Comparable<T>> Comparator<T> fromComparable(Class<T> clazz){
		return new Comparator<T>(){
			public int compare(T o1, T o2) {
				return o1.compareTo(o2);
			}
		};
	}
	public static <T,U> Comparator<T> byChild(final F1<? super T,U> child,final Comparator<? super U> childComparator){
		return new Comparator<T>(){
			public int compare(T o1, T o2) {
				return childComparator.compare(child.apply(o1), child.apply(o2));
			}
		};
	}
	public static <T> Comparator<T> fromFunction(final F2<T,T,Integer> comp){
		return new Comparator<T>(){
			public int compare(T o1, T o2) {
				return comp.apply(o1, o2);
			}
		};
	}
	public static <T extends Comparable<T>> Predicate<T> lessThan(final T value){
		return new AbstractPredicate<T>(){
			public boolean predicate(T arg1) {
				return arg1.compareTo(value)<0;
			}
		};
	}
	public static <T extends Comparable<T>,U> F1<T,Tuple2<T,U>> rangeMap(final Tuple2<T,U> defaultValue,final Seq<Tuple2<T,U>> data){
		return new RichFunction1<T,Tuple2<T,U>>(){
			public Tuple2<T,U> apply(T arg1) {
				Tuple2<T,U> res = data.select(
						Predicates.predicate(Tuple2Impl.<T,U>ele1F().combineWith(lessThan(arg1))))
					.first();
				return res == null ? defaultValue : res;
			}
		};
	}
}
