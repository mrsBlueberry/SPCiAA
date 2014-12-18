package solver;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import solver.grammar.Production;
import solver.grammar.Stuff;
import solver.grammar.TreeProduction;
import solver.grammar.Vertex;
import solver.grammar.VertexType;

public class Application {

	private Exectuor exectuor = new Exectuor();
	private List<List<Vertex>> treeByLevel = new ArrayList<>();

	private List<Vertex> level(int lvl) {
		return treeByLevel.get(lvl);
	}

	private int levelSize(int lvl) {
		return level(lvl).size();
	}

	private int height() {
		return treeByLevel.size();
	}

	public List<Vertex> leaves() {
		return level(height() - 1);
	}

	private int leafCount() {
		return leaves().size();
	}

	private Vertex firstLeaf() {
		return leaves().get(0);
	}

	private Vertex lastLeaf() {
		return leaves().get(leafCount() - 1);
	}

	private Vertex root() {
		return level(0).get(0);
	}

	private void traverse(Vertex v, int level) {
		if (v == null) {
			return;
		}
		if (treeByLevel.size() == level) {
			treeByLevel.add(new ArrayList<Vertex>());
		}
		treeByLevel.get(level).add(v);
		for (Vertex vert : v.children) {
			traverse(vert, level + 1);

		}
	}

	public void makeTree(Production production) {
        Queue<Production> queue = new LinkedList<>();
        queue.add(production);
        int level = 1;
        while (!queue.isEmpty()) {
            List<Production> l = new LinkedList<>(queue);
            queue.clear();
            
            int size = 0;
            for (Production p : l) {
                size += p.vertex.children.length;
            }
            
            exectuor.beginStage(size);
            for (Production p : l) {
                for (Vertex child: p.vertex.children) {
                    VertexType type;
                    if (level < Stuff.treeHeight - 1) {
                        type = VertexType.INTERNAL;
                    } else if (level == Stuff.treeHeight - 1) {
                        type = VertexType.LEAF_PARENT;
                    } else {
                        type = VertexType.LEAF;
                    }
                    TreeProduction prod = new TreeProduction(child, type);
                    exectuor.submitProduction(prod);
                    queue.add(prod);
                }
            }
            exectuor.waitForEnd();
            ++level;
            System.out.println("koniec while, level: " + level);
        }
    }

	public static void main(String[] args) {
		Application a = new Application();
		Vertex v = new Vertex(null);
		a.exectuor.beginStage(1);
		TreeProduction production = new TreeProduction(v, VertexType.ROOT);
		a.exectuor.submitProduction(production);
		a.exectuor.waitForEnd();
		a.makeTree(production);
		a.exectuor.shutdown();
		a.traverse(v, 0);
		int size = a.leaves().size();
		double [][][]matrices = new double[size][][];
		double [][]vectors = new double[size][];
		
		for(int i=0;i<size;++i){
			matrices[i] = a.leaves().get(i).A;
			MatrixUtil.printMatrix(matrices[i]);
			vectors[i] = a.leaves().get(i).b;
		}
		double [][] A = new double[matrices.length-1+matrices[0].length][matrices.length-1+matrices[0].length];

		MatrixUtil.glueMatrixes(matrices, 1, 0, A);
		double [] b = MatrixUtil.glueVectors(vectors);
		MatrixUtil.printMatrix(A);
		double []solution = MatrixUtil.gaussianElimination(A, b);
		MatrixUtil.printVector(solution);
		System.out.println();
		MatrixUtil.printVector(MatrixUtil.multiplyMatrixVectorLeft(A, solution));

	}

}
