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

import net.virtualvoid.jcosmos.Extractor;
import net.virtualvoid.jcosmos.Implementation;
import net.virtualvoid.jcosmos.Module;
import net.virtualvoid.jcosmos.Repository;
import net.virtualvoid.jcosmos.annotation.Export;
import net.virtualvoid.jcosmos.annotation.Import;
import net.virtualvoid.jcosmos.functional.v0.F1;
import net.virtualvoid.jcosmos.functional.v0.Predicate;
import net.virtualvoid.jcosmos.functional.v0.PredicateMin;
import net.virtualvoid.jcosmos.functional.v0.Predicates;
import net.virtualvoid.jcosmos.functional.v0.Seq;
import net.virtualvoid.jcosmos.functional.v0.Seqs;
import net.virtualvoid.jcosmos.io.ClassLocations;

@Export
public class SimpleRepository implements Repository{
	@Import	protected Seqs Seqs;
	@Import protected static Predicates preds;
	@Import	protected Extractor Extractor;
	@Import	protected ClassLocations ClassLocations;

	private final F1<File, Module> file2module = new F1<File,Module>(){
		public Module apply(File arg1) {
			return Extractor.extract(ClassLocations.fromFile(arg1));
		}
	};

	private final LazyVal<Seq<Module>> modules = new LazyVal<Seq<Module>>(){
		@Override
		protected net.virtualvoid.jcosmos.functional.v0.Seq<Module> retrieve() {
			return Seqs.array(
					new File("../numbers/bin")
					,new File("../calc-app/bin"))
						.map(file2module)
						.asArray();
		};
	};

	public Module[] getModules() {
		return modules.get().asNativeArray(Module.class);
	}

	private static Predicate<Implementation> implementsIf(final Class<?>ifClass){
		return preds.predicate(new PredicateMin<Implementation>(){
			public boolean predicate(Implementation v) {
				return ifClass.isAssignableFrom(v.getInterfaceClass());
			}
		});
	}

	private <T,U> Seq<U> flatMap(F1<T,Seq<U>> func,Seq<T> seq){
		return seq.map(func).fold(Seqs.<U>join(), Seqs.<U>emptySequence());
	}
	public Implementation[] getImplementations(final Class<?> ifClass) {
		return flatMap(new F1<Module,Seq<Implementation>>(){
				public Seq<Implementation> apply(Module arg1) {
					return Seqs.array(arg1.getExports())
						.select(implementsIf(ifClass));
				}
				public Class<Seq<Implementation>> getResultType() {
					return (Class)Seq.class;
				}
			},modules.get())
			.asNativeArray(Implementation.class);
	}
}
