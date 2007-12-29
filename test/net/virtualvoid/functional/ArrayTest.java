package net.virtualvoid.functional;

import static net.virtualvoid.functional.BoxingBenchmarkTest.richBenchmark;
import static org.testng.AssertJUnit.assertEquals;
import net.virtualvoid.functional.Functions.Function0;
import net.virtualvoid.functional.Functions.Function2;
import net.virtualvoid.functional.Functions.RichFunction1;
import net.virtualvoid.functional.mutable.Array;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ArrayTest {
	public final static Function2<Integer,Integer,Integer> add =
		new Function2<Integer,Integer,Integer>(){
			public Integer apply(Integer arg1, Integer arg2) {
				return arg1 + arg2;
			}
		};
	public final static Function2<Integer,Integer,Integer> mult =
		new Function2<Integer,Integer,Integer>(){
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
		return new Function2<StringBuilder, String, StringBuilder>(){
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
	private final static IRichSequence<Integer> arraySublist = Array.instance(1,1,1,0,1,2,3,4,5,6,7,8).sublist(4,6);
	private final static IRichSequence<Integer> rangeSeq = Sequences.range(1, 7);
	private final static IRichSequence<Integer> rangeSeqSublist = Sequences.range(-6, 12).sublist(7,6);

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
	public void testFold(IRichSequence<Integer> numbers){
		int sum = numbers.fold(add, 0);
		assertEquals(21,sum);
	}
	@Test(dataProvider = "sequences")
	public void testSelect(IRichSequence<Integer> numbers){
		IRichSequence<Integer> evenOnes = numbers.select(even);
		assertEquals(3,evenOnes.length());
		assertEquals("2,4,6",evenOnes.map(toStringF).fold(join(","), new StringBuilder()).toString());
	}
	@SuppressWarnings("unchecked")
	@Test
	public void testArrayPartition(){
		Object[] oa=array.partition(3).asArray();
		IRichSequence<Integer>[] ar = new IRichSequence[oa.length];
		System.arraycopy(oa, 0, ar, 0, oa.length);
		assertEquals(3,ar.length);
		assertEquals(2,ar[0].length());
		assertEquals(2,ar[1].length());
		assertEquals(2,ar[2].length());

		oa=array.partition(4).asArray();
		ar = new IRichSequence[oa.length];
		System.arraycopy(oa, 0, ar, 0, oa.length);
		assertEquals(3,ar.length);
		assertEquals(2,ar[0].length());
		assertEquals(2,ar[1].length());
		assertEquals(2,ar[2].length());
	}

	private final Function2<String,String,String> stringConcat = new Function2<String,String,String>(){
		public String apply(String arg1, String arg2) {
			// this is expensive for the sake of it
			return arg1 + arg2;
		}
	};
	@Test
	public void testReduce(){
		Sequences.range(0, 10000).map(toStringF).reduce(stringConcat, "");
	}

	@SuppressWarnings("unused") // referenced by name
	@DataProvider
	private Object[][]namedSeqs(){
		return new Object[][]{
			new Object[]{"Range [1,10)",Sequences.range(1, 10)}
			,new Object[]{"Range [1,10000)",Sequences.range(1, 10000)}
		};
	}
	@Test(dataProvider="namedSeqs")
	public void testReduceVsFoldTimes(String art,final IRichSequence<Integer> seq){
		richBenchmark("reduce with "+art, 10000, new Function0<Object>(){
			public Object apply() {
				seq.reduce(add, 0);
				return null;
			}
		});
		richBenchmark("fold with "+art, 10000, new Function0<Object>(){
			public Object apply() {
				seq.fold (add, 0);
				return null;
			}
		});
	}
}
