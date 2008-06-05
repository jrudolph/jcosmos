package net.virtualvoid.functional;

public interface Either<T,U> {
	interface First<T> extends Either<T,Object>{

	}
	interface Second<T> extends Either<Object,T>{

	}
	public static interface Handler<T,U,ResT>{
		ResT handleThis(T arg);
		ResT handleThat(U arg);
	}
	<ResT> ResT handle(Handler<? super T,? super U,ResT> handler);
}
