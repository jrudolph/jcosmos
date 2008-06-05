package net.virtualvoid.functional;

import net.virtualvoid.functional.Either.First;
import net.virtualvoid.functional.Either.Second;
import net.virtualvoid.functional.Tuples.Tuple2Impl;
import net.virtualvoid.jcosmos.functional.v0.Tuple2;

public class Types {
	public static <T> First<T> eitherThis(final T ele){
		return new First<T>(){
			public <ResT> ResT handle(
					Handler<? super T, ? super Object, ResT> handler) {
				return handler.handleThis(ele);
			}
		};
	}
	public static <U> Second<U> eitherThat(final U ele){
		return new Second<U>(){
			public <ResT> ResT handle(
					Handler<? super Object, ? super U, ResT> handler) {
				return handler.handleThat(ele);
			}
		};
	}
	public static <T,U> Tuple2<T,U> tuple(final T arg1,final U arg2){
		return new Tuple2Impl<T,U>(arg1,arg2);
	}
	public static <T,U> Tuple2<T,U> pair(final T arg1,final U arg2){
		return tuple(arg1,arg2);
	}
}
