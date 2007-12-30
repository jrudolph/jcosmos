package net.virtualvoid.functional;

import static java.text.MessageFormat.format;
import net.virtualvoid.functional.Benchmark.BenchFunc;
import net.virtualvoid.functional.Functions.Function0;
import net.virtualvoid.functional.Functions.Function1;

import org.testng.annotations.Test;

public class BoxingBenchmarkTest {
	public static abstract class Blub{
		public abstract int[]blub();
	}
	static int[] ints2(int number){
		int[] res = new int[number];
		for (int i = 0;i<number;i++)
			res[i] = i + 1;
		return res;
	}
	static interface Predicate{
		boolean pred(int i);
	}
	public static void select(int []is,Predicate p){
		for(int i=is.length-1;i>=0;i--)
			if(p.pred(is[i]))
				is[i] = -1;
	}
	public static void selectFinal(int []is,final Predicate p){
		for(int i=is.length-1;i>=0;i--)
			if(p.pred(is[i]))
				is[i] = -1;
	}
	static class KnownPredicate implements Predicate{
		public boolean pred(int i) {
			return i % 2 == 0;
		}
	}
	public static void select(int []is,KnownPredicate p){
		for(int i=is.length-1;i>=0;i--)
			if(p.pred(is[i]))
				is[i] = -1;
	}
	static class KnownPredicate2 implements Predicate{
		public final boolean pred(int i) {
			return i % 2 == 0;
		}
	}
	public static void select(int []is,KnownPredicate2 p){
		for(int i=is.length-1;i>=0;i--)
			if(p.pred(is[i]))
				is[i] = -1;
	}
	static interface GenPredicate<T>{
		boolean pred(T arg);
	}
	public static void select(int []is,GenPredicate<Integer> p){
		for(int i=is.length-1;i>=0;i--)
			if(p.pred(new Integer(is[i])))
				is[i] = -1;
	}
	public static void selectWithAllocator(int []is,GenPredicate<Number> p){
		for(int i=is.length-1;i>=0;i--)
			if(p.pred(IntAllocator.alloc(is[i])))
				is[i] = -1;
	}
	static interface IntFunc<T>{
		T pred(int arg);
	}
	public static void select(int []is,IntFunc<Boolean> p){
		for(int i=is.length-1;i>=0;i--)
			if(p.pred(is[i]))
				is[i] = -1;
	}
	public static void select(int []is,Function1<Integer,Boolean> p){
		for(int i=is.length-1;i>=0;i--)
			if(p.apply(is[i]))
				is[i] = -1;
	}
	public static void selectWithAlloc(int []is,Function1<? super Number,Boolean> p){
		for(int i=is.length-1;i>=0;i--)
			if(p.apply(IntAllocator.alloc(is[i]))==Boolean.TRUE)
				is[i] = -1;
	}
	static class IntAllocator{
		/*static ThreadLocal<IntAllocator> alloc = new ThreadLocal<IntAllocator>(){
			@Override
			protected IntAllocator initialValue() {
				return null;
			}
		};*/
		static IntAllocator thisAlloc;

		private final static int SIZE = 1500;

		MyInteger []head = new MyInteger[SIZE];
		MyInteger []tail = new MyInteger[SIZE];

		int hindex = 0;
		int tindex = 0;

		MyInteger []alloced = new MyInteger[SIZE];
		int allocIndex;

		public <ResT>ResT withIntAllocator(Function0<ResT> func){
			//assert alloc.get() == null;
			assert thisAlloc ==null;

			System.arraycopy(alloced, 0, head, 0, hindex);
			hindex = 0;

			//alloc.set(this);
			thisAlloc=this;
			ResT res = func.apply();
			//alloc.set(null);
			thisAlloc=null;
			return res;
		}
		public <ResT> Function0<ResT> withIntAllocatorFunction(final Function0<ResT> func){
			return new Function0<ResT>(){
				public ResT apply() {
					return withIntAllocator(func);
				}
				@Override
				public String toString() {
					return format("IntAllocating {0}",func.toString());
				}
			};
		}
		public static MyInteger alloc(int v){
			IntAllocator a = thisAlloc;//.get();
			assert a != null;
			MyInteger res;
			if (a.hindex < SIZE && (res = a.head[a.hindex++])!= null){
				res.val = v;
				return res;
			}
			else {
				res = a.tail[0];
				if (res == null){
					res = new MyInteger();
					res.val = v;
					if (a.allocIndex<SIZE)
						a.alloced[a.allocIndex++] = res;
					return res;
				}
				else {
					MyInteger[]ar = a.head;
					a.head = a.tail;
					a.tail = ar;
					a.hindex = 1;
					a.tindex = 0;
					return res;
				}
			}
		}
		public static void delete(MyInteger n){
			//IntAllocator a = alloc.get();
			IntAllocator a = thisAlloc;
			assert a != null;
			a.tail[a.tindex++]=n;
		}
	}
	@Test
	public void benchmarkPredicatesRaw(){
		final int []is = ints2(1000);
		IntAllocator alloc = new IntAllocator();
		final Function1<Number,Boolean> func2 = new Function1<Number,Boolean>(){
			public Boolean apply(Number arg) {
				return arg.intValue() % 2 == 0 ?Boolean.TRUE:Boolean.FALSE;
			}
		};
		final IntFunc<Boolean> intF = new IntFunc<Boolean>(){
			public Boolean pred(int arg) {
				return arg % 2 == 0;
			}
		};
		final IntFunc<Boolean> intF2 = new IntFunc<Boolean>(){
			public Boolean pred(int arg) {
				return arg % 2 == 0 ? Boolean.TRUE : Boolean.FALSE;
			}
		};
		final Predicate even = new Predicate(){
			public boolean pred(int i) {
				return i % 2 == 0;
			}
		};
		final KnownPredicate kp = new KnownPredicate();
		final KnownPredicate2 kp2 = new KnownPredicate2();
		final GenPredicate<Integer> gp = new GenPredicate<Integer>(){
			public boolean pred(Integer arg) {
				return arg % 2 ==0;
			}
		};
		final GenPredicate<Number> gp2 = new GenPredicate<Number>(){
			public boolean pred(Number arg) {
				return arg.intValue() % 2 ==0;
			}
		};

		final Function1<Integer,Boolean> func = new Function1<Integer,Boolean>(){
			public Boolean apply(Integer arg) {
				return arg % 2 == 0;
			}
		};

		Benchmark.shootout(100000,new BenchFunc("raw w/ backward for loop"){
			public Object apply() {
				int []a = is;
				int len = a.length-1;
				for (;len>= 0;len--)
					if (a[len] % 2 == 0)
						a[len] = -1;
				return null;
			}
		}
		,new Runner(){
			@Override
			public int[] is() {
				return is;
			}
			@Override
			public String toString() {
				return "handcrafted jvm bytecodes";
			}
		}
		,alloc.withIntAllocatorFunction(new BenchFunc("Integer.valueOf of values 1 to 1000"){
			public Object apply() {

				for (int i=0;i<1000;i++)
					/*IntAllocator.delete(*/IntAllocator.alloc(i);//);
				return null;
			}
		})
		,new BenchFunc("raw w/ while loop"){
			public Object apply() {
				int []a = is;
				int len = a.length-1;
				while(len>= 0){
					if (a[len] % 2 == 0)
						a[len] = -1;
					len--;
				}
				return null;
			}
		}
		,new BenchFunc("raw w/ forward for loop"){
			public Object apply() {
				int []a = is;
				int len = a.length;
				for (int i=0;i < len;i++)
					if (a[i] % 2 == 0)
						a[i] = -1;
				return null;
			}
		}
		,new BenchFunc("raw w/ enhanced for loop"){
			public Object apply() {
				int []a = is;
				for (int i:a)
					if (i % 2 == 0)
						a[0] = -1;

				return null;
			}
		}
		,new BenchFunc("with IntFunc"){
			public Object apply() {
				select(is,intF);
				return null;
			}
		}
		,new BenchFunc("with better IntFunc"){
			public Object apply() {
				select(is,intF2);
				return null;
			}
		}
		,new BenchFunc("with specialized interface int=>boolean"){
			public Object apply() {
				select(is,even);
				return null;
			}
		}
		,new BenchFunc("with specialized final interface int=>boolean"){
			public Object apply() {
				selectFinal(is,even);
				return null;
			}
		}
		,new BenchFunc("with generic predicate (boxed ints)"){
			public Object apply() {
				select(is,gp);
				return null;
			}
		}
		,alloc.withIntAllocatorFunction(new BenchFunc("with generic predicate (int allocator)"){
			public Object apply() {
				selectWithAllocator(is,gp2);
				return null;
			}
		})
		,new BenchFunc("with known class"){
			public Object apply() {
				select(is,kp);
				return null;
			}
		}
		,new BenchFunc("with known class with final method"){
			public Object apply() {
				select(is,kp2);
				return null;
			}
		}
		,new BenchFunc("with Function1"){
			public Object apply() {
				select(is,func);
				return null;
			}
		}
		,alloc.withIntAllocatorFunction(new BenchFunc("with IntAllocator"){
			public Object apply() {
				selectWithAlloc(is,func2);
				return null;
			}
		}));
	}
	static class MyInteger extends Number {
		int val;
		@Override
		public double doubleValue() {
			return val;
		}
		@Override
		public float floatValue() {
			return val;
		}
		@Override
		public int intValue() {
			return val;
		}
		@Override
		public long longValue() {
			return val;
		}
	}
	/*@Test
	public void testRunnerPutField(){

		Integer i = new Integer(500);
		Runner r = new Runner() {
			@Override
			public int[] is() {
				return null;
			}
		};
		r.test(i);
		assertEquals(28,i.intValue());
	}*/
}
