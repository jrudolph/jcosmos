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

import net.virtualvoid.jcosmos.Extractor;
import net.virtualvoid.jcosmos.Implementation;
import net.virtualvoid.jcosmos.Module;
import net.virtualvoid.jcosmos.ModulePolicy;
import net.virtualvoid.jcosmos.ModuleStorage;
import net.virtualvoid.jcosmos.Repository;
import net.virtualvoid.jcosmos.annotation.Import;
import net.virtualvoid.jcosmos.functional.v0.Predicates;
import net.virtualvoid.jcosmos.functional.v0.Seqs;
import net.virtualvoid.jcosmos.io.ClassLocations;

public class FactoryHelper {
	static Class<?>[] getExportedInterfaces(Object o){
		return o.getClass().getInterfaces();
	}

	private final static ModulePolicy policy = new FirstModulePolicy();
	private static ModuleStorage storage = new FileSystemStorage();

	Map<Module,WeakReference<ClassLoader>> loadedModules =
		new HashMap<Module, WeakReference<ClassLoader>>();

	Map<Class<?>,WeakReference<Object>> registry = new HashMap<Class<?>, WeakReference<Object>>();

	Repository repo;

	private static <T> WeakReference<T> ref(T val){
		return new WeakReference<T>(val);
	}
	private <T> void register(Class<T> clazz,T obj){
		registry.put((Class)clazz, (WeakReference)ref(obj));
		fillInImports(obj);
	}
	public void init(){
		try {
			ClassLoader preFunc = new VerboseCL("preliminaryFunctional",new URL[]{new URL("file:../functional/bin/")},Engine.ifCl);
			Seqs Seqs=(Seqs)preFunc.loadClass("net.virtualvoid.functional.Sequences").newInstance();
			Predicates Preds = (Predicates)preFunc.loadClass("net.virtualvoid.functional.Predicates").newInstance();
			SimpleExtractor extractor = new SimpleExtractor();
			LocationFactory locationFactory = new LocationFactory();

			register(Seqs.class,Seqs);
			register(Predicates.class,Preds);
			register(ClassLocations.class, locationFactory);
			register(Extractor.class, extractor);

			repo = new SimpleRepository();
			fillInImports(repo);

		} catch (Exception e) {
			throw new Error(e);
		}
	}

	<T> T getFactory(final Class<T> clazz){
		return getFactory(clazz,policy,repo);
	}
	<T> T getFactory(final Class<T> clazz,ModulePolicy policy){
		return getFactory(clazz,policy,repo);
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
	<T> T getFactory(final Class<T> clazz,ModulePolicy policy,Repository repo){
		WeakReference<Object> objectRef = registry.get(clazz);
		T res;
		if (objectRef==null||(res = clazz.cast(objectRef.get())) == null){
			Implementation[] candidates = repo.getImplementations(clazz);
			if (candidates.length < 1)
				throw new Error("No candidates found for "+clazz.getSimpleName());

			Implementation impl = policy.decide(candidates);
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
	public Object fillInImports(Object o){
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
