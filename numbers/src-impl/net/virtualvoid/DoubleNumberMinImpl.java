package net.virtualvoid;

public class DoubleNumberMinImpl implements NumberMin{
	final double value;
	private final static DoubleNumberFactory factory  = FactoryHelper.getFactory(DoubleNumberFactory.class);
	DoubleNumberMinImpl(double d){
		value=d;
	}
	static NumberMin d(double d){
		return factory.newInstance(d);
	}
	public NumberMin add(NumberMin n2) {
		return d(value+n2.doubleValue());
	}
	public double doubleValue() {
		return value;
	}
	public NumberMin mult(NumberMin n2) {
		return d(value*n2.doubleValue());
	}
	public NumberMin multInv() {
		return d(1./value);
	}
	public NumberMin neg() {
		return d(-value);
	}
}
