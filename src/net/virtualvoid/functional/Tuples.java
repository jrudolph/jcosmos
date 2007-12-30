package net.virtualvoid.functional;

import net.virtualvoid.functional.Functions.IRichFunction1;
import net.virtualvoid.functional.Functions.RichFunction1;

public class Tuples {
	public static interface Tuple1<T1>{
		T1 ele1();
	}
	public static class Tuple2<T1,T2>{
		private final T1 ele1;
		private final T2 ele2;
		public Tuple2(T1 ele1,T2 ele2){
			this.ele1=ele1;
			this.ele2=ele2;
		}
		public T1 ele1(){
			return ele1;
		}
		public T2 ele2(){
			return ele2;
		}
		public static <T1,T2> IRichFunction1<Tuple2<T1,T2>,T1> ele1F(){
			return new RichFunction1<Tuple2<T1,T2>,T1>(){
				public T1 apply(Tuple2<T1, T2> arg1) {
					return arg1.ele1();
				}
			};
		}
		public static <T1,T2> IRichFunction1<Tuple2<T1,T2>,T2> ele2F(){
			return new RichFunction1<Tuple2<T1,T2>,T2>(){
				public T2 apply(Tuple2<T1, T2> arg1) {
					return arg1.ele2();
				}
			};
		}
	}
	public static interface Tuple3<T1,T2,T3>{
		T1 ele1();
		T2 ele2();
		T3 ele3();
	}
	public static interface Tuple4<T1,T2,T3,T4>{
		T1 ele1();
		T2 ele2();
		T3 ele3();
		T4 ele4();
	}
}
