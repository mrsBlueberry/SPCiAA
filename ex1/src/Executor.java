import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @(#)Executor.java
 * 
 * 
 * @author
 * @version 1.00 2011/6/13
 */

class Executor extends Thread {

	private int power = 3;

	public synchronized void run() {
		Counter counter = new Counter(this);
		Vertex S = new Vertex(null, null, null, "S");
		// [(P1)]
		P1 p1 = new P1(S, counter);
		p1.start();
		counter.release();

		make2kTree(p1, counter);
	}

	private void make2kTree(Production production, Counter counter) {
		Queue<Production> queue = new LinkedList();
		queue.add(production);
		int k = 1;
		while (!queue.isEmpty()) {
			Iterator<Production> i = queue.iterator();
			List<Production> l = new LinkedList();
			while (i.hasNext()) {
				l.add(queue.poll());

			}
			for (Production p : l) {
				if (k < power) {
					P2 p2a = new P2(p.m_vertex.m_left, counter);
					P2 p2b = new P2(p.m_vertex.m_right, counter);

					p2a.start();
					p2b.start();
					queue.add(p2b);
					queue.add(p2a);
				} else {
					P3 p3a = new P3(production.m_vertex.m_left, counter);
					P3 p3b = new P3(production.m_vertex.m_right, counter);
					p3a.start();
					p3b.start();

				}
			}
			counter.release();

			++k;

		}

	}
}
