package net.virtualvoid;

public abstract class LazyVal<T> {
	protected abstract T retrieve();
	private T val=null;
	boolean set=false;
	public T get(){
		if (!set){
			val=retrieve();
			set=true;
		}
		return val;
	}
}
