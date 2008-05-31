package net.virtualvoid.jcosmos.engine;
import java.net.URL;
import java.net.URLClassLoader;

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

	@Override
	protected Class<?> findClass(String name)
			throws ClassNotFoundException {
		System.err.println("["+this.name+"] Trying to find: "+name);
		Class<?> res = super.findClass(name);
		System.err.println("["+this.name+"] Found "+name);
		return res;
	}
}