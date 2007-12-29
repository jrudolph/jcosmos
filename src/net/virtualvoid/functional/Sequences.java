package net.virtualvoid.functional;

import static java.text.MessageFormat.format;
import net.virtualvoid.functional.Functions.Function1;
import net.virtualvoid.functional.Functions.Function2;
import net.virtualvoid.functional.Predicates.Predicate;

public class Sequences {
	public static <T> ISequence<T> unfold(final T outerStart,final Function1<? super T,T> succ,final Predicate<? super T> stopCondition){
		return new AbstractRichSequence<T>(){
			public <U> U fold(Function2<? super U, ? super T, U> func, U start) {
				T value = outerStart;
				while(!stopCondition.predicate(value)){
					start = func.apply(start, value);
					value = succ.apply(value);
				}
				return start;
			}
			// because class can't get less specific than the start
			// element
			@SuppressWarnings("unchecked")
			public Class<? super T> getElementClass() {
				return (Class<T>)outerStart.getClass();
			}
		};
	}
	public static IRandomAccessSequence<Integer> range(final Integer from, final int toExclusive){
		return new AbstractRandomAccessSequence<Integer>(){
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
			public Class<Integer> getElementClass() {
				return Integer.class;
			}
		};
	}
	public final static <T>ISequence<T> emptySequence(){
		return new AbstractRichSequence<T>(){
			public <U> U fold(Function2<? super U, ? super T, U> func, U start) {
				return start;
			}
			@Override
			public <U> ISequence<U> map(Function1<? super T, U> func) {
				return emptySequence();
			}
			@Override
			public ISequence<T> select(Function1<? super T, Boolean> predicate) {
				return this;
			}
			@Override
			public String toString() {
				return "[]";
			}
			@SuppressWarnings("unchecked")
			public Class<? super T> getElementClass() {
				return Object.class;
			}
		};
	}
	public static <T> ISequence<T> singleton(final T element){
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
			// because can't get less specific than the one element
			@SuppressWarnings("unchecked")
			public Class<? super T> getElementClass() {
				return (Class<T>) element.getClass();
			}
		};
	}
	public static <T> ISequence<T> fromIterable(final Iterable<T> it){
		return new AbstractRichSequence<T>(){
			public <U> U fold(Function2<? super U, ? super T, U> func, U start) {
				for(T ele:it)
					start = func.apply(start, ele);
				return start;
			}
			public Class<? super T> getElementClass() {
				return Object.class;
			}
		};
	}
	public static <T> ISequence<T> fromFoldable(final IFoldable<T> foldable){
		return new AbstractRichSequence<T>(){
			public <U> U fold(Function2<? super U, ? super T, U> func, U start) {
				return foldable.fold(func, start);
			}
			public Class<? super T> getElementClass() {
				return foldable.getElementClass();
			}
		};
	}
	public static <T> IRandomAccessSequence<T> fromRandomAccessable(final IRandomAccessable<T> accessable){
		return new AbstractRandomAccessSequence<T>(){
			public T get(int index) {
				return accessable.get(index);
			}
			@Override
			public int length() {
				return accessable.length();
			}
			public Class<? super T> getElementClass() {
				return accessable.getElementClass();
			}
		};
	}
}
