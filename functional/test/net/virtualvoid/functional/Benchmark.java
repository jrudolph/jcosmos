package net.virtualvoid.functional;

import static java.text.MessageFormat.format;
import static net.virtualvoid.functional.Types.tuple;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import net.virtualvoid.functional.Functions.IRichFunction1;
import net.virtualvoid.functional.Functions.IRichFunction2;
import net.virtualvoid.functional.Functions.RichFunction0;
import net.virtualvoid.functional.Functions.RichFunction1;
import net.virtualvoid.functional.Functions.RichFunction2;
import net.virtualvoid.functional.Tuples.Tuple2;
import net.virtualvoid.functional.mutable.Array;
import net.virtualvoid.functional.util.Comparators;

public class Benchmark {
	public static IRichFunction2<Integer,Function0<?>,Long> benchmark =
		new RichFunction2<Integer, Function0<?>, Long>(){
			public Long apply(Integer arg1, Function0<?> runner) {
				System.out.println(format("Running {0}",runner.toString()));
				int times = arg1;

				long time = System.nanoTime();

				for (;times>=0;times--)
					runner.apply();

				long stop = System.nanoTime();

				return stop - time;
			}
		};

	public static abstract class BenchFunc
	extends RichFunction0<Object>{
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
			return String.format("%4.1f times more %s than"
				, p > 1 ? p : 1d/p
				, p > 1 ? pos : neg);
		else
			return format("as {0}/{1} as",pos,neg);
	}
	String[] durationNames = new String[]{
		"Years"

	};
	@SuppressWarnings("unchecked")
	private final static Function1<Long,Tuple2<Long,String>> durations =
		Comparators.rangeMap(tuple(365l * 24 * 60 * 60 * 1000 * 1000 * 1000,"Year"),
				Array.instance(
						tuple(7l * 24 * 60 * 60 * 1000 * 1000 * 1000,"Week")
						,tuple(24l * 60 * 60 * 1000 * 1000 * 1000,"Day")
						,tuple(60l * 60 * 1000 * 1000 * 1000,"Hour")
						,tuple(60l * 1000 * 1000 * 1000,"Minute")
						,tuple(1000l * 1000 * 1000,"Second")
						,tuple(1000l * 1000,"Millisecond")
						,tuple(1000l,"Microsecond")
						,tuple(1l,"Nanosecond")));

	public static String durationName(long nanos){
		Tuple2<Long, String> entry = durations.apply(nanos);
		return String.format("%5.1f %ss", (double)nanos/entry.ele1(),entry.ele2());
	}
	public static void shootout(int times,Function0<?>reference,Function0<?>...contrahents){
		final IRichFunction1<Function0<?>, Long> bencher = benchmark.applyPartial(times);

		final long referenceTime = bencher.apply(reference);

		// with closure syntax the following would be:
		// map(f:Function0 => tuple(f.toString,(double)benchmark(times,f)/referenceTime))
		Array<Tuple2<String, Long>> results = Array.instance(contrahents)
			.map(Functions.multiFunc(Strings.toStringF, bencher))
			.sort(Comparators.byChild(
					Tuple2.<String,Long>ele2F()
					,Comparators.fromComparable(Long.class)));

		Function1<Tuple2<String,Long>,String> labeler = new RichFunction1<Tuple2<String,Long>,String>(){
			public String apply(Tuple2<String, Long> arg1) {
				long time = arg1.ele2();
				double share = (double)time/referenceTime;
				return String.format(
						"%-30.30s reference %+6.1f%% (%-36s time: %s"
						,arg1.ele1().toString()
						,(share - 1) * 100
						,shareNames(share, "slow", "fast")+" reference)"
						,durationName(time));
			}
		};

		System.out.println(format("reference: {0} time: {1}",reference.toString(),durationName(referenceTime)));
		results
			.map(labeler)
			.fold(Strings.println, System.out);

		final Date now = new Date();

		File benchmarkRuns = new File("benchruns.csv");
		PrintStream stream;
		try {
			stream = new PrintStream(new FileOutputStream(benchmarkRuns,true));
		} catch (FileNotFoundException e) {
			throw new Error(e);
		}

		Function1<Tuple2<String,Long>,String> csvLabeler =
			new RichFunction1<Tuple2<String,Long>,String>(){
				public String apply(Tuple2<String, Long> arg1) {
					return format("{2,date,short};{2,time,long};{0};{1}",arg1.ele1(),arg1.ele2(),now);
				}
			};
		results
			// add reference result
			.append(tuple(reference.toString(),referenceTime))
			// label
			.map(csvLabeler)
			//print
			.fold(Strings.println, stream)/*System.out*/;

		stream.close();
	}
}
