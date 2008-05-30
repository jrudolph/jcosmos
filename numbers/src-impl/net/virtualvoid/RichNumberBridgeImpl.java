package net.virtualvoid;

public class RichNumberBridgeImpl implements NumberImplementor{
	public Number newInstance(final NumberMin n) {
		return new Number(){
			public Number div(NumberMin n2) {
				return newInstance(n.mult(n2.multInv()));
			}
			public Number sub(NumberMin n2) {
				return newInstance(n.add(n2.neg()));
			}
			public Number add(NumberMin n2) {
				return newInstance(n.add(n2));
			}
			public double doubleValue() {
				return n.doubleValue();
			}
			public Number mult(NumberMin n2) {
				return newInstance(n.mult(n2));
			}
			public Number multInv() {
				return newInstance(n.multInv());
			}
			public Number neg() {
				return newInstance(n.neg());
			}
		};
	}
}
