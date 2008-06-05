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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.virtualvoid.crypto.SHA1Hash;
import net.virtualvoid.functional.Predicates;
import net.virtualvoid.functional.mutable.Array;
import net.virtualvoid.jcosmos.Extractor;
import net.virtualvoid.jcosmos.Implementation;
import net.virtualvoid.jcosmos.Module;
import net.virtualvoid.jcosmos.annotation.Export;
import net.virtualvoid.jcosmos.functional.v0.F1;
import net.virtualvoid.jcosmos.io.ClassLocation;

public class SimpleExtractor implements Extractor{
	public Module extract(final ClassLocation url) {
		return new Module(){
			String name = SimpleExtractor.getName(url);

			Module thisModule = this;
			final Implementation[] impls =
				Array.instance(url.getClasses())
					.select(new Predicates.AbstractPredicate<Class<?>>(){
						public boolean predicate(Class<?> v) {
							return v.isAnnotationPresent(Export.class);
						}
					})
					.map(new F1<Class<?>,Implementation>(){
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
				/*return new SHA1Hash(){
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
				};*/
				return url.getId();
			}
			public String getName() {
				return name;
			}
		};
	}
	private static String getName(ClassLocation loc){
		try {
			Properties props = new Properties();
			InputStream resource = loc.getResource("cosmos.properties");
			if (resource==null)
				throw new Error("Couldn't find cosmos.properties for location "+loc.toString());
			props.load(resource);
			if (!props.containsKey("module-name"))
				throw new Error("Module name for location "+loc.toString()+" not found");
			return props.getProperty("module-name");
		} catch (IOException e) {
			throw new Error(e);
		}
	}
}
