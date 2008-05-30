/*
 The contents of this file are subject to the Mozilla Public License
 Version 1.1 (the "License"); you may not use this file except in
 compliance with the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/

 Software distributed under the License is distributed on an "AS IS"
 basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 License for the specific language governing rights and limitations
 under the License.

 The Initial Developer of the Original Code is Johannes Rudolph.
 Portions created by the Initial Developer are Copyright (C) 2008
 the Initial Developer. All Rights Reserved.

 Contributor(s):
    Johannes Rudolph <johannes_rudolph@gmx.de>
*/

package net.virtualvoid.jcosmos.engine;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import net.virtualvoid.jcosmos.annotation.Import;
import net.virtualvoid.numbers.DoubleNumberFactoryImpl;
import net.virtualvoid.numbers.RationalNumberFactoryImpl;
import net.virtualvoid.numbers.RichNumberBridgeImpl;

public class FactoryHelper {
	static Class<?>[] getExportedInterfaces(Object o){
		return o.getClass().getInterfaces();
	}

	static Map<Class<?>,Object> registry = new HashMap<Class<?>, Object>();
	static{
		Object[]impls = {new DoubleNumberFactoryImpl()
			,new RichNumberBridgeImpl()
			,new RationalNumberFactoryImpl()
		};

		for (Object o:impls)
			for (Class<?> itf:getExportedInterfaces(o))
				registry.put(itf,null);


		for (Object o:impls)
			for (Class<?> itf:getExportedInterfaces(o))
				registry.put(itf, fillInImports(o));
	}
	private static <T> T getFactory(final Class<T> clazz){
		T res = clazz.cast(registry.get(clazz));
		if (res == null){
			if (registry.containsKey(clazz)){
				// if object is expected but not yet loaded
				// supply a proxy at least

				return clazz.cast(Proxy.newProxyInstance(FactoryHelper.class.getClassLoader(), new Class<?>[]{clazz}, new InvocationHandler(){
					@Override
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						T res = getFactory(clazz);
						if (res == null)
							throw new Error("should be initialized by now");
						return method.invoke(res, args);
					}
				}));
			}
			else
				throw new Error("Implementation not found");
		}
		else
			return res;
	}
	public static Object fillInImports(Object o){
		for (Field f:o.getClass().getDeclaredFields()){
			if (f.isAnnotationPresent(Import.class)){
				try {
					f.setAccessible(true);
					f.set(o, getFactory(f.getType()));
				} catch (Exception e) {
					throw new Error(e);
				}
			}
		}
		return o;
	}
}
