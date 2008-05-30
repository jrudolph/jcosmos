package net.virtualvoid.numbers;

import net.virtualvoid.NumberMin;

public class DoubleNumberInv {
	boolean addInv(NumberMin recv,NumberMin arg1,NumberMin res){
		return res.doubleValue() == recv.doubleValue() + arg1.doubleValue();
	}
	boolean multInv(NumberMin recv,NumberMin arg1,NumberMin res){
		return res.doubleValue() == recv.doubleValue() * arg1.doubleValue();
	}
	boolean multInvInv(NumberMin recv,NumberMin res){
		return res.doubleValue() == 1. / recv.doubleValue();
	}
	boolean negInv(NumberMin recv,NumberMin res){
		return res.doubleValue() == -recv.doubleValue();
	}
}
