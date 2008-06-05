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

import net.virtualvoid.functional.Seq;
import net.virtualvoid.functional.Sequences;
import net.virtualvoid.functional.Predicates.AbstractPredicate;
import net.virtualvoid.functional.mutable.Array;
import net.virtualvoid.jcosmos.Extractor;
import net.virtualvoid.jcosmos.Implementation;
import net.virtualvoid.jcosmos.Module;
import net.virtualvoid.jcosmos.Repository;
import net.virtualvoid.jcosmos.functional.v0.F1;
import net.virtualvoid.jcosmos.io.ClassLocations;

public class SimpleRepository implements Repository{
	private static Extractor e = new SimpleExtractor();
	private static ClassLocations l = new LocationFactory();

	private static F1<File, Module> file2module = new F1<File,Module>(){
		public Module apply(File arg1) {
			return e.extract(l.fromFile(arg1));
		}
		public Class<Module> getResultType() {
			return Module.class;
		}
	};

	private static Seq<Module> modules = Array.instance(
		new File("../numbers/bin")
		,new File("../calc-app/bin"))
			.map(file2module)
			.asArray();

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

	private static <T,U> Seq<U> flatMap(F1<T,Seq<U>> func,Seq<T> seq){
		return seq.map(func).fold(Sequences.<U>join(), Sequences.<U>emptySequence());
	}
	public Implementation[] getImplementations(final Class<?> ifClass) {
		return flatMap(new F1<Module,Seq<Implementation>>(){
				public Seq<Implementation> apply(Module arg1) {
					return Array.instance(arg1.getExports())
						.select(implementsIf(ifClass));
				}
				public Class<Seq<Implementation>> getResultType() {
					return (Class)Seq.class;
				}
			},modules)
			.asNativeArray(Implementation.class);
	}
}
