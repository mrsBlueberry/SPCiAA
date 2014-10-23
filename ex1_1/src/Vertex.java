/**
 * @(#)Vertex.java
 *
 *
 * @author
 * @version 1.00 2011/6/13
 */



class Vertex {
	//label
		String m_label;
		//links to adjacent elements
		Vertex m_left;
		Vertex m_right;
		Vertex m_parent;

		//local system of equations
		double[][] m_a;
		double[] m_b;
		double[] m_x;
	//constructor
	Vertex(Vertex Left, Vertex Right, Vertex Parent, String Lab){
		this.m_left=Left;
		this.m_right=Right;
		this.m_parent=Parent;
		this.m_label=Lab;
		m_a = new double[3][3];
		m_b = new double[3];
		m_x = new double[3];
	}
	//empty constructor
	Vertex(){
		this.m_left=null;
		this.m_right=null;
		this.m_parent=null;
		m_a = new double[3][3];
		m_b = new double[3];
		m_x = new double[3];
	}
	

	//methods for adding links
	void set_left(Vertex Left){
		m_left=Left;
	}
	void set_right(Vertex Right){
		m_right=Right;
	}
	void set_parent(Vertex Parent){
		m_parent=Parent;
	}
	void set_label(String Lab){
		m_label=Lab;
	}
}
