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

import net.virtualvoid.jcosmos.Implementation;
import net.virtualvoid.jcosmos.ModulePolicy;
import net.virtualvoid.jcosmos.Program;

public class Engine {
	static ClassLoader ifCl;
	public static void setCl(ClassLoader ifCl){
		Engine.ifCl=ifCl;
	}
	private static ModulePolicy byName(final String name){
		return new ModulePolicy(){
			public Implementation decide(Implementation[] impls) {
				for(Implementation impl:impls)
					if (impl.getModule().getName().equals(name))
						return impl;
				return null;
			}
		};
	}

	public static void main(String[] args) {
		if (args.length<1){
			System.out.println("Usage: engine <module> [<classname>]");
			System.exit(1);
		}

		String module = args[0];
		//String className = args[1];
		try {
			FactoryHelper.getFactory(Program.class, byName(module))
				.main(new String[0]);

			System.in.read();
		} catch (Exception e) {
			throw new Error(e);
		}
		System.gc();
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
