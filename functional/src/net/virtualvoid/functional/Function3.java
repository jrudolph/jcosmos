/**
 * 
 */
package net.virtualvoid.functional;

public interface Function3<Arg1T,Arg2T,Arg3T,ResT> {
	ResT apply(Arg1T arg1,Arg2T arg2,Arg3T arg3);
}