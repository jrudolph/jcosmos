package net.virtualvoid;

public class RationalNumberImpl implements RationalNumber{
	private final long num;
	private final long denom;
	static DoubleNumberFactory factory = FactoryHelper.getFactory(DoubleNumberFactory.class);
	RationalNumberImpl(long num, long denom) {
		this.num = num;
		this.denom = denom;
	}
	private static long ggt(long n1,long n2){
	    long mod = n1 % n2;
	    if (mod == 0)
	    	return n2;
	    else
	    	return ggt(n2,mod);
	}
	public long denominator() {
		return denom;
	}
	public long numerator() {
		return num;
	}
	static RationalNumberImpl f(long num,long denom){
		return new RationalNumberImpl(num,denom);
	}
	static RationalNumberImpl fGekuerzt(long num,long denom){
		long t=ggt(num,denom);
		return f(num/t,denom/t);
	}
	public Number add(NumberMin n2) {
		if (n2 instanceof RationalNumber)
			return add((RationalNumber)n2);
		else
			return factory.newInstance(doubleValue()+n2.doubleValue());
	}
	public double doubleValue() {
		return num/denom;
	}
	public Number mult(NumberMin n2) {
		if (n2 instanceof RationalNumber)
			return mult((RationalNumber)n2);
		else
			return factory.newInstance(doubleValue()*n2.doubleValue());
	}
	public RationalNumber multInv() {
		return f(denom,num);
	}
	public RationalNumber neg() {
		return f(-num,denom);
	}
	@Override
	public RationalNumber add(RationalNumber r2) {
		return fGekuerzt(num*r2.denominator()+r2.numerator()*denom, denom*r2.denominator());
	}
	@Override
	public RationalNumber mult(RationalNumber r2) {
		return fGekuerzt(num*r2.numerator(),denom*r2.denominator());
	}

	private static final NumberImplementor numbers = FactoryHelper.getFactory(NumberImplementor.class);
	private Number meFat(){
		return numbers.newInstance(this);
	}

	@Override
	public RationalNumber sub(RationalNumber n2) {
		return (RationalNumber) meFat().sub(n2);
	}
	@Override
	public Number sub(NumberMin n2) {
		return meFat().sub(n2);
	}

	@Override
	public RationalNumber div(RationalNumber n2) {
		return (RationalNumber) meFat().div(n2);
	}
	@Override
	public Number div(NumberMin n2) {
		return meFat().div(n2);
	}
}