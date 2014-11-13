import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @(#)Executor.java
 * 
 * 
 * @author
 * @version 1.00 2011/6/13
 */

class Executor extends Thread {
	private int power = 4;
	List<List<Vertex>> treesByLevel = new ArrayList<>();
	private CyclicBarrier barrier;

	private void makeBarrier(int nodes) {
		if (barrier != null) {
			throw new RuntimeException("Nested barrier");
		}
		barrier = new CyclicBarrier(nodes + 1);
	}

	private void sync() {
		try {
			barrier.await();
			barrier = null;
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	}

	private List<Vertex> level(int lvl) {
		return treesByLevel.get(lvl);
	}

	private int levelSize(int lvl) {
		return level(lvl).size();
	}

	private int height() {
		return treesByLevel.size();
	}

	private List<Vertex> leaves() {
		return level(height() - 1);
	}

	private int leafCount() {
		return leaves().size();
	}

	private Vertex firstLeaf() {
		return leaves().get(0);
	}

	private Vertex lastLeaf() {
		return leaves().get(leafCount() - 1);
	}

	private Vertex root() {
		return level(0).get(0);
	}

	public synchronized void run() {

		Vertex S = new Vertex(null, null, null, "S");
		Production.h = 1.0/(Math.pow(2.0, power) -1.0);
		makeBarrier(1);
		P1 p1 = new P1(S, barrier);
		p1.start();
		sync();

		make2kTree(p1);
		traverse(p1.m_vertex, 0);
		for (int t = 0; t < 10000; ++t) {

			makeBarrier(leafCount());
			A1 a1 = new A1(firstLeaf(), barrier);
			a1.start();
			for (int i = 1; i < leafCount() - 1; ++i) {
				A a = new A(leaves().get(i), barrier);
				a.start();
			}
			AN an = new AN(lastLeaf(), barrier);
			an.start();
			sync();

			for (int i = height() - 2; i > 0; --i) {
				makeBarrier(levelSize(i));
				for (Vertex v : level(i)) {
					A2 a2 = new A2(v, barrier);
					a2.start();
				}
				sync();

				makeBarrier(levelSize(i));
				for (Vertex v : level(i)) {
					E2 e2 = new E2(v, barrier);
					e2.start();
				}
				sync();
			}

			makeBarrier(1);
			Aroot aRoot = new Aroot(root(), barrier);
			aRoot.start();
			sync();

			makeBarrier(1);
			Eroot eRoot = new Eroot(root(), barrier);
			eRoot.start();
			sync();

			for (int i = 0; i < height() - 1; ++i) {
				makeBarrier(levelSize(i));
				for (Vertex v : level(i)) {
					BS b = new BS(v, barrier);
					b.start();
				}
				sync();
			}

			// Output
			if (t % 100 == 0) {
				List<Double> result = new ArrayList<>();
				for (int i = 0; i < leafCount(); ++i) {
					result.add(leaves().get(i).m_x[1]);
				}
				result.add(lastLeaf().m_x[2]);
				ResultPrinter.printResult(result);
				delay(1000);
			}
		}

		System.out.println("Done.");

	}

	private void delay(int ms) {
		try {
			TimeUnit.MILLISECONDS.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}

	private void traverse(Vertex v, int level) {
		if (v == null) {
			return;
		}
		if (treesByLevel.size() == level) {
			treesByLevel.add(new ArrayList<Vertex>());
		}
		treesByLevel.get(level).add(v);
		traverse(v.m_left, level + 1);
		traverse(v.m_right, level + 1);
	}

	private void make2kTree(Production production) {
		Queue<Production> queue = new LinkedList<>();
		queue.add(production);
		int k = 2;
		while (!queue.isEmpty()) {
			Iterator<Production> i = queue.iterator();
			List<Production> l = new LinkedList<>();
			int size = queue.size();
			CyclicBarrier barrier = new CyclicBarrier(size * 2 + 1);
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
