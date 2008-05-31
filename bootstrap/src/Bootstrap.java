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

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import net.virtualvoid.jcosmos.engine.VerboseCL;

public class Bootstrap {
	public static void main(String[] args) {
		try {
			URLClassLoader ifCl = new VerboseCL("InterfaceCL",new URL[]{new URL("file:../interfaces/bin/")});
			URL engURL = new URL("file:../engine/bin/");

			URLClassLoader engCl = new VerboseCL("EngineCL",new URL[]{engURL},ifCl);

			Class<?> engine = engCl.loadClass("net.virtualvoid.jcosmos.engine.Engine");
			Method setCl = engine.getDeclaredMethod("setCl", ClassLoader.class);
			setCl.invoke(null, ifCl);

			Method main = engine.getMethod("main", String[].class);
			main.invoke(null,(Object)args);
		} catch (Exception e) {
			throw new Error(e);
		}
	}
}
