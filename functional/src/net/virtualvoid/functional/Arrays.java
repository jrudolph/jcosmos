package net.virtualvoid.functional;

import net.virtualvoid.jcosmos.annotation.Export;
import net.virtualvoid.jcosmos.functional.v0.RASeq;

@Export
public class Arrays implements net.virtualvoid.jcosmos.functional.util.v0.Arrays{
	public RASeq<Byte> fromPrimitive(final byte[] bs) {
		return new AbstractRandomAccessSequence<Byte>(){
			@Override
			public int length() {
				return bs.length;
			}
			public Byte get(int index) {
				return bs[index];
			}
		};
	}
}
