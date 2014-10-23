import java.util.ArrayList;
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
	List<List<Vertex>> treesByLevel = new ArrayList();
	

	public synchronized void run() {
		Counter counter = new Counter(this);
		Vertex S = new Vertex(null, null, null, "S");

		P1 p1 = new P1(S, counter);
		p1.start();
		counter.release();
//
//		P2 p2a = new P2(p1.m_vertex.m_left, counter);
//		P2 p2b = new P2(p1.m_vertex.m_right, counter);
//		p2a.start();
//		p2b.start();
//		counter.release();
//
//		P2 p2c = new P2(p2a.m_vertex.m_left, counter);
//		P2 p2d = new P2(p2a.m_vertex.m_right, counter);
//		P3 p3a = new P3(p2b.m_vertex.m_left, counter);
//		P3 p3b = new P3(p2b.m_vertex.m_right, counter);
//		p2c.start();
//		p2d.start();
//		p3a.start();
//		p3b.start();
//		counter.release();
//
//		P3 p3c = new P3(p2c.m_vertex.m_left, counter);
//		P3 p3d = new P3(p2c.m_vertex.m_right, counter);
//		P3 p3e = new P3(p2d.m_vertex.m_left, counter);
//		P3 p3f = new P3(p2d.m_vertex.m_right, counter);
//		p3c.start();
//		p3d.start();
//		p3e.start();
//		p3f.start();
//		// MULTI-FRONTAL SOLVER ALGORITHM
//
//		A1 localMat1 = new A1(p3c.m_vertex, counter);
//		A localMat2 = new A(p3d.m_vertex, counter);
//		A localMat3 = new A(p3e.m_vertex, counter);
//		A localMat4 = new A(p3f.m_vertex, counter);
//		A localMat5 = new A(p3a.m_vertex, counter);
//		AN localMat6 = new AN(p3b.m_vertex, counter);
//		localMat1.start();
//		localMat2.start();
//		localMat3.start();
//		localMat4.start();
//		localMat5.start();
//		localMat6.start();
//		counter.release();
//
//		A2 mergedMat1 = new A2(p2c.m_vertex, counter);
//		A2 mergedMat2 = new A2(p2d.m_vertex, counter);
//		A2 mergedMat3 = new A2(p2b.m_vertex, counter);
//		mergedMat1.start();
//		mergedMat2.start();
//		mergedMat3.start();
//		counter.release();
//
//		E2 gaussElimMat1 = new E2(p2b.m_vertex, counter);
//		E2 gaussElimMat2 = new E2(p2c.m_vertex, counter);
//		E2 gaussElimMat3 = new E2(p2d.m_vertex, counter);
//		gaussElimMat1.start();
//		gaussElimMat2.start();
//		gaussElimMat3.start();
//		counter.release();
//
//		A2 mergedMat4 = new A2(p2a.m_vertex, counter);
//		mergedMat4.start();
//		counter.release();
//
//		E2 gaussElimMat4 = new E2(p2a.m_vertex, counter);
//		gaussElimMat4.start();
//		counter.release();
//
//		Aroot mergedRootMat = new Aroot(p1.m_vertex, counter);
//		mergedRootMat.start();
//		counter.release();
//
//		Eroot fullElimMat = new Eroot(p1.m_vertex, counter);
//		fullElimMat.start();
//		counter.release();
//
//		BS backSub1 = new BS(p1.m_vertex, counter);
//		BS backSub2 = new BS(p2a.m_vertex, counter);
//		backSub1.start();
//		backSub2.start();
//		counter.release();
//
//		BS backSub3 = new BS(p2c.m_vertex, counter);
//		BS backSub4 = new BS(p2d.m_vertex, counter);
//		backSub3.start();
//		backSub4.start();
//		counter.release();
//		
//		double u7 = fullElimMat.m_vertex.m_b[2];
//		double u1 = fullElimMat.m_vertex.m_b[1];
//		double u5 = 2.0 / 3.0 * u7;
//		double u3 = 0.5 * u1 + 1.0 / 2.0 * u5;
//		double u2 = 0.5 * u1 + 0.5 * u3;
//		double u4 = 0.5 * u3 + 0.5 * u5;
//		double u6 = 0.5 * u5 + 0.5 * u7;
//		
//		System.err.println("u1="+u1+"\nu2="+u2+"\nu3="+u3+"\nu4="+u4+"\nu5="+u5+"\nu6="+u6+"\nu7="+u7);
		
		make2kTree(p1, counter);
		traverse(p1.m_vertex, 0);
		int idx = treesByLevel.size()-1;
		A1 a1 = new A1(treesByLevel.get(idx).get(0), counter);
		a1.start();
		for(int i=1;i<idx-1;++i){
			A a = new A(treesByLevel.get(idx).get(i), counter);
			a.start();
		}
		AN an = new AN(treesByLevel.get(idx).get(idx), counter);
		an.start();
		counter.release();
		for(int i=idx-1;i>0;--i){
			for(Vertex v : treesByLevel.get(i)){
				A2 a2 = new A2(v, counter);
				a2.start();
			}
			counter.release();
			for(Vertex v : treesByLevel.get(i)){
				E2 e2 = new E2(v, counter);
				e2.start();
			}
			counter.release();
		}
		Aroot aRoot = new Aroot(treesByLevel.get(0).get(0), counter);
		aRoot.start();
		counter.release();
		Eroot eRoot = new Eroot(treesByLevel.get(0).get(0), counter);
		eRoot.start();
		counter.release();
		
		for(int i=0;i<idx-1;++i){
			for(Vertex v : treesByLevel.get(i)){
				BS b = new BS(v, counter);
				b.start();
			}
			counter.release();
		}
		
		for(int i=0;i<treesByLevel.get(idx).size();++i){
			System.out.println("x"+i+"="+treesByLevel.get(idx).get(i).m_b[1]);
			System.out.println("x"+(i+1)+"="+treesByLevel.get(idx).get(i).m_b[2]);

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
					P3 p3a = new P3(p.m_vertex.m_left, counter);
					P3 p3b = new P3(p.m_vertex.m_right, counter);
					p3a.start();
					p3b.start();
				}
			}
			counter.release();
			++k;
		}
	}
}

