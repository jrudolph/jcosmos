package net.virtualvoid.functional;

import java.lang.reflect.Method;

import net.virtualvoid.functional.Tuples.Tuple2;
import net.virtualvoid.functional.mutable.Array;

public class Functions {
	public static interface Function<ResT> {
		Class<ResT> getResultType();
	}
	public static interface Function0<ResT>
		extends Function<ResT> {
		ResT apply();
	}
	public static interface Function1<Arg1T,ResT>
		extends Function<ResT> {
		ResT apply(Arg1T arg1);
	}
	public static interface Function2<Arg1T,Arg2T,ResT>
		extends Function<ResT> {
		ResT apply(Arg1T arg1,Arg2T arg2);
	}
	public static interface Function3<Arg1T,Arg2T,Arg3T,ResT>
		extends Function<ResT> {
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
		IRichFunction1<Arg2T,ResT> applyPartial(Arg1T arg1);
	}
	public static abstract class AbstractFunction<ResT> implements Function<ResT>{
		protected Class<ResT> resultType;
		protected AbstractFunction(Class<ResT> resultType) {
			this.resultType = resultType;
		}
		public AbstractFunction(TypeRef<ResT> typeRef) {
			this(typeRef.clazz());
		}
		public AbstractFunction() {
			this.resultType = null;
		}
		private final static IRichFunction1<Method,String> getNameF =
			new RichFunction1<Method, String>(String.class){
				public String apply(Method arg1) {
					return arg1.getName();
				}
			};
		@SuppressWarnings("unchecked")
		public Class<ResT> getResultType() {
			if (resultType == null){
				ISequence<Method> applyMs =
					Array.instance(getClass().getMethods())
						.select(getNameF.combineWith(Predicates.equalsP("apply")));
				resultType = (Class<ResT>) applyMs.first().getReturnType();
			}
			return resultType;
		}
	}
	public static abstract class RichFunction0<ResT>
		extends AbstractFunction<ResT>
		implements IRichFunction0<ResT>{
		protected RichFunction0(Class<ResT> resultType) {
			super(resultType);
		}
		public RichFunction0() {
			super();
		}
		public RichFunction0(TypeRef<ResT> typeRef) {
			super(typeRef);
		}
		public <Arg1T> IRichFunction1<Arg1T, ResT> withIgnoredArg() {
			return new RichFunction1<Arg1T,ResT>(resultType){
				public ResT apply(Arg1T arg1) {
					return RichFunction0.this.apply();
				}
			};
		}
	}
	public static abstract class RichFunction1<Arg1T,ResT>
		extends AbstractFunction<ResT>
		implements IRichFunction1<Arg1T,ResT>{
		protected RichFunction1(Class<ResT> resultType) {
			super(resultType);
		}
		public RichFunction1() {
			super();
		}
		public RichFunction1(TypeRef<ResT> typeRef) {
			super(typeRef);
		}
		public <Arg2T>IRichFunction2<Arg1T, Arg2T, ResT> withIgnoredArg() {
			return new RichFunction2<Arg1T,Arg2T,ResT>(resultType){
				public ResT apply(Arg1T arg1, Arg2T arg2) {
					return RichFunction1.this.apply(arg1);
				}
			};
		}
		public <Res2T> IRichFunction1<Arg1T, Res2T> combineWith(
				final Function1<? super ResT, Res2T> func) {
			return new RichFunction1<Arg1T,Res2T>(func.getResultType()){
				public Res2T apply(Arg1T arg1) {
					return func.apply(RichFunction1.this.apply(arg1));
				}
			};
		}
	}
	public static abstract class RichFunction2<Arg1T,Arg2T,ResT>
		extends AbstractFunction<ResT>
		implements IRichFunction2<Arg1T,Arg2T,ResT>{
		protected RichFunction2(Class<ResT> resultType) {
			super(resultType);
		}
		public RichFunction2() {
			super();
		}
		public RichFunction2(TypeRef<ResT> typeRef) {
			super(typeRef);
		}
		public IRichFunction1<Arg2T, ResT> applyPartial(final Arg1T arg1) {
			return new RichFunction1<Arg2T,ResT>(resultType){
				public ResT apply(Arg2T arg2) {
					return RichFunction2.this.apply(arg1,arg2);
				}
			};
		}
	}
	public static <ArgT,ResT1,ResT2> IRichFunction1<ArgT,Tuple2<ResT1,ResT2>> multiFunc(
			final Function1<? super ArgT,ResT1> func1,final Function1<? super ArgT,ResT2> func2){
		return new RichFunction1<ArgT,Tuple2<ResT1,ResT2>>(new TypeRef<Tuple2<ResT1,ResT2>>(){}.clazz()){
			public Tuple2<ResT1, ResT2> apply(ArgT arg1) {
				return Types.tuple(func1.apply(arg1),func2.apply(arg1));
			}
		};
	}
	public static <Arg1T,Arg2T,ResT1,ResT2> IRichFunction2<Arg1T,Arg2T,Tuple2<ResT1,ResT2>> tupleFunc(
			final Function1<? super Arg1T,ResT1> func1,final Function1<? super Arg2T,ResT2> func2){
		return new RichFunction2<Arg1T,Arg2T,Tuple2<ResT1,ResT2>>(new TypeRef<Tuple2<ResT1,ResT2>>(){}.clazz()){
			public Tuple2<ResT1, ResT2> apply(Arg1T arg1,Arg2T arg2) {
				return Types.tuple(func1.apply(arg1),func2.apply(arg2));
			}
		};
	}
	private static final IRichFunction1<?,?> identity =
		new RichFunction1<Object,Object>(Object.class){
			public Object apply(Object arg1) {
				return arg1;
			}
		};
	@SuppressWarnings("unchecked") // we know it works
	public static <T> IRichFunction1<T,T> identity(){
		return (IRichFunction1<T, T>) identity;
	}
	public static <Arg1T,ResT> RichFunction1<Arg1T,ResT> rich(final Function1<Arg1T,ResT> func){
		return new RichFunction1<Arg1T, ResT>(func.getResultType()){
			public ResT apply(Arg1T arg1) {
				return func.apply(arg1);
			}
		};
	}
	public static abstract class IntFunction0
	implements Function0<Integer>{
		protected abstract int applyImpl();
		public final Integer apply() {
			return applyImpl();
		}
	}
	public static abstract class IntFunction1
	implements Function1<Integer,Integer>{
		protected abstract int apply(int arg1);
		public Integer apply(Integer arg1) {
			return apply((int)arg1);
		}
	}
	public static abstract class IntFunction2
	implements Function2<Integer,Integer,Integer>{
		protected abstract int apply(int arg1,int arg2);
		public Integer apply(Integer arg1, Integer arg2) {
			return apply((int)arg1,(int)arg2);
		}
	}
	@SuppressWarnings("unchecked")
	public final static <T> Function2<T,T,T> firstNotNullValue(){
		return new RichFunction2<T, T, T>((Class<T>)Object.class){
			public T apply(T arg1, T arg2) {
				return arg1 == null ? arg2 : arg1;
			}
		};
	}
}
