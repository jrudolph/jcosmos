package net.virtualvoid.functional.mutable;

import net.virtualvoid.functional.AbstractRichSequence;
import net.virtualvoid.functional.Functions.Function2;

public class LinkedList<T> extends AbstractRichSequence<T>{
	static class Entry<T> {
		T entry;
		Entry<T> tail;

		public <U> U fold(Function2<? super U, ? super T, U> func, U start) {
			U val = func.apply(start, entry);
			return tail == null ? val : tail.fold(func,val);
		}
	}
	private Entry<T> head;
	private Entry<T> tail;

	private final Class<T> elementType;
	public LinkedList(Class<T> elementType) {
		this.elementType = elementType;
	}
	public static <T> LinkedList<T> linkedListOf(Class<T> elementType){
		return new LinkedList<T>(elementType);
	}

	public <U> U fold(Function2<? super U, ? super T, U> func, U start) {
		if (head == null)
			return start;
		else
			return head.fold(func, start);
	}
	private Entry<T> newEntry(T ele){
		Entry<T> res = new Entry<T>();
		res.entry = ele;
		res.tail = null;
		return res;
	}
	public void append(T ele){
		Entry<T> newOne = newEntry(ele);
		if (head == null){
			assert tail == null;

			head = tail = newOne;
		}
		else{
			tail.tail = newOne;
			tail = newOne;
		}
	}
	public void prepend(T ele){
		Entry<T> newOne = newEntry(ele);
		if (head == null){
			assert tail == null;

			head = tail = newOne;
		}
		else{
			newOne.tail = head;
			head = newOne;
		}
	}
	public Class<? super T> getElementClass() {
		return elementType;
	}
}
