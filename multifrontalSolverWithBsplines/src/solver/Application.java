package solver;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import solver.grammar.BackwardSubstitution;
import solver.grammar.CombineChildrenProducion;
import solver.grammar.EliminationProduction;
import solver.grammar.Production;
import solver.grammar.SolveRootProduction;
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
				for (Vertex child : p.vertex.children) {
					VertexType type;
					if (level < Stuff.treeHeight - 2) {
						type = VertexType.INTERNAL;
					} else if (level == Stuff.treeHeight - 2) {
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
		Vertex v = new Vertex(null, 0, Stuff.leafCount);
		a.exectuor.beginStage(1);
		TreeProduction production = new TreeProduction(v, VertexType.ROOT);
		a.exectuor.submitProduction(production);
		a.exectuor.waitForEnd();
		a.makeTree(production);
		a.traverse(v, 0);

		for (int it = 0; it < 10000; ++it) {
			for(Vertex vert : a.leaves()){
				vert.generateMatrix();

			}
			for (int i = a.height() - 2; i > 0; --i) {

				a.exectuor.beginStage(a.levelSize(i));
				for (Vertex node : a.level(i)) {

					int interval = node.type.equals(VertexType.LEAF_PARENT) ? 1
							: Stuff.p;
					int ommitCount = i == a.height() - 2 ? 0
							: i == a.height() - 3 ? 1 : Stuff.p;

					CombineChildrenProducion p = new CombineChildrenProducion(
							node, interval, ommitCount);
					a.exectuor.submitProduction(p);

				}
				a.exectuor.waitForEnd();

				System.out.println("combined level: " + i);
				a.exectuor.beginStage(a.levelSize(i));
				for (Vertex node : a.level(i)) {

					EliminationProduction p = new EliminationProduction(node);
					a.exectuor.submitProduction(p);

				}
				a.exectuor.waitForEnd();
				System.out.println("eliminated level :" + i);
			}

			a.exectuor.beginStage(1);
			Production prod = new CombineChildrenProducion(a.root(), Stuff.p,
					Stuff.p);
			a.exectuor.submitProduction(prod);
			a.exectuor.waitForEnd();

			a.exectuor.beginStage(1);
			prod = new SolveRootProduction(a.root());
			a.exectuor.submitProduction(prod);
			a.exectuor.waitForEnd();

			System.out.println("root solved");

			for (int i = 0; i < a.height(); ++i) {
				a.exectuor.beginStage(a.levelSize(i));
				for (Vertex node : a.level(i)) {
					BackwardSubstitution bs = new BackwardSubstitution(node);
					a.exectuor.submitProduction(bs);

				}
				a.exectuor.waitForEnd();
				System.out.println("backward substitutuion done for level: "
						+ i);

			}
			

			double[] result = new double[a.leafCount() + Stuff.p];

			int count = 0;
			for (Vertex node : a.leaves()) {
				result[count] = node.x[0];
				++count;

			}
			for (int i = 1; i < a.leaves().get(a.leaves().size() - 1).x.length; ++i) {
				result[count] = a.leaves().get(a.leaves().size() - 1).x[i];
				++count;
			}
			MatrixUtil.printVector(result);
			System.out.println();

			Bspline spline = new Bspline(Stuff.knotVector, Stuff.p);

			int s = 1000;
			double[] x = new double[s];
			double[] y = new double[s];

			double cus = 0.0;
			for (int i = 0; i < s; ++i) {
				x[i] = cus;
				cus += 1.0 / s;
				y[i] = spline.evaluate(x[i], result);

			}

			if(it%100==0){
				ResultPrinter.printResult(x, y);

			}
		}
		a.exectuor.shutdown();

	}

}
