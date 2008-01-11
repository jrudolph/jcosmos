package net.virtualvoid.functional;

import static java.text.MessageFormat.format;
import static net.virtualvoid.functional.Types.tuple;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.virtualvoid.functional.Functions.Function1;
import net.virtualvoid.functional.Functions.Function2;
import net.virtualvoid.functional.Tuples.Tuple2;
import net.virtualvoid.functional.mutable.Array;

public abstract class AbstractRichSequence<T> implements ISequence<T>{
	@SuppressWarnings("unchecked")
	public T[] asNativeArray(Class<T> elementClass) {
		T[] res=Array.newNative(elementClass, length());
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
	@SuppressWarnings("unchecked")
	public Array<T> asArray() {
		return Array.instance(asNativeArray((Class)Object.class));
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
			public Class<? super V> getElementClass() {
				//func.getClass().getMethod("apply", AbstractRichSequence.this.getElementClass())
				return Object.class; // can't be more specific since Function1 has no metadata
			}
		};
	}
	static class FoundException extends Error{
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		Object value;
		public FoundException(Object value) {
			this.value = value;
		}
	};
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
			public Class<? super T> getElementClass() {
				return AbstractRichSequence.this.getElementClass();
			}
			@SuppressWarnings("unchecked")
			@Override
			public T first() {
				try{
					return fold(new Function2<T,T,T>(){
						public T apply(T arg1, T arg2) {
							if (arg2 != null)
								throw new FoundException(arg2);
							else
								return null;
						}
					}, null);
				}
				catch(FoundException fe){
					return (T)fe.value;
				}
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
			public Class<? super Tuple2<Integer, T>> getElementClass() {
				return Tuple2.class;
			}
		};
	}
	public ISequence<? extends ISequence<T>> partition(int parts){
		return Sequences.singleton(this);
	}
	@SuppressWarnings("unchecked")
	public T reduce(final Function2<? super T, ? super T, T> func){
		final Object starter = new Object();
		return fold(new Function2<T,T,T>(){
			public T apply(T arg1, T arg2) {
				if (arg1 == starter)
					return arg2;
				else
					return func.apply(arg1, arg2);
			}
		},(T)starter);
	}

	private final static int THREADS = Runtime.getRuntime().availableProcessors();
	private final static ExecutorService executor = Executors.newFixedThreadPool(THREADS);
	private final static <T> Function1<Future<T>,T> futureResultF(){
		return new Function1<Future<T>,T>(){
			public T apply(Future<T> arg1) {
				try {
					return arg1.get();
				} catch (Exception e) {
					assert false;
					throw new Error("This should be handled better?");
				}
			}
		};
	}

	public T reduceThreaded(final Function2<? super T, ? super T, T> func) {
		Callable<T>[] tasks = partition(THREADS).map(new Function1<ISequence<T>,Callable<T>>(){
			public Callable<T> apply(final ISequence<T> arg1) {
				return new Callable<T>(){
					public T call() throws Exception {
						return arg1.reduce(func);
					}
				};
			}
		}).asNativeArray(new TypeRef<Callable<T>>(){}.clazz());
		try {
			return
				Sequences.fromIterable(
						executor.invokeAll(Arrays.asList(tasks)))
					.map(AbstractRichSequence.<T>futureResultF())
					.reduce(func);
		} catch (InterruptedException e) {
			throw new Error(e);
		}
	}

	@SuppressWarnings("unchecked")
	public Array<T> sort(Comparator<T> comparator) {
		Object[] oa = asNativeArray((Class)Object.class);
		Arrays.sort((T[])oa,comparator);
		return new Array<T>((T[])oa);
	}
	public T first() {
		return fold(Functions.<T>firstNotNullValue(),null);
	}
	public ISequence<T> join(final ISequence<? extends T> other) {
		return new AbstractRichSequence<T>(){
			public <U> U fold(Function2<? super U, ? super T, U> func, U start) {
				return other.fold(func, AbstractRichSequence.this.fold(func,start));
			}
			public Class<? super T> getElementClass() {
				return AbstractRichSequence.this.getElementClass();
			}
		};
	}
	public ISequence<T> prepend(final T first) {
		return new AbstractRichSequence<T>(){
			public <U> U fold(Function2<? super U, ? super T, U> func, U start) {
				return AbstractRichSequence.this.fold(func, func.apply(start,first));
			}
			public Class<? super T> getElementClass() {
				return AbstractRichSequence.this.getElementClass();
			}
		};
	}
	public ISequence<T> append(final T last) {
		return new AbstractRichSequence<T>(){
			public <U> U fold(Function2<? super U, ? super T, U> func, U start) {
				return func.apply(AbstractRichSequence.this.fold(func,start),last);
			}
			public Class<? super T> getElementClass() {
				return AbstractRichSequence.this.getElementClass();
			}
		};
	}
}
