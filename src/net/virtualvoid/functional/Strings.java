package net.virtualvoid.functional;

import java.io.PrintStream;

import net.virtualvoid.functional.Functions.Function1;
import net.virtualvoid.functional.Functions.Function2;
import net.virtualvoid.functional.Functions.IRichFunction1;
import net.virtualvoid.functional.Functions.RichFunction1;

public class Strings {
	public final static IRichFunction1<Object,String> toStringF =
		new RichFunction1<Object,String>(){
			public String apply(Object arg1) {
				return arg1.toString();
			}
		};
	public static Function1<String,String> surroundWith(final String prefix,final String suffix){
		return new Function1<String,String>(){
			public String apply(String arg1) {
				return prefix + arg1 + suffix;
			}
		};
	}
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
	public static Function2<PrintStream,String,PrintStream> println =
		new Function2<PrintStream,String,PrintStream>(){
			public PrintStream apply(PrintStream arg1, String arg2) {
				arg1.println(arg2);
				return arg1;
			}
		};
}
