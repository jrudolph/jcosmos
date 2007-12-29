package net.virtualvoid.functional;

import net.virtualvoid.functional.Functions.Function1;

public interface IRichRandomAccessSequence<T> extends IRichSequence<T>,IRandomAccessSequence<T> {
	IRichRandomAccessSequence<T> sublist(int from, int length);
	<U> IRichRandomAccessSequence<U> map(Function1<? super T, U> func);
}
