package net.virtualvoid.functional;

import static java.text.MessageFormat.format;
import net.virtualvoid.jcosmos.functional.v0.F1;
import net.virtualvoid.jcosmos.functional.v0.F2;

public abstract class AbstractRandomAccessSequence<T>
	extends AbstractRichSequence<T>
	implements RASeq<T>{

	public RASeq<T> sublist(final int from, final int length) {
		assert from >= 0;
		assert length > 0;
		assert from + length <= length();

		return new AbstractRandomAccessSequence<T>(){
			public T get(int index) {
				return AbstractRandomAccessSequence.this.get(from + index);
			}
			@Override
			public int length() {
				return length;
			}
			@Override
			public String toString() {
				return format("Sublist[{0}..{1}) of {2}",from,from+length,AbstractRandomAccessSequence.this.toString());
			}
		};
	}
	public <U> U fold(F2<? super U, ? super T, U> func, U start) {
		for(int i=0;i<length();i++)
			start = func.apply(start, get(i));
		return start;
	}
	@Override
	public abstract int length();

	@Override
	public Seq<? extends Seq<T>> partition(final int parts) {
		return new AbstractRichSequence<Seq<T>>(){
			public <U> U fold(
					F2<? super U, ? super Seq<T>, U> func,
					U start) {
				int i = 0;
				AbstractRandomAccessSequence<T> outerThis = AbstractRandomAccessSequence.this;
				int len = outerThis.length();
				int partSize = Math.max(1,(int)Math.ceil((double)len / parts));
				while (i < len){
					start = func.apply(start, outerThis.sublist(i,Math.min(len - i,partSize)));
					i+=partSize;
				}
				return start;
			}
		};
	}
	@Override
	public <V> RASeq<V> map(final F1<? super T, V> func) {
		return new AbstractRandomAccessSequence<V>(){
			@Override
			public int length() {
				return AbstractRandomAccessSequence.this.length();
			}
			public V get(int index) {
				return func.apply(AbstractRandomAccessSequence.this.get(index));
			}
		};
	}
	@Override
	public T first() {
		return get(0);
	}
}
