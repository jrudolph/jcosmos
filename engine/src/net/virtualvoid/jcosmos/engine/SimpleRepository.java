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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.virtualvoid.crypto.SHA1Hash;
import net.virtualvoid.functional.AbstractRichSequence;
import net.virtualvoid.functional.ISequence;
import net.virtualvoid.functional.Predicates;
import net.virtualvoid.functional.Sequences;
import net.virtualvoid.functional.Functions.Function1;
import net.virtualvoid.functional.Functions.Function2;
import net.virtualvoid.functional.Predicates.AbstractPredicate;
import net.virtualvoid.functional.mutable.Array;
import net.virtualvoid.jcosmos.Implementation;
import net.virtualvoid.jcosmos.Module;
import net.virtualvoid.jcosmos.Repository;
import net.virtualvoid.jcosmos.annotation.Export;

public class SimpleRepository implements Repository{
	private static FileFilter dirs = new FileFilter(){
		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}
	};
	static FileFilter classFiles = new FileFilter(){
		public boolean accept(File pathname) {
			return pathname.getName().endsWith(".class");
		}
	};
	static ISequence<File> files(final File dir,final FileFilter filter){
		assert dir.isDirectory();
		return new AbstractRichSequence<File>(){
			public <U> U fold(Function2<? super U, ? super File, U> func,
					U start) {
				for (File f:dir.listFiles(filter))
					start = func.apply(start, f);
				for (File f:dir.listFiles(dirs))
					start = files(f,filter).fold(func, start);
				return start;
			}
			public Class<File> getElementClass() {
				return File.class;
			}
		};
	}
	static String subtractFromFront(String str1,String str2){
		assert str1.startsWith(str2);
		return str1.substring(str2.length());
	}
	static ISequence<Class<?>> classEnumerator(final File dir){
		try {
			final ClassLoader cl = new VerboseCL("enumeratorHelper",new URL[]{new URL("file:"+dir.getAbsolutePath()+"/")},Engine.ifCl);
			return files(dir,classFiles)
				.map(new Function1<File,Class<?>>(){
					public Class<?> apply(File arg1) {
						try {
							String name = subtractFromFront(arg1.getCanonicalPath(),dir.getCanonicalPath()+"/");
							name = name.replace(File.separatorChar, '.')
								.replaceAll("\\.class$", "");

							return cl.loadClass(name);
						} catch (Exception e) {
							throw new Error(e);
						}
					}
					public Class<Class<?>> getResultType() {
						return (Class) Class.class;
					}
				});
		} catch (MalformedURLException e) {
			throw new Error(e);
		}
	}
	static Module fromFolder(final String name,final File dir){
		return new Module(){
			Module thisModule = this;
			final Implementation[] impls = classEnumerator(dir)
			.select(new Predicates.AbstractPredicate<Class<?>>(){
				public boolean predicate(Class<?> v) {
					return v.isAnnotationPresent(Export.class);
				}
			})
			.map(new Function1<Class<?>,Implementation>(){
				public Implementation apply(final Class<?> arg1) {
					final String name = arg1.getName();
					final Class<?> ifClazz = arg1.getInterfaces()[0];

					return new Implementation(){
						public String getImplementationClassName() {
							return name;
						}
						public Class<?> getInterfaceClass() {
							return ifClazz;
						}
						public Module getModule() {
							return thisModule;
						}
					};
				}
				public Class<Implementation> getResultType() {
					return Implementation.class;
				}
			})
			.asNativeArray(Implementation.class);

			public Implementation[] getExports() {
				return impls;
			}
			public SHA1Hash getId() {
				return new SHA1Hash(){
					public String asString() {
						try {
							// TODO: that's cheating for now
							// since it is not yet defined
							// how sha1 of folders will work
							return dir.getCanonicalPath();
						} catch (IOException e) {
							throw new Error(e);
						}
					}
					public byte[] asBytes() {
						return null;
					}
				};
			}
			public String getName() {
				return name;
			}
		};
	}

	private static Array<Module> modules = Array.instance(
		fromFolder("numbers", new File("../numbers/bin"))
		,fromFolder("calc-test", new File("../calc-app/bin")));

	public Module[] getModules() {
		return modules.asNativeArray(Module.class);
	}
	private static AbstractPredicate<Implementation> implementsIf(final Class<?>ifClass){
		return new AbstractPredicate<Implementation>(){
			public boolean predicate(Implementation v) {
				return ifClass.isAssignableFrom(v.getInterfaceClass());
			}
		};
	}

	private static <T,U> ISequence<U> flatMap(Function1<T,ISequence<U>> func,ISequence<T> seq){
		return seq.map(func).fold(Sequences.<U>join(), Sequences.<U>emptySequence());
	}
	public Implementation[] getImplementations(final Class<?> ifClass) {
		return flatMap(new Function1<Module,ISequence<Implementation>>(){
				public ISequence<Implementation> apply(Module arg1) {
					return Array.instance(arg1.getExports())
						.select(implementsIf(ifClass));
				}
				public Class<ISequence<Implementation>> getResultType() {
					return (Class)ISequence.class;
				}
			},modules)
			.asNativeArray(Implementation.class);
	}
}
