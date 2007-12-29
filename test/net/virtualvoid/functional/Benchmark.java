package net.virtualvoid.functional;

import static java.text.MessageFormat.format;
import net.virtualvoid.functional.Functions.Function0;


public class Benchmark {
	public static long benchmark(int times,Function0<?> runner){
		long time = System.nanoTime();

		for (;times>=0;times--)
			runner.apply();

		long stop = System.nanoTime();
		return stop - time;
	}
	public static void benchmarkAndReport(String name,int times,Function0<?>runner){
		long time = benchmark(times,runner);
		System.out.println(format("{0}: {1,number} ns = {2,number} ns/run",name,time,(double)time/times));
	}
	public static void benchmarkAndReport(int times,Function0<?>runner){
		benchmarkAndReport(runner.toString(),times, runner);
	}
}
