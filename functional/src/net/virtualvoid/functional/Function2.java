/**
 * 
 */
package net.virtualvoid.functional;

public interface Function2<Arg1T,Arg2T,ResT> {
	ResT apply(Arg1T arg1,Arg2T arg2);
}