package net.virtualvoid;

public class DoubleNumberFactoryImpl implements DoubleNumberFactory{
	private final static LazyVal<NumberImplementor> implementor = new LazyVal<NumberImplementor>(){
		@Override
		protected NumberImplementor retrieve() {
			return FactoryHelper.getFactory(NumberImplementor.class);
		}
	};
	public Number newInstance(double d) {
		return implementor.get().newInstance(new DoubleNumberMinImpl(d));
	}
}
