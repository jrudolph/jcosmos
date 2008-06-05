package net.virtualvoid.functional;

import net.virtualvoid.functional.Functions.Function1;

public interface RASeq<T> extends Seq<T>,IRandomAccessable<T> {
	RASeq<T> sublist(int from, int length);
	<U> RASeq<U> map(Function1<? super T, U> func);
}
