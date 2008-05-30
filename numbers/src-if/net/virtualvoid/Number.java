package net.virtualvoid;

public interface Number extends NumberMin{
	Number add(NumberMin n2);
	Number mult(NumberMin n2);
	Number neg();
	Number multInv();
	double doubleValue();

	Number sub(NumberMin n2);
	Number div(NumberMin n2);
}
