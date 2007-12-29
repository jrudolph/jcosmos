package net.virtualvoid.functional;

import static java.text.MessageFormat.format;
import net.virtualvoid.functional.Functions.Function0;
import net.virtualvoid.functional.Functions.Function1;
import net.virtualvoid.functional.Functions.IRichFunction1;
import net.virtualvoid.functional.Functions.IRichFunction2;
import net.virtualvoid.functional.Functions.RichFunction2;
import net.virtualvoid.functional.mutable.Array;


public class Benchmark {
	public static IRichFunction2<Integer,Function0<?>,Long> benchmark =
		new RichFunction2<Integer, Function0<?>, Long>(){
			public Long apply(Integer arg1, Function0<?> runner) {
				int times = arg1;

				long time = System.nanoTime();

				for (;times>=0;times--)
					runner.apply();

				long stop = System.nanoTime();

				return stop - time;
			}
		};

	public static abstract class BenchFunc implements Function0<Object>{
		private final String name;

		public BenchFunc(String name) {
			super();
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public static void benchmarkAndReport(String name,int times,Function0<?>runner){
		long time = benchmark.apply(times,runner);
		System.out.println(format("{0}: {1,number} ns = {2,number} ns/run",name,time,(double)time/times));
	}
	public static void benchmarkAndReport(int times,Function0<?>runner){
		benchmarkAndReport(runner.toString(),times, runner);
	}
	public static String shareNames(double p,String pos,String neg){
		if (Math.abs(p-1)>0.05)
			return format("{0,number,#.#} times more {1} than"
				, p > 1 ? p : 1d/p
				, p > 1 ? pos : neg);
		else
			return format("as {0}/{1} as",pos,neg);
	}
	public static void shootout(int times,Function0<?>reference,Function0<?>...contrahents){
		final IRichFunction1<Function0<?>, Long> bencher = benchmark.applyPartial(times);

		final long referenceTime = bencher.apply(reference);

		// with closure syntax the following would be:
		// map(f:Function0 => tuple(f.toString,(double)benchmark(times,f)/referenceTime))
		IRandomAccessSequence<String> results = Array.instance(contrahents)
			.map(new Function1<Function0<?>,String>(){
				public String apply(Function0<?> arg1) {
					Long time = bencher.apply(arg1);
					double share = (double)time/referenceTime;
					return format("{0}:\n\t{2,number,+ ##0.0%;- ##0.0%} compared to refs runtime\t({1} reference) time:{3,number}ns",arg1.toString(),shareNames(share, "slow", "fast"),share - 1,time);
				}
			});
		System.out.println(format("reference: {0} time: {1,number}ns",reference.toString(),referenceTime));
		results.fold(Strings.println, System.out);
	}
}
