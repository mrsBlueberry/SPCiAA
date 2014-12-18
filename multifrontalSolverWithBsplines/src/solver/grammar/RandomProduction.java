package solver.grammar;

public class RandomProduction extends Production {

	public RandomProduction(Vertex vert, VertexType type) {
		super(vert, type);
		
	}

	@Override
	public void apply(Vertex vert) {
				
			vert.initializeVertex(Stuff.p+1, type, 0);
			vert.generateRandomValues();
			
			
		
		
	}

}
