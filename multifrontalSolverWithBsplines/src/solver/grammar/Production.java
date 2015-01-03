package solver.grammar;

import java.util.concurrent.CountDownLatch;

public abstract class Production implements Runnable {
	
	public CountDownLatch latch;
	public Vertex vertex;
	public VertexType type;
	
	
	
	public Production(Vertex vert){
		
		this.vertex = vert;
		
	}

	@Override
	public void run() {
		try{
			apply(vertex);
		}catch(Exception e){
			e.printStackTrace();
		}
		
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

