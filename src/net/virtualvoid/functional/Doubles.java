package net.virtualvoid.functional;

import net.virtualvoid.functional.Functions.IRichFunction1;
import net.virtualvoid.functional.Functions.IRichFunction2;
import net.virtualvoid.functional.Functions.RichFunction1;
import net.virtualvoid.functional.Functions.RichFunction2;

public class Doubles {
	public static final IRichFunction2<Number,Number,Double> divide =
		new RichFunction2<Number,Number,Double>(){
			public Double apply(Number arg1, Number arg2) {
				return arg1.doubleValue() / arg2.doubleValue();
			}
		};
	public static final IRichFunction1<Number,Double> multipleInverse =
		new RichFunction1<Number,Double>(){
			public Double apply(Number arg) {
				return 1d / arg.doubleValue();
			}
		};

}
