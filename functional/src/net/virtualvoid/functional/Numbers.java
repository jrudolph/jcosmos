package net.virtualvoid.functional;

import net.virtualvoid.jcosmos.annotation.Export;
import net.virtualvoid.jcosmos.functional.v0.F1;

@Export
public class Numbers implements net.virtualvoid.jcosmos.functional.util.v0.Numbers{
	public F1<String, Byte> asByte(final int radix) {
		return new F1<String, Byte>(){
			public Byte apply(String arg1) {
				return Byte.valueOf(arg1, radix);
			}
		};
	}
}
