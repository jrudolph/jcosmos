package net.virtualvoid.functional;

import static java.text.MessageFormat.format;
import net.virtualvoid.functional.Functions.Function1;
import net.virtualvoid.functional.Functions.Function2;
import net.virtualvoid.functional.Predicates.Predicate;

public class Sequences {
	public static <T> IRichSequence<T> unfold(final T outerStart,final Function1<? super T,T> succ,final Predicate<? super T> stopCondition){
		return new AbstractRichSequence<T>(){
			public <U> U fold(Function2<? super U, ? super T, U> func, U start) {
				T value = outerStart;
				while(!stopCondition.predicate(value)){
					start = func.apply(start, value);
					value = succ.apply(value);
				}
				return start;
			}
		};
	}
	public static IRichRandomAccessSequence<Integer> range(final Integer from, final int toExclusive){
		return new AbstractRichRandomAccessSequence<Integer>(){
			public Integer get(int index) {
				return from + index;
			}
			@Override
			public int length() {
				return toExclusive - from;
			}
			@Override
			public String toString() {
				return format("({0,number};{1,number}]",from,from+toExclusive);
			}
		};
	}
	public final static <T>IRichSequence<T> emptySequence(){
		return new AbstractRichSequence<T>(){
			public <U> U fold(Function2<? super U, ? super T, U> func, U start) {
				return start;
			}
			@Override
			public <U> IRichSequence<U> map(Function1<? super T, U> func) {
				return emptySequence();
			}
			@Override
			public IRichSequence<T> select(Function1<? super T, Boolean> predicate) {
				return this;
			}
			@Override
			public String toString() {
				return "[]";
			}
		};
	}
	public final static <T> IRichSequence<T> singleton(final T element){
		return new AbstractRichSequence<T>(){
			public <U> U fold(Function2<? super U, ? super T, U> func, U start) {
				return func.apply(start, element);
			}
			@Override
			public int length() {
				return 1;
			}
			@Override
			public String toString() {
				return format("[{0}]",element.toString());
			}
		};
	}
	public final static <T> IRichSequence<T> fromIterable(final Iterable<T> it){
		return new AbstractRichSequence<T>(){
			public <U> U fold(Function2<? super U, ? super T, U> func, U start) {
				for(T ele:it)
					start = func.apply(start, ele);
				return start;
			}
		};
	}
}
