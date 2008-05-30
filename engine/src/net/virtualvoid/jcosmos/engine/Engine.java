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

import java.util.Arrays;

import net.virtualvoid.jcosmos.Program;

public class Engine {
	public static void main(String[] args) {
		if (args.length<1){
			System.out.println("Usage: engine [classname]");
			System.exit(1);
		}

		String className = args[0];
		try {
			Class<?> cl = Class.forName(className);

			assert Program.class.isAssignableFrom(cl);

			Program program = ((Program)cl.newInstance());
			FactoryHelper.fillInImports(program);
			program.main(args.length>1?
					Arrays.copyOfRange(args, 1, args.length-1)
					:new String[0]);
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found: "+className);
			System.exit(1);
		} catch (Exception e) {
			throw new Error(e);
		}
	}
}
