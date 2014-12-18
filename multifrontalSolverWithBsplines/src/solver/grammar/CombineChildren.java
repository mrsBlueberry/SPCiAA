package solver.grammar;

public class CombineChildren extends Production{

	int interval;
	int ommitCount;
	public CombineChildren(Vertex vert, VertexType type, int interval, int ommitCount) {
		super(vert, type);
		this.interval = interval;
		this.ommitCount = ommitCount;
	}

	@Override
	public void apply(Vertex vert) {
		switch(vert.type){
		case LEAF_PARENT:
			break;
		case INTERNAL:
		case ROOT:
			
		}
	}

}
