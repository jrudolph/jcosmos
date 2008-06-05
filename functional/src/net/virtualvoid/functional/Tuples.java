package net.virtualvoid.functional;

import net.virtualvoid.functional.Functions.IRichFunction1;
import net.virtualvoid.functional.Functions.RichFunction1;
import net.virtualvoid.jcosmos.functional.v0.Tuple2;

public class Tuples {
	public static class Tuple2Impl<T1,T2> implements Tuple2<T1,T2>{
		private final T1 ele1;
		private final T2 ele2;
		public Tuple2Impl(T1 ele1,T2 ele2){
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
}
