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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

public class VerboseCL extends URLClassLoader{
	public VerboseCL(String name,URL[] urls) {
		super(urls);
		this.name=name;
	}
	public VerboseCL(String name,URL[] urls, ClassLoader parent) {
		super(urls, parent);
		this.name=name;
	}

	String name;

	private void out(String format,Object...args){
		System.out.println(String.format("[%s] %s",name,String.format(format,args)));
	}

	@Override
	protected Class<?> findClass(String name)
			throws ClassNotFoundException {
		//System.err.println("["+this.name+"] Trying to find: "+name);
		Class<?> res = super.findClass(name);
		out("loaded class %s",name);
		return res;
	}
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		out("Finalizing");
	}

	@Override
	public URL findResource(String name) {
		out("Trying to find %s",name);
		URL res = super.findResource(name);
		if (res!=null)
			out("Found resource %s",name);
		return res;
	}
	@Override
	public URL getResource(String name) {
		out("Trying to get %s",name);
		URL res = super.getResource(name);
		if (res != null)
			out("got %s",name);
		return res;
	}
	@Override
	public Enumeration<URL> findResources(String name) throws IOException {
		out("Trying to find Resources %s",name);
		return super.findResources(name);
	}
}