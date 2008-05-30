package net.virtualvoid;

public interface RationalNumber extends Number{
	public RationalNumber add(RationalNumber n2);
	public RationalNumber mult(RationalNumber n2);
	public RationalNumber div(RationalNumber n2);
	public RationalNumber sub(RationalNumber n2);

	public RationalNumber multInv();
	public RationalNumber neg();

	long numerator();
	long denominator();
}
