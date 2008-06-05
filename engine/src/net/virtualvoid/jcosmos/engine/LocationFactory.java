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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import net.virtualvoid.crypto.SHA1Hash;
import net.virtualvoid.functional.AbstractRichSequence;
import net.virtualvoid.functional.ISequence;
import net.virtualvoid.functional.Functions.Function1;
import net.virtualvoid.functional.Functions.Function2;
import net.virtualvoid.jcosmos.io.ClassLocation;
import net.virtualvoid.jcosmos.io.ClassLocations;

public class LocationFactory implements ClassLocations{
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
	public ClassLocation fromFile(final File f){
		return new ClassLocation(){
			public Class<?>[] getClasses() {
				return classEnumerator(f)
					.asNativeArray((Class)Class.class);
			}
			public SHA1Hash getId() {
				return new SHA1Hash(){
					public byte[] asBytes() {
						return null;
					}
					public String asString() {
						return f.getAbsolutePath();
					}
				};
			}
			public InputStream getResource(String path) {
				try {
					return new FileInputStream(f.getAbsolutePath()+"/"+path);
				} catch (FileNotFoundException e) {
					return null;
				}
			}
			@Override
			public String toString() {
				return String.format("Filesystem [%s]",f.getPath());
			}
		};
	}
}
