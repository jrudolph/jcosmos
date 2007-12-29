package net.virtualvoid.functional;

public class Functions {
	public static interface Function0<ResT> {
		ResT apply();
	}
	public static interface Function1<Arg1T,ResT> {
		ResT apply(Arg1T arg1);
	}
	public static interface Function2<Arg1T,Arg2T,ResT> {
		ResT apply(Arg1T arg1,Arg2T arg2);
	}
	public static interface Function3<Arg1T,Arg2T,Arg3T,ResT> {
		ResT apply(Arg1T arg1,Arg2T arg2,Arg3T arg3);
	}
	public static interface IRichFunction0<ResT> extends Function0<ResT>{
		<Arg1T>IRichFunction1<Arg1T,ResT> withIgnoredArg();
	}
	public static interface IRichFunction1<Arg1T,ResT> extends Function1<Arg1T,ResT>{
		<Arg2T>IRichFunction2<Arg1T,Arg2T,ResT> withIgnoredArg();
		<Res2T>IRichFunction1<Arg1T,Res2T> combineWith(Function1<? super ResT,Res2T> func);
	}
	public static interface IRichFunction2<Arg1T,Arg2T,ResT> extends Function2<Arg1T,Arg2T,ResT>{
		//RichFunction1 withIgnoredArg();
	}
	public static abstract class RichFunction0<ResT> implements IRichFunction0<ResT>{
		public <Arg1T> IRichFunction1<Arg1T, ResT> withIgnoredArg() {
			return new RichFunction1<Arg1T,ResT>(){
				public ResT apply(Arg1T arg1) {
					return RichFunction0.this.apply();
				}
			};
		}
	}
	public static abstract class RichFunction1<Arg1T,ResT> implements IRichFunction1<Arg1T,ResT>{
		public <Arg2T>IRichFunction2<Arg1T, Arg2T, ResT> withIgnoredArg() {
			return new IRichFunction2<Arg1T,Arg2T,ResT>(){
				public ResT apply(Arg1T arg1, Arg2T arg2) {
					return RichFunction1.this.apply(arg1);
				}
			};
		}
		public <Res2T> IRichFunction1<Arg1T, Res2T> combineWith(
				final Function1<? super ResT, Res2T> func) {
			return new RichFunction1<Arg1T,Res2T>(){
				public Res2T apply(Arg1T arg1) {
					return func.apply(RichFunction1.this.apply(arg1));
				}
			};
		}
	}
	public static <Arg1T,ResT> RichFunction1<Arg1T,ResT> rich(final Function1<Arg1T,ResT> func){
		return new RichFunction1<Arg1T, ResT>(){
			public ResT apply(Arg1T arg1) {
				return func.apply(arg1);
			}
		};
	}
}
