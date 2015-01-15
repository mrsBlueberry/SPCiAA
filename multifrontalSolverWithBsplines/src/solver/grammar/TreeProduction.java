package solver.grammar;

public class TreeProduction extends Production{
	

	public TreeProduction(Vertex vert, VertexType type) {
		super(vert);
		this.type = type;
		
	}

	@Override
	public void apply(Vertex vert) {
		switch(type){
		case ROOT:
			vert.initializeVertex(Stuff.p*3, type, 2);		
			break;
		case INTERNAL:
			vert.initializeVertex(Stuff.p*3, type, 2);
			break;
		case LEAF_PARENT:
			vert.initializeVertex(Stuff.p*2+1, type, Stuff.p+1);
			break;
		case LEAF:
			vert.initializeVertex(Stuff.p+1, type, 0);
			break;
			
		}
		
	}


}
