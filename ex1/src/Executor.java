
/**
 * @(#)Executor.java
 *
 *
 * @author
 * @version 1.00 2011/6/13
 */


class Executor extends Thread {
	public synchronized void run() {
		Counter counter = new Counter(this);
		Vertex S = new Vertex(null,null,null,"S");
		//[(P1)]
		P1 p1 = new P1(S,counter);
		p1.start();
		counter.release();
		//[(P2)1(P2)2]
		P2 p2a = new P2(p1.m_vertex.m_left,counter);
		P2 p2b = new P2(p1.m_vertex.m_right,counter);
		p2a.start();
		p2b.start();
		counter.release();
		//[(P2)3(P2)4(P3)5(P3)6]
		P2 p2c = new P2(p2a.m_vertex.m_left,counter);
		P2 p2d = new P2(p2a.m_vertex.m_right,counter);
		P3 p3a = new P3(p2b.m_vertex.m_left,counter);
		P3 p3b = new P3(p2b.m_vertex.m_right,counter);
		p2c.start();
		p2d.start();
		p3a.start();
		p3b.start();
		counter.release();
		//[(P3)1(P3)2(P3)3(P3)4]
		P3 p3c = new P3(p2c.m_vertex.m_left,counter);
		P3 p3d = new P3(p2c.m_vertex.m_right,counter);
		P3 p3e = new P3(p2d.m_vertex.m_left,counter);
		P3 p3f = new P3(p2d.m_vertex.m_right,counter);
		p3c.start();
		p3d.start();
		p3e.start();
		p3f.start();

		
//		make2kTree(p1, counter, 1);
	}

	private void make2kTree(Production production, Counter counter, int k) {
		if (k == 3) {
			P3 p3a = new P3(production.m_vertex.m_left, counter);
			P3 p3b = new P3(production.m_vertex.m_right, counter);
			p3a.start();
			p3b.start();
		}else{
			P2 p2a = new P2(production.m_vertex.m_left, counter);
			P2 p2b = new P2(production.m_vertex.m_right, counter);
			
			p2a.start();
			p2b.start();
			make2kTree(p2b, counter, k +1);
			make2kTree(p2a, counter, k+1);
		}
	}
}

