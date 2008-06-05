package net.virtualvoid.functional;

import net.virtualvoid.functional.Tuples.Tuple2;

public class Functions {
	public static interface IRichFunction0<ResT> extends F0<ResT>{
		<Arg1T>IRichFunction1<Arg1T,ResT> withIgnoredArg();
	}
	public static interface IRichFunction1<Arg1T,ResT> extends F1<Arg1T,ResT>{
		<Arg2T>IRichFunction2<Arg1T,Arg2T,ResT> withIgnoredArg();
		<Res2T>IRichFunction1<Arg1T,Res2T> combineWith(F1<? super ResT,Res2T> func);
	}
	public static interface IRichFunction2<Arg1T,Arg2T,ResT> extends F2<Arg1T,Arg2T,ResT>{
		IRichFunction1<Arg2T,ResT> applyPartial(Arg1T arg1);
	}

	public static abstract class RichFunction0<ResT>
		implements IRichFunction0<ResT>{
		public RichFunction0() {
			super();
		}
		public <Arg1T> IRichFunction1<Arg1T, ResT> withIgnoredArg() {
			return new RichFunction1<Arg1T,ResT>(){
				public ResT apply(Arg1T arg1) {
					return RichFunction0.this.apply();
				}
			};
		}
	}
	public static abstract class RichFunction1<Arg1T,ResT>
		implements IRichFunction1<Arg1T,ResT>{
		public RichFunction1() {
			super();
		}
		public <Arg2T>IRichFunction2<Arg1T, Arg2T, ResT> withIgnoredArg() {
			return new RichFunction2<Arg1T,Arg2T,ResT>(){
				public ResT apply(Arg1T arg1, Arg2T arg2) {
					return RichFunction1.this.apply(arg1);
				}
			};
		}
		public <Res2T> IRichFunction1<Arg1T, Res2T> combineWith(
				final F1<? super ResT, Res2T> func) {
			return new RichFunction1<Arg1T,Res2T>(){
				public Res2T apply(Arg1T arg1) {
					return func.apply(RichFunction1.this.apply(arg1));
				}
			};
		}
	}
	public static abstract class RichFunction2<Arg1T,Arg2T,ResT>
		implements IRichFunction2<Arg1T,Arg2T,ResT>{
		public RichFunction2() {
			super();
		}
		public IRichFunction1<Arg2T, ResT> applyPartial(final Arg1T arg1) {
			return new RichFunction1<Arg2T,ResT>(){
				public ResT apply(Arg2T arg2) {
					return RichFunction2.this.apply(arg1,arg2);
				}
			};
		}
	}
	public static <ArgT,ResT1,ResT2> IRichFunction1<ArgT,Tuple2<ResT1,ResT2>> multiFunc(
			final F1<? super ArgT,ResT1> func1,final F1<? super ArgT,ResT2> func2){
		return new RichFunction1<ArgT,Tuple2<ResT1,ResT2>>(){
			public Tuple2<ResT1, ResT2> apply(ArgT arg1) {
				return Types.tuple(func1.apply(arg1),func2.apply(arg1));
			}
		};
	}
	public static <Arg1T,Arg2T,ResT1,ResT2> IRichFunction2<Arg1T,Arg2T,Tuple2<ResT1,ResT2>> tupleFunc(
			final F1<? super Arg1T,ResT1> func1,final F1<? super Arg2T,ResT2> func2){
		return new RichFunction2<Arg1T,Arg2T,Tuple2<ResT1,ResT2>>(){
			public Tuple2<ResT1, ResT2> apply(Arg1T arg1,Arg2T arg2) {
				return Types.tuple(func1.apply(arg1),func2.apply(arg2));
			}
		};
	}
	private static final IRichFunction1<?,?> identity =
		new RichFunction1<Object,Object>(){
			public Object apply(Object arg1) {
				return arg1;
			}
		};
	@SuppressWarnings("unchecked") // we know it works
	public static <T> IRichFunction1<T,T> identity(){
		return (IRichFunction1<T, T>) identity;
	}
	public static <Arg1T,ResT> RichFunction1<Arg1T,ResT> rich(final F1<Arg1T,ResT> func){
		return new RichFunction1<Arg1T, ResT>(){
			public ResT apply(Arg1T arg1) {
				return func.apply(arg1);
			}
		};
	}
	public static abstract class IntFunction0
	implements F0<Integer>{
		protected abstract int applyImpl();
		public final Integer apply() {
			return applyImpl();
		}
	}
	public static abstract class IntFunction1
	implements F1<Integer,Integer>{
		protected abstract int apply(int arg1);
		public Integer apply(Integer arg1) {
			return apply((int)arg1);
		}
	}
	public static abstract class IntFunction2
	implements F2<Integer,Integer,Integer>{
		protected abstract int apply(int arg1,int arg2);
		public Integer apply(Integer arg1, Integer arg2) {
			return apply((int)arg1,(int)arg2);
		}
	}
	@SuppressWarnings("unchecked")
	public final static <T> F2<T,T,T> firstNotNullValue(){
		return new RichFunction2<T, T, T>(){
			public T apply(T arg1, T arg2) {
				return arg1 == null ? arg2 : arg1;
			}
		};
	}
}
