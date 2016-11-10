/**
 * 
 */
package edu.wpi.quangvu.app.utility;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author quangvu
 *
 */
public class RoundRobinQueue<T> implements Iterator<T> {

	private final LinkedList<T> list = new LinkedList<T>();

	private int pos = 0;
	private T last = null;

	public RoundRobinQueue() {
		// TODO Auto-generated constructor stub
	}

	public synchronized void add(T element) {
		list.add(element);
	}
	
	public synchronized void remove(T element) {
		if (list.remove(element)) {			
			if (last == element) {
				//pos = nextPos();
				// potential bug
			}
		}
	}
	
	protected int nextPos() {
		if (pos >= list.size() - 1) {
			pos = 0;
		} else {
			pos++;
		}
		
		return pos;
	}

	@Override
	public synchronized boolean hasNext() {
		return !list.isEmpty();
	}

	public synchronized T peek() {
		if (hasNext()) {
			return list.get(pos);			
		}
		return null;
	}
	
	@Override
	public synchronized T next() {
		if (hasNext()) {
			pos  = nextPos();
			last = list.get(pos);			
		} else {
			pos = 0;
			last = null;
		}

		return last;
	}

	@Override
	public synchronized void remove() {
		if (last != null && hasNext()) {
			list.remove(last);
		}
	}
	
	public synchronized void clear() {
		list.clear();
	}

}
