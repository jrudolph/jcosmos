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
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.virtualvoid.jcosmos.Implementation;
import net.virtualvoid.jcosmos.Module;
import net.virtualvoid.jcosmos.ModulePolicy;
import net.virtualvoid.jcosmos.ModuleStorage;
import net.virtualvoid.jcosmos.Repository;
import net.virtualvoid.jcosmos.annotation.Import;

public class FactoryHelper {
	static Class<?>[] getExportedInterfaces(Object o){
		return o.getClass().getInterfaces();
	}
	/*static class Implementation{
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
	}*/
	private final static Repository repo = new SimpleRepository();
	private final static ModulePolicy policy = new FirstModulePolicy();
	private static ModuleStorage storage = new FileSystemStorage();

	private static Map<Module,WeakReference<ClassLoader>> loadedModules =
		new HashMap<Module, WeakReference<ClassLoader>>();

	static Map<Class<?>,WeakReference<Object>> registry = new HashMap<Class<?>, WeakReference<Object>>();

	static <T> T getFactory(final Class<T> clazz){
		return getFactory(clazz,policy);
	}
	static class UnresolvedDependencyException extends RuntimeException{
		Class<?> clazz;

		public UnresolvedDependencyException(Class<?> clazz) {
			super();
			this.clazz = clazz;
		}
		@Override
		public String getMessage() {
			return "Couldn't find implementation for "+clazz.getName();
		}
	}
	static <T> T getFactory(final Class<T> clazz,ModulePolicy policy){
		WeakReference<Object> objectRef = registry.get(clazz);
		T res;
		if (objectRef==null||(res = clazz.cast(objectRef.get())) == null){
			Implementation impl = policy.decide(repo.getImplementations(clazz));
			if (impl == null)
				throw new UnresolvedDependencyException(clazz);

			Module module = impl.getModule();
			WeakReference<ClassLoader> ref = loadedModules.get(module);

			ClassLoader cl;
			if (ref==null||(cl=ref.get())==null){
				cl = new VerboseCL(module.getName(),new URL[]{storage.getModuleLocation(module.getId())},Engine.ifCl);
				loadedModules.put(module,new WeakReference<ClassLoader>(cl));
			}

			try {
				Class<?> implClazz = cl.loadClass(impl.getImplementationClassName());
				Object factory = implClazz.newInstance();
				registry.put(clazz, new WeakReference<Object>(factory));
				fillInImports(factory);
				return clazz.cast(factory);
			} catch (Exception e) {
				throw new Error(e);
			}
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
