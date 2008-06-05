package net.virtualvoid.functional;


public interface RASeq<T> extends Seq<T>,IRandomAccessable<T> {
	RASeq<T> sublist(int from, int length);
	<U> RASeq<U> map(F1<? super T, U> func);
}
