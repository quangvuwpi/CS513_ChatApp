/**
 * 
 */
package edu.wpi.quangvu.app.net.transport;

import edu.wpi.quangvu.app.utility.AbstractRunnableTask;
import edu.wpi.quangvu.app.utility.RoundRobinQueue;

/**
 * Transport I/O task
 * 
 * @author quangvu
 *
 */
public class TransportTask extends AbstractRunnableTask {

	private static TransportTask instance = null;

	private RoundRobinQueue<NetConnection> queue = new RoundRobinQueue<NetConnection>();

	private boolean running = true;

	protected TransportTask() {
	}

	public static TransportTask getInstance() {
		if (instance == null) {
			instance = new TransportTask();
		}
		return instance;
	}

	public synchronized void add(NetConnection connection) {
		queue.add(connection);
	}

	public synchronized void remove(NetConnection connection) {
		queue.remove(connection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (running) {

			if (queue.hasNext()) {
				queue.next().update();
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				running = false;
			}
		}
	}

	@Override
	public void setup() {
		running = true;
	}

	@Override
	public void teardown() {
		running = false;
		queue.clear();
	}

}
