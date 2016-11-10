/**
 * 
 */
package edu.wpi.quangvu.app.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import edu.wpi.quangvu.app.utility.AbstractManageableResource;
import edu.wpi.quangvu.app.utility.IRunnableTask;

/**
 * Managing tasks of ChatApp
 * 
 * @author quangvu
 *
 */
public class TaskManager extends AbstractManageableResource {

	static final int MAX_THREAD_COUNT = 5;

	private static TaskManager instance = null;

	/** Registered but not running tasks **/
	protected ArrayList<IRunnableTask> list = new ArrayList<IRunnableTask>();
	/** Registered and currently running task **/
	protected HashMap<IRunnableTask, Future<?>> running = new HashMap<IRunnableTask, Future<?>>();
	/** Thread pool **/
	protected ExecutorService exec = Executors
			.newFixedThreadPool(MAX_THREAD_COUNT);

	protected TaskManager() {
	}

	public static TaskManager getInstance() {
		if (instance == null) {
			instance = new TaskManager();
		}
		return instance;
	}

	/**
	 * Register a task
	 * 
	 * @param task
	 *            the task to register
	 */
	public void register(IRunnableTask task) {
		if (!list.contains(task)) {
			list.add(task);
		}
	}

	/**
	 * Register and immediate start a task
	 * 
	 * @param task
	 *            the task to start
	 */
	public void start(IRunnableTask task) {
		register(task);
		
		if (!running.containsKey(task)) {
			Future<?> f = exec.submit(task);
			running.put(task, f);
		}
	}

	/**
	 * Stop a running thread
	 * 
	 * @param task
	 *            the task to stop
	 */
	public void stop(IRunnableTask task) {
		if (running.containsKey(task)) {
			Future<?> f = running.get(task);

			f.cancel(true);
		}
	}

	/**
	 * Stop all tasks
	 */
	public void stop() {
		Iterator<IRunnableTask> i = list.iterator();
		while (i.hasNext()) {
			stop(i.next());
		}
	}

	/**
	 * Start all registered task
	 */
	public void start() {
		Iterator<IRunnableTask> i = list.iterator();
		while (i.hasNext()) {
			IRunnableTask t = i.next();
			
			t.setup();
			start(t);
		}
	}

	@Override
	public void teardown() {
		// Stop all running threads
		stop();
		
		// Call the teardown method on each
		Iterator<IRunnableTask> i = list.iterator();
		while (i.hasNext()) {
			i.next().teardown();
		}
		
		exec.shutdown();
	}

}
