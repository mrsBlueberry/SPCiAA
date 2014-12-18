package solver.grammar;

import java.util.concurrent.CountDownLatch;

public abstract class Production implements Runnable {
	
	public CountDownLatch latch;
	public Vertex vertex;
	public VertexType type;
	
	
	
	public Production(Vertex vert, VertexType type){
		
		this.vertex = vert;
		this.type = type;
		
	}

	@Override
	public void run() {
		apply(vertex);
		getLatch().countDown();
		
	}
	
	public abstract void apply(Vertex vert);

	public CountDownLatch getLatch() {
		return latch;
	}

	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	} 

}

