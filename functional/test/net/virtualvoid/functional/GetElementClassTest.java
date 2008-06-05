package net.virtualvoid.functional;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;
import net.virtualvoid.functional.Functions.Function;
import net.virtualvoid.functional.Functions.Function1;
import net.virtualvoid.functional.Functions.RichFunction0;
import net.virtualvoid.functional.Tuples.Tuple2;
import net.virtualvoid.functional.mutable.Array;

import org.testng.annotations.Test;

public class GetElementClassTest {
	@Test
	public void testComplexType(){
		Function<Tuple2<String,Integer>> f = new RichFunction0<Tuple2<String,Integer>>(){
			public Tuple2<String, Integer> apply() {
				return null;
			}
		};
		assertEquals("Tuple2",f.getResultType().getSimpleName());
	}
	@Test
	public void testMappedClass(){
		Function1<? super Integer,String> iToS = Strings.toStringF;
		Array<Integer> is = Array.instance(1,2,3,4);
		assertEquals(Integer.class,is.getElementClass());
		ISequence<String> strs = is.map(iToS);
		assertEquals(String.class,strs.getElementClass());
	}
	@Test
	public <T> void typeRefGenericArrayInstance(){
		assertTrue(new TypeRef<T[]>(){}.clazz().isArray());
	}
}
