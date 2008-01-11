package net.virtualvoid.functional;

import net.virtualvoid.functional.Functions.AbstractFunction;
import net.virtualvoid.functional.Functions.Function1;

public class Predicates {
	public static interface Predicate<T> extends Function1<T,Boolean>{
		Predicate<T> and(Predicate<T> t2);
		Predicate<T> inverse();
		boolean predicate(T v);
	};
	public static abstract class AbstractPredicate<T>
		extends AbstractFunction<Boolean>
		implements Predicate<T> {
		protected AbstractPredicate() {
			super(Boolean.class);
		}
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
	}
	public static <T> Predicate<T> predicate(final Function1<T,Boolean> func){
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
}
