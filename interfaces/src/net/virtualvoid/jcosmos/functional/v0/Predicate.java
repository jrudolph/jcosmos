/**
 *
 */
package net.virtualvoid.jcosmos.functional.v0;


public interface Predicate<T> extends F1<T,Boolean>,PredicateMin<T>{
	Predicate<T> and(Predicate<T> t2);
	Predicate<T> inverse();
	<U> Predicate<U> ofChild(F1<U,T> func);
}