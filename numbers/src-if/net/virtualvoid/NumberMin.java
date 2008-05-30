package net.virtualvoid;

public interface NumberMin {
	NumberMin add(NumberMin n2);
	NumberMin mult(NumberMin n2);
	NumberMin neg();
	NumberMin multInv();
	double doubleValue();
}
