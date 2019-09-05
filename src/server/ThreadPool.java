/**
 * Name: Kazuto Okamoto
 * Username: KOKAMOTO
 * Student ID: 1035484
 */

package server;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class is for implementing thread pool without library such as 
 * "ThreadPoolExecutor". Pool size is chosen by DictionaryServer class.
 */
public class ThreadPool implements DateTime {
	private ThreadQueue <Runnable> queue;
	
	public ThreadPool(int threadNum, ServerFrame frame) {
		queue = new ThreadQueue(frame);
		String threadName = null;
		Executor task = null;
		
		// Start the certain number (theadNum) of threads.
		for (int i = 1; i <= threadNum; i++) {
			threadName = "Thread " + i;
			task = new Executor(queue, frame);
			Thread thread = new Thread(task, threadName);
			thread.start();
		}
	}
	
	// Enqueue task passed from server.
	public void enqueueTask(Runnable task) {
		queue.enqueue(task);
	}
	
	// This class is for implementing thread queue.
	private class ThreadQueue<T> {
		private Queue<T> queue = new LinkedList<T>();
		private ServerFrame frame;
		
		public ThreadQueue (ServerFrame frame) {
			this.frame = frame;
		}
		
		public synchronized void enqueue(T task) {
			if (queue.size() == 0) {
				notify();
			} else {
				try {
					wait();
				} catch (InterruptedException e) {
					frame.getThreadInfo().append(Thread.currentThread().getName() + "interrupted.\n");
					System.out.println(e.getMessage());
				}
			}
			
			queue.add(task);
		}
		
		public synchronized T dequeue() {
			if (queue.size() != 0) {
				notify();
			} else {
				try {
					wait();
				} catch (InterruptedException e) {
					frame.getThreadInfo().append(Thread.currentThread().getName() + "interrupted.\n");
					System.out.println(e.getMessage());
				}
			}
			return queue.poll();
		}
	}
	
	// This class is for executing task and log thread information.
	private class Executor implements Runnable {
		private ThreadQueue<Runnable> queue;
		private ServerFrame frame;
		
		public Executor(ThreadQueue<Runnable> queue, ServerFrame frame) {
			this.queue = queue;
			this.frame = frame;
		}
		
		@Override
		public void run() {
			try {
				while (true) {
					// Dequeue one waiting task from thread queue and run it.
					Runnable task = queue.dequeue();
					frame.getThreadInfo().append(Thread.currentThread().getName() + " starts at " + getDateTime() +"\n\n");
					task.run();
					frame.getThreadInfo().append(Thread.currentThread().getName() + " ends at " + getDateTime() +"\n\n");
				}
			} catch (Exception e) {
				frame.getThreadInfo().append(Thread.currentThread().getName() + " interruptedat at " + getDateTime() +"\n\n");
				System.out.println(e.getMessage());
			}
		}
	}
}
