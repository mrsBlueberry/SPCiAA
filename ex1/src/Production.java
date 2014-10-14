/**
 * @(#)Production.java
 *
 *
 * @author
 * @version 1.00 2011/6/13
 */


abstract class Production extends Thread {

	Production(Vertex Vert,Counter Count){
		m_vertex = Vert;
		m_counter = Count;
		m_drawer = new GraphDrawer();
	}

	//returns first vertex from the left
	abstract Vertex apply(Vertex v);

	//run the thread
	public void run() {
		m_counter.inc();
		//apply the production
		m_vertex = apply(m_vertex);
		//plot the graph
		m_drawer.draw(m_vertex);
		m_counter.dec();
	}

	//vertex where the production will be applied
	Vertex m_vertex;
	//graph drawer
	GraphDrawer m_drawer;
	//productions counter
	Counter m_counter;
}

class P1 extends Production {
	P1(Vertex Vert,Counter Count){
		super(Vert,Count);
	}
	Vertex apply(Vertex S) {
	  System.out.println("p1");
	  Vertex T1 = new Vertex(null,null,S,"T");
	  Vertex T2 = new Vertex(null,null,S,"T");
	  S.set_left(T1);
	  S.set_right(T2);
	  S.set_label("root");
	  return S;
	}
}

class P2 extends Production{
	P2(Vertex vert, Counter count){
		super(vert, count);
	}
	
	Vertex apply(Vertex vert){
		System.out.println("p2");
		Vertex t1 = new Vertex(null, null, vert, "T");
		Vertex t2 = new Vertex(null, null, vert, "T");
		vert.set_left(t1);
		vert.set_right(t2);
		vert.set_label("int");
		return vert;
	}
}

class P3 extends Production{
	P3(Vertex vert, Counter count){
		super(vert, count);
	}
	
	Vertex apply(Vertex vert){
		System.out.println("p3");
		Vertex t1 = new Vertex(null, null, vert, "node");
		Vertex t2 = new Vertex(null, null, vert, "node");
		vert.set_left(t1);
		vert.set_right(t2);
		vert.set_label("int");
		return vert;
	}
}

