package net.virtualvoid.functional;

import static org.testng.AssertJUnit.assertEquals;
import net.virtualvoid.functional.Functions.Function2;
import net.virtualvoid.functional.Functions.RichFunction0;
import net.virtualvoid.functional.Functions.RichFunction1;
import net.virtualvoid.functional.Functions.RichFunction2;
import net.virtualvoid.functional.mutable.Array;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ArrayTest {
	public final static Function2<Integer,Integer,Integer> add =
		new RichFunction2<Integer,Integer,Integer>(Integer.class){
			public Integer apply(Integer arg1, Integer arg2) {
				return arg1 + arg2;
			}
		};
	public final static Function2<Integer,Integer,Integer> mult =
		new RichFunction2<Integer,Integer,Integer>(Integer.class){
			public Integer apply(Integer arg1, Integer arg2) {
				return arg1 * arg2;
			}
		};

	public final static RichFunction1<Integer,Boolean> even =
		new RichFunction1<Integer,Boolean>(){
			public Boolean apply(Integer arg1) {
				return arg1 % 2 == 0;
			}
		};

	public static Function2<StringBuilder,String,StringBuilder> join(final String sep){
		return new RichFunction2<StringBuilder, String, StringBuilder>(StringBuilder.class){
			public StringBuilder apply(StringBuilder arg1, String arg2) {
				if (arg1.length() != 0)
					arg1.append(sep);
				arg1.append(arg2);
				return arg1;
			}
		};
	}

	public static RichFunction1<Object,String> toStringF = new RichFunction1<Object, String>(){
		public String apply(Object arg1) {
			return arg1.toString();
		}
	};
	private final static Array<Integer> array = Array.instance(1,2,3,4,5,6);
	private final static ISequence<Integer> arraySublist = Array.instance(1,1,1,0,1,2,3,4,5,6,7,8).sublist(4,6);
	private final static ISequence<Integer> rangeSeq = Sequences.range(1, 7);
	private final static ISequence<Integer> rangeSeqSublist = Sequences.range(-6, 12).sublist(7,6);

	@DataProvider
	public Object[][]sequences(){
		return new Object[][]{
			new Object[]{array}
			,new Object[]{rangeSeq}
			,new Object[]{arraySublist}
			,new Object[]{rangeSeqSublist}
		};
	}

	@Test(dataProvider = "sequences")
	public void testFold(ISequence<Integer> numbers){
		int sum = numbers.fold(add, 0);
		assertEquals(21,sum);
	}
	@Test(dataProvider = "sequences")
	public void testSelect(ISequence<Integer> numbers){
		ISequence<Integer> evenOnes = numbers.select(even);
		assertEquals(3,evenOnes.length());
		assertEquals("2,4,6",evenOnes.map(toStringF).fold(join(","), new StringBuilder()).toString());
	}
	@SuppressWarnings("unchecked")
	@Test
	public void testArrayPartition(){
		ISequence<Integer>[] ar=array.partition(3).asNativeArray((Class)ISequence.class);
		assertEquals(3,ar.length);
		assertEquals(2,ar[0].length());
		assertEquals(2,ar[1].length());
		assertEquals(2,ar[2].length());

		ar=array.partition(4).asNativeArray((Class)ISequence.class);
		assertEquals(3,ar.length);
		assertEquals(2,ar[0].length());
		assertEquals(2,ar[1].length());
		assertEquals(2,ar[2].length());
	}

	private final Function2<String,String,String> stringConcat = new RichFunction2<String,String,String>(String.class){
		public String apply(String arg1, String arg2) {
			// this is expensive for the sake of it
			return arg1 + arg2;
		}
	};
	@Test
	public void testReduce(){
		Sequences.range(0, 10000).map(toStringF).reduce(stringConcat);
	}

	@SuppressWarnings("unused") // referenced by name
	@DataProvider
	private Object[][]namedSeqs(){
		return new Object[][]{
			new Object[]{"Range [1,10)",Sequences.range(1, 10)}
			,new Object[]{"Range [1,100000)",Sequences.range(1, 100000)}
		};
	}
	@Test(dataProvider="namedSeqs")
	public void testReduceVsFoldTimes(final String art,final ISequence<Integer> seq){
		Benchmark.shootout(1000, new RichFunction0<Object>(Object.class){
				public Object apply() {
					seq.fold (add, 0);
					return null;
				}
				@Override
				public String toString() {
					return "fold with" +art;
				}
			}
			,new RichFunction0<Object>(Object.class){
				public Object apply() {
					seq.reduce(add);
					return null;
				}
				@Override
				public String toString() {
					return "reduce with "+art;
				}
			});
	}
	@Test
	public void testAsArray(){
		assertEquals(Integer[].class,rangeSeq.asNativeArray(Integer.class).getClass());
	}
}
