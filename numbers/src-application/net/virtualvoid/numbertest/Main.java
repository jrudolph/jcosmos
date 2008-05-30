package net.virtualvoid.numbertest;

import net.virtualvoid.DoubleNumberFactory;
import net.virtualvoid.FactoryHelper;
import net.virtualvoid.Number;
import net.virtualvoid.Program;
import net.virtualvoid.RationalNumber;
import net.virtualvoid.RationalNumberFactory;

public class Main implements Program{
	private final static DoubleNumberFactory factory = FactoryHelper.getFactory(DoubleNumberFactory.class);
	private final static RationalNumberFactory rs = FactoryHelper.getFactory(RationalNumberFactory.class);
	private static Number d(double d){
		return factory.newInstance(d);
	}
	public void main(String[] args) {
		Number n1 = d(5.5);
		Number n2 = d(4.5);
		assert Math.abs(n1.sub(n2).doubleValue()-1.0) < 0.0001;

		RationalNumber r1 = rs.newInstance(5, 2);
		RationalNumber r2 = rs.newInstance(4, 3);
		RationalNumber res = r1.add(r2);
		assert res.numerator() == 23;
		assert res.denominator() == 6;

		RationalNumber res2 = r1.mult(r2);
		assert res2.numerator() == 10;
		assert res2.denominator() == 3;

		RationalNumber kuerz = rs.newInstance(24, 3);
		assert kuerz.numerator() == 8;
		assert kuerz.denominator() == 1;
	}
}
