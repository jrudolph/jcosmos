package net.virtualvoid;

public class RationalNumberFactoryImpl implements RationalNumberFactory{
	@Override
	public RationalNumber newInstance(long num, long denom) {
		return RationalNumberImpl.fGekuerzt(num,denom);
	}
}
