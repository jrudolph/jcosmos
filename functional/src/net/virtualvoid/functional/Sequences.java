package net.virtualvoid.functional;

import static java.text.MessageFormat.format;
import net.virtualvoid.functional.mutable.Array;
import net.virtualvoid.jcosmos.annotation.Export;
import net.virtualvoid.jcosmos.functional.v0.F1;
import net.virtualvoid.jcosmos.functional.v0.F2;
import net.virtualvoid.jcosmos.functional.v0.Foldable;
import net.virtualvoid.jcosmos.functional.v0.Predicate;
import net.virtualvoid.jcosmos.functional.v0.RASeq;
import net.virtualvoid.jcosmos.functional.v0.RandomAccessible;
import net.virtualvoid.jcosmos.functional.v0.Seq;
import net.virtualvoid.jcosmos.functional.v0.Seqs;

@Export
public class Sequences implements Seqs{
	public static <T> Seq<T> unfold(final T outerStart,final F1<? super T,T> succ,final Predicate<? super T> stopCondition){
		return new AbstractRichSequence<T>(){
			public <U> U fold(F2<? super U, ? super T, U> func, U start) {
				T value = outerStart;
				while(!stopCondition.predicate(value)){
					start = func.apply(start, value);
					value = succ.apply(value);
				}
				return start;
			}
		};
	}
	public static RASeq<Integer> range(final Integer from, final int toExclusive){
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
				return format("[{0,number}..{1,number})",from,toExclusive);
			}
		};
	}
	public final static <T>Seq<T> getEmptySequence(){
		return new AbstractRichSequence<T>(){
			public <U> U fold(F2<? super U, ? super T, U> func, U start) {
				return start;
			}
			@Override
			public <U> Seq<U> map(F1<? super T, U> func) {
				return getEmptySequence();
			}
			@Override
			public Seq<T> select(Predicate<? super T> predicate) {
				return this;
			}
			@Override
			public String toString() {
				return "[]";
			}
		};
	}
	public static <T> Seq<T> singleton(final T element){
		return new AbstractRichSequence<T>(){
			public <U> U fold(F2<? super U, ? super T, U> func, U start) {
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
	public static <T> Seq<T> fromIterable(final Iterable<T> it){
		return new AbstractRichSequence<T>(){
			public <U> U fold(F2<? super U, ? super T, U> func, U start) {
				for(T ele:it)
					start = func.apply(start, ele);
				return start;
			}
		};
	}
	public static <T> Seq<T> fromFoldable(final Foldable<T> foldable){
		return new AbstractRichSequence<T>(){
			public <U> U fold(F2<? super U, ? super T, U> func, U start) {
				return foldable.fold(func, start);
			}
		};
	}
	public static <T> RASeq<T> fromRandomAccessable(final RandomAccessible<T> accessable){
		return new AbstractRandomAccessSequence<T>(){
			public T get(int index) {
				return accessable.get(index);
			}
			@Override
			public int length() {
				return accessable.length();
			}
		};
	}

	@SuppressWarnings("unchecked")
	public static F2 joinSeqs =
		new F2<Seq<Object>,Seq<Object>,Seq<Object>>(){
		public Seq<Object> apply(Seq<Object> arg1, Seq<Object> arg2) {
			return arg1.join(arg2);
		}
	};
	public <T> Seq<T> array(T... els) {
		return Array.instance(els);
	}
	public <T> Seq<T> emptySequence() {
		return Sequences.getEmptySequence();
	}
	@SuppressWarnings("unchecked")
	public <T> F2<Seq<T>, Seq<T>, Seq<T>> join() {
		return joinSeqs;
	}
	public <T> RASeq<T> raseq(RandomAccessible<T> accessible) {
		return fromRandomAccessable(accessible);
	}
	public <T> Seq<T> seq(Foldable<T> foldable) {
		return fromFoldable(foldable);
	}
}
