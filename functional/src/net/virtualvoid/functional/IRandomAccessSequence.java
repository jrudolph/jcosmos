package net.virtualvoid.functional;

import net.virtualvoid.functional.Functions.Function1;

public interface IRandomAccessSequence<T> extends ISequence<T>,IRandomAccessable<T> {
	IRandomAccessSequence<T> sublist(int from, int length);
	<U> IRandomAccessSequence<U> map(Function1<? super T, U> func);
}
