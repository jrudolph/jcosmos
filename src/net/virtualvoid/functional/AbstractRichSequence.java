package net.virtualvoid.functional;

import static java.text.MessageFormat.format;
import static net.virtualvoid.functional.Types.tuple;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.virtualvoid.functional.Functions.Function1;
import net.virtualvoid.functional.Functions.Function2;
import net.virtualvoid.functional.Tuples.Tuple2;

public abstract class AbstractRichSequence<T> implements ISequence<T>{
	@SuppressWarnings("unchecked")
	public T[] asArray() {
		T[] res = (T[])new Object[length()];
		return withIndex().fold(new Function2<T[],Tuple2<Integer,T>,T[]>(){
			public T[] apply(T[] array, Tuple2<Integer, T> arg2) {
				int index = arg2.ele1();

				assert array.length > index :
					format("Supplied array too short: length = {0,number}, probably {1}.length() is incorrectly implemented",array.length,AbstractRichSequence.this.getClass().getSimpleName());

				array[index] = arg2.ele2();
				return array;
			}
		},res);
	}
	public int length() {
		return fold(Integers.succ.withIgnoredArg(),0);
	}
	public <V> ISequence<V> map(final Function1<? super T, V> func) {
		return new AbstractRichSequence<V>(){
			public <U> U fold(final Function2<? super U, ? super V, U> foldFunc, U start) {
				return AbstractRichSequence.this.fold(new Function2<U,T,U>(){
					public U apply(U start, T arg2) {
						return foldFunc.apply(start,func.apply(arg2));
					}
				},start);
			}
		};
	}
	public ISequence<T> select(final Function1<? super T, Boolean> predicate) {
		return new AbstractRichSequence<T>(){
			public <U> U fold(final Function2<? super U, ? super T, U> func, U start) {
				return AbstractRichSequence.this.fold(new Function2<U,T,U>(){
					public U apply(U start, T arg2) {
						if (predicate.apply(arg2))
							return func.apply(start, arg2);
						else
							return start;
					}
				}, start);
			}
		};
	}
	public ISequence<Tuple2<Integer, T>> withIndex() {
		return new AbstractRichSequence<Tuple2<Integer,T>>(){
			public <U> U fold(
					final Function2<? super U, ? super Tuple2<Integer, T>, U> func,
					U start) {
				return AbstractRichSequence.this.fold(new Function2<Tuple2<Integer,U>,T,Tuple2<Integer,U>>(){
					public Tuple2<Integer,U> apply(Tuple2<Integer,U> arg1, T arg2) {
						Integer index = arg1.ele1();
						return tuple(index + 1,func.apply(arg1.ele2(), tuple(index,arg2)));
					}
				},tuple(0,start)).ele2();
			}
		};
	}
	public ISequence<? extends ISequence<T>> partition(int parts){
		return Sequences.singleton(this);
	}
	private final static int THREADS = 2;
	private final static ExecutorService executor = Executors.newFixedThreadPool(THREADS);
	public T reduce(final Function2<? super T, ? super T, T> func, final T start) {
		//return fold(func,start);
		Object[] tasks = partition(THREADS).map(new Function1<ISequence<T>,Callable<T>>(){
			public Callable<T> apply(final ISequence<T> arg1) {
				return new Callable<T>(){
					public T call() throws Exception {
						T res = arg1.fold(func, start);
						return res;
					}
				};
			}
		}).asArray();
		try {
			return Sequences.fromIterable(
					executor.invokeAll((Collection<Callable<T>>)(Collection)Arrays.asList(tasks)))
					.fold(new Function2<T,Future<T>,T>(){
						public T apply(T arg1, Future<T> arg2) {
							try {
								return func.apply(arg1, arg2.get());
							} catch (Exception e) {
								throw new Error(e);
							}
						}
					}, start);
		} catch (InterruptedException e) {
			throw new Error(e);
		}
	}
}
