package net.virtualvoid.functional;

import static java.text.MessageFormat.format;
import net.virtualvoid.functional.Functions.Function1;
import net.virtualvoid.functional.Functions.Function2;

public abstract class AbstractRichRandomAccessSequence<T>
	extends AbstractRichSequence<T>
	implements IRichRandomAccessSequence<T>{

	public IRichRandomAccessSequence<T> sublist(final int from, final int length) {
		assert from >= 0;
		assert length > 0;
		assert from + length <= length();

		return new AbstractRichRandomAccessSequence<T>(){
			public T get(int index) {
				return AbstractRichRandomAccessSequence.this.get(from + index);
			}
			@Override
			public int length() {
				return length;
			}
			@Override
			public String toString() {
				return format("Sublist[{0}..{1}) of {2}",from,from+length,AbstractRichRandomAccessSequence.this.toString());
			}
		};
	}
	public <U> U fold(Function2<? super U, ? super T, U> func, U start) {
		for(int i=0;i<length();i++)
			start = func.apply(start, get(i));
		return start;
	}
	@Override
	public abstract int length();

	@Override
	public IRichSequence<? extends IRichSequence<T>> partition(final int parts) {
		return new AbstractRichSequence<IRichSequence<T>>(){
			public <U> U fold(
					Function2<? super U, ? super IRichSequence<T>, U> func,
					U start) {
				int i = 0;
				AbstractRichRandomAccessSequence<T> outerThis = AbstractRichRandomAccessSequence.this;
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
	public <V> IRichRandomAccessSequence<V> map(final Function1<? super T, V> func) {
		return new AbstractRichRandomAccessSequence<V>(){
			@Override
			public int length() {
				return AbstractRichRandomAccessSequence.this.length();
			}
			public V get(int index) {
				return func.apply(AbstractRichRandomAccessSequence.this.get(index));
			};
		};
	}
}
