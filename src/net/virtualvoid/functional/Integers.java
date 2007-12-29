package net.virtualvoid.functional;

import net.virtualvoid.functional.Functions.IRichFunction1;
import net.virtualvoid.functional.Functions.RichFunction1;
import net.virtualvoid.functional.Predicates.AbstractPredicate;
import net.virtualvoid.functional.Predicates.Predicate;

public class Integers {
	public final static IRichFunction1<Integer,Integer> succ =
		new RichFunction1<Integer, Integer>(){
			public Integer apply(Integer arg1) {
				return arg1 + 1;
			}
		};
	public final static Predicate<Integer> lt(final int comp){
		return new AbstractPredicate<Integer>(){
			public boolean predicate(Integer arg1) {
				return arg1 < comp;
			}
		};
	}
}
