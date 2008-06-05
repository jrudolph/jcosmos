package net.virtualvoid.functional;

import net.virtualvoid.jcosmos.functional.v0.F1;


public class Predicates {
	public static abstract class AbstractPredicate<T>
		implements Predicate<T> {
		public Predicate<T> and(final Predicate<T> pred2) {
			final Predicate<T> outerThis = this;
			return new AbstractPredicate<T>(){
				public boolean predicate(T arg1) {
					return outerThis.predicate(arg1) && pred2.predicate(arg1);
				}
			};
		}
		public Predicate<T> inverse() {
			return new AbstractPredicate<T>(){
				public boolean predicate(T arg1) {
					return !AbstractPredicate.this.predicate(arg1);
				}
			};
		}
		public final Boolean apply(T arg1) {
			return predicate(arg1);
		}
		public <U> Predicate<U> ofChild(F1<U, T> func) {
			return combine(func,this);
		}
	}
	public static <T> Predicate<T> predicate(final F1<T,Boolean> func){
		return new AbstractPredicate<T>(){
			public boolean predicate(T arg1) {
				return func.apply(arg1);
			}
		};
	}
	public static <T> Predicate<T> equalsP(final T other){
		return new AbstractPredicate<T>(){
			public boolean predicate(T v) {
				return other.equals(v);
			}
		};
	}
	public static <T,U> Predicate<T> combine(final F1<T,U> func,final Predicate<? super U> pred){
		return new AbstractPredicate<T>(){
			public boolean predicate(T u) {
				return pred.predicate(func.apply(u));
			}
		};
	}
}
