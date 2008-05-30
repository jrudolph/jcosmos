package net.virtualvoid;

public class FactoryHelper {
	private final static Object []registry = {
		new DoubleNumberFactoryImpl(),
		new RichNumberBridgeImpl(),
		new RationalNumberFactoryImpl()
	};
	public static <T> T getFactory(Class<T> clazz){
		 for (Object o:registry)
			 if (clazz.isInstance(o))
				 return clazz.cast(o);
		 throw new Error("Implementation not found");
	}
}
