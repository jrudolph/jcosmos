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

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.virtualvoid.jcosmos.annotation.Import;

public class FactoryHelper {
	static Class<?>[] getExportedInterfaces(Object o){
		return o.getClass().getInterfaces();
	}
	static class Implementation{
		String ifName;
		String implName;
		public Implementation(String ifName, String implName) {
			super();
			this.ifName = ifName;
			this.implName = implName;
		}
	}
	static class Module{
		String name;
		String url;
		Implementation[] implementations;
	}

	static Module numbersModule = new Module(){{
		name="numbers";
		url="file:../numbers/bin/";
		implementations = new Implementation[]{
				new Implementation(
						"net.virtualvoid.numbers.DoubleNumberFactory"
						,"net.virtualvoid.numbers.DoubleNumberFactoryImpl")
				,new Implementation(
						"net.virtualvoid.numbers.RationalNumberFactory"
						,"net.virtualvoid.numbers.RationalNumberFactoryImpl")
				,new Implementation(
						"net.virtualvoid.numbers.NumberImplementor"
						,"net.virtualvoid.numbers.RichNumberBridgeImpl")};
	}};

	private static Module getModule(String className){
		return numbersModule;
	}
	private static Map<Module,WeakReference<ClassLoader>> loadedModules =
		new HashMap<Module, WeakReference<ClassLoader>>();

	private static String getImplementationClass(Module m,String className){
		for (Implementation impl:m.implementations)
			if (impl.ifName.equals(className))
				return impl.implName;
		return null;
	}
	static Map<Class<?>,Object> registry = new HashMap<Class<?>, Object>();

	private static Object loading = new Object();

	private static <T> T getFactory(final Class<T> clazz){
		T res = clazz.cast(registry.get(clazz));
		if (res == null){
			Module module = getModule(clazz.getName());
			WeakReference<ClassLoader> ref = loadedModules.get(module);

			ClassLoader cl;
			if (ref==null||(cl=ref.get())==null){
				try {
					cl = new VerboseCL(module.name,new URL[]{new URL(module.url)},Engine.ifCl);
					loadedModules.put(module,new WeakReference<ClassLoader>(cl));
				} catch (MalformedURLException e) {
					throw new Error(e);
				}
			}

			try {
				Class<?> implClazz = cl.loadClass(getImplementationClass(module, clazz.getName()));
				Object factory = implClazz.newInstance();
				registry.put(clazz,loading);
				fillInImports(factory);
				registry.put(clazz, factory);
				return clazz.cast(factory);
			} catch (Exception e) {
				throw new Error(e);
			}
		}
		else if (res == loading){
			return clazz.cast(Proxy.newProxyInstance(FactoryHelper.class.getClassLoader(), new Class<?>[]{clazz}, new InvocationHandler(){
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
