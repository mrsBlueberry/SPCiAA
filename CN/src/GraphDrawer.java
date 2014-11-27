/**
 * @(#)GraphDrawer.java
 *
 *
 * @author
 * @version 1.00 2011/6/13
 */



class GraphDrawer {
	GraphDrawer() {
	}
	//draw the graph
	void draw(Vertex v) {
        //plot the tree in the pre-order
        //go up to the root
	  while(v.m_parent!=null) {
		v=v.m_parent;
	  }
	  //plot
	  plot_preorder(v);
	}
	void plot_preorder(Vertex v)
	{
		System.out.print(v.m_label+" ");
		if(v.m_left!=null) {
			System.out.print("Left son:");
			plot_preorder(v.m_left);
		}
		if(v.m_right!=null) {
			System.out.print("Right son:");
			plot_preorder(v.m_right);
		}
	    System.out.println(".");
	}
}
