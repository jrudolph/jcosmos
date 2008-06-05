package net.virtualvoid.jcosmos.functional.v0;



public interface RASeq<T> extends Seq<T>,RandomAccessable<T> {
	RASeq<T> sublist(int from, int length);
	<U> RASeq<U> map(F1<? super T, U> func);
}
