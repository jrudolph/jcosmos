


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
