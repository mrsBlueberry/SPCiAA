import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @(#)Executor.java
 *
 *
 * @author
 * @version 1.00 2011/6/13
 */


class Executor extends Thread {
	private int power = 5;
	List<List<Vertex>> treesByLevel = new ArrayList();
	

	public synchronized void run() {

		Vertex S = new Vertex(null, null, null, "S");

		CyclicBarrier barrier = new CyclicBarrier(2);
		P1 p1 = new P1(S, barrier);
		p1.start();
		try {
			barrier.await();

			make2kTree(p1);
			traverse(p1.m_vertex, 0);
			int idx = treesByLevel.size() - 1;
			barrier = new CyclicBarrier(treesByLevel.get(idx).size() + 1);
			A1 a1 = new A1(treesByLevel.get(idx).get(0), barrier);
			a1.start();
			for (int i = 1; i < treesByLevel.get(idx).size() - 1; ++i) {
				A a = new A(treesByLevel.get(idx).get(i), barrier);
				a.start();
			}
			AN an = new AN(treesByLevel.get(idx).get(
					treesByLevel.get(idx).size() - 1), barrier);
			an.start();

			barrier.await();
			for (int i = idx - 1; i > 0; --i) {
				barrier = new CyclicBarrier(treesByLevel.get(i).size() + 1);
				for (Vertex v : treesByLevel.get(i)) {
					A2 a2 = new A2(v, barrier);
					a2.start();
				}

				barrier.await();
				barrier = new CyclicBarrier(treesByLevel.get(i).size() + 1);
				for (Vertex v : treesByLevel.get(i)) {
					E2 e2 = new E2(v, barrier);
					e2.start();
				}
				barrier.await();
			}
			barrier = new CyclicBarrier(2);
			Aroot aRoot = new Aroot(treesByLevel.get(0).get(0), barrier);
			aRoot.start();
			barrier.await();
			barrier = new CyclicBarrier(2);
			Eroot eRoot = new Eroot(treesByLevel.get(0).get(0), barrier);
			eRoot.start();
			barrier.await();

			for (int i = 0; i < idx; ++i) {
				barrier = new CyclicBarrier(treesByLevel.get(i).size() + 1);
				for (Vertex v : treesByLevel.get(i)) {
					BS b = new BS(v, barrier);
					b.start();
				}
				barrier.await();
			}

			List<Double> result = new ArrayList<>();
			System.out.println("x0" + "="
					+ treesByLevel.get(idx).get(0).m_x[1]);
			result.add(treesByLevel.get(idx).get(0).m_x[1]);
			for (int i = 1; i < treesByLevel.get(idx).size(); ++i) {
				System.out.println("x" + i + "="
						+ treesByLevel.get(idx).get(i).m_x[2]);
				result.add(treesByLevel.get(idx).get(i).m_x[2]);

			}
			ResultPrinter.printResult(result);
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void traverse(Vertex v, int level){
		if(v == null){
			return;
		}
		if(treesByLevel.size()== level){
			treesByLevel.add(new ArrayList<Vertex>());
		}
		treesByLevel.get(level).add(v);
		traverse(v.m_left, level+1);
		traverse(v.m_right, level+1);
	}

	private void make2kTree(Production production) {
		Queue<Production> queue = new LinkedList();
		queue.add(production);
		int k = 1;
		while (!queue.isEmpty()) {
			Iterator<Production> i = queue.iterator();
			List<Production> l = new LinkedList();
			int size = queue.size();
			CyclicBarrier barrier = new CyclicBarrier(size*2+1);
			while (i.hasNext()) {
				l.add(queue.poll());
			}
			for (Production p : l) {
				if (k < power) {
					P2 p2a = new P2(p.m_vertex.m_left, barrier);
					P2 p2b = new P2(p.m_vertex.m_right, barrier);
					p2a.start();
					p2b.start();
					queue.add(p2b);
					queue.add(p2a);
				} else {
					P3 p3a = new P3(p.m_vertex.m_left, barrier);
					P3 p3b = new P3(p.m_vertex.m_right, barrier);
					p3a.start();
					p3b.start();
				}
			}
			try {
				barrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			++k;
		}
	}
}

