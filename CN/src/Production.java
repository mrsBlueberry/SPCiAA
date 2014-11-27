import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @(#)Production.java
 * 
 * 
 * @author
 * @version 1.00 2011/6/13
 */

abstract class Production extends Thread {

	// vertex where the production will be applied
	Vertex m_vertex;
	// graph drawer
	GraphDrawer m_drawer;
	// productions counter
	CyclicBarrier m_barrier;
	public static double h;
	public static double dt = 0.0666;
	public static double alpha = 1.0/2.0;
	public static double beta = 1.0 - alpha;

	Production(Vertex Vert, CyclicBarrier barrier) {
		m_vertex = Vert;
		m_barrier = barrier;
		m_drawer = new GraphDrawer();
	}

	// returns first vertex from the left
	abstract Vertex apply(Vertex v);

	// run the thread
	public void run() {
		//apply the production
		m_vertex = apply(m_vertex);
		//plot the graph
		m_drawer.draw(m_vertex);
		try {
			m_barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class P1 extends Production {
	P1(Vertex vert, CyclicBarrier barrier) {
		super(vert, barrier);
	}

	Vertex apply(Vertex S) {
//		System.out.println("p1");
		Vertex T1 = new Vertex(null, null, S, "T");
		Vertex T2 = new Vertex(null, null, S, "T");
		S.set_left(T1);
		S.set_right(T2);
		S.set_label("root");
		return S;
	}
}

class P2 extends Production {
	P2(Vertex vert, CyclicBarrier barrier) {
		super(vert, barrier);
	}

	Vertex apply(Vertex vert) {
//		System.out.println("p2");
		Vertex t1 = new Vertex(null, null, vert, "T");
		Vertex t2 = new Vertex(null, null, vert, "T");
		vert.set_left(t1);
		vert.set_right(t2);
		vert.set_label("int");
		return vert;
	}
}

class P3 extends Production {
	P3(Vertex vert, CyclicBarrier barrier) {
		super(vert, barrier);
	}

	Vertex apply(Vertex vert) {
//		System.out.println("p3");
		Vertex t1 = new Vertex(null, null, vert, "node");
		Vertex t2 = new Vertex(null, null, vert, "node");
		vert.set_left(t1);
		vert.set_right(t2);
		vert.set_label("int");
		return vert;
	}
}

class A extends Production {

	A(Vertex vert, CyclicBarrier barrier) {
		super(vert, barrier);
	}

	@Override
	Vertex apply(Vertex vert) {
//		System.out.println("A");
		vert.m_a[1][1] = h/3.0 + dt * alpha * 1.0 / h;
		vert.m_a[2][1] = h/6.0 - dt * alpha * 1.0 / h;
		vert.m_a[1][2] = h/6.0 - dt * alpha * 1.0 / h;
		vert.m_a[2][2] = h/3.0 + dt * alpha * 1.0 / h;
		// copying values to m_x_old for next iteration
		vert.m_x_old[0] = vert.m_x[0];
		vert.m_x_old[1] = vert.m_x[1];
		vert.m_x_old[2] = vert.m_x[2];
		vert.m_b[1] = (h / 3.0 - dt * beta * 1.0 / h) * vert.m_x_old[1]
				+ (h / 6.0 + dt * beta * 1.0 / h) * vert.m_x_old[2];
		vert.m_b[2] = (h / 6.0 + dt * beta * 1.0 / h) * vert.m_x_old[1]
				+ (h / 3.0 - dt * beta * 1.0 / h) * vert.m_x_old[2];
		return vert;
	}

}

class A1 extends Production {
	A1(Vertex vert, CyclicBarrier barrier) {
		super(vert, barrier);
	}

	Vertex apply(Vertex vert) {
//		System.out.println("A");
		vert.m_a[1][1] = h / 3.0 + dt * alpha * (1.0 / h + 1.0);
		vert.m_a[2][1] = h / 6.0 - dt * alpha * 1.0 / h;
		vert.m_a[1][2] = h / 6.0 - dt * alpha * 1.0 / h;
		vert.m_a[2][2] = h / 3.0 + dt * alpha * (1.0 / h + 1);
		//copying values to m_x_old for next iteration
		vert.m_x_old[0] = vert.m_x[0];
		vert.m_x_old[1] = vert.m_x[1];
		vert.m_x_old[2] = vert.m_x[2];
		vert.m_b[1] = (h / 3.0 - dt * beta * (1.0 / h + 1)) * vert.m_x_old[1]
				+ (h / 6.0 + dt * beta * 1.0 / h) * vert.m_x_old[2] + dt;
		vert.m_b[2] = (h / 6.0 + dt * beta * 1.0 / h) * vert.m_x_old[1]
				+ (h / 3.0 - dt * beta * 1 / h) * vert.m_x_old[2];
		return vert;
	}
}

class AN extends Production {
	AN(Vertex vert, CyclicBarrier barrier) {
		super(vert, barrier);
	}

	Vertex apply(Vertex vert) {
//		System.out.println("AN");
		vert.m_a[1][1] = h/3.0 + dt * alpha * 1.0 / h;
		vert.m_a[2][1] = h/6.0 - dt * alpha * 1.0 / h;
		vert.m_a[1][2] = h/6.0 - dt * alpha * 1.0 / h;
		vert.m_a[2][2] = h / 3.0 + dt * alpha * (1.0 / h + 1);
		// copying values to m_x_old for next iteration
		vert.m_x_old[0] = vert.m_x[0];
		vert.m_x_old[1] = vert.m_x[1];
		vert.m_x_old[2] = vert.m_x[2];
		vert.m_b[1] = (h / 3.0 - dt * beta * 1 / h) * vert.m_x_old[1]
				+ (h / 6.0 + dt * beta * 1.0 / h) * vert.m_x_old[2];
		vert.m_b[2] = (h / 6.0 + dt * beta * 1.0 / h) * vert.m_x_old[1]
				+ (h / 3.0 - dt * beta * (1.0 / h + 1)) * vert.m_x_old[2] - dt;
		return vert;
	}
}

class A2 extends Production {
	A2(Vertex vert, CyclicBarrier barrier) {
		super(vert, barrier);
	}

	Vertex apply(Vertex vert) {
//		System.out.println("A2");
		vert.m_a[0][0] = vert.m_left.m_a[2][2] + vert.m_right.m_a[1][1];
		vert.m_a[1][0] = vert.m_left.m_a[1][2];
		vert.m_a[2][0] = vert.m_right.m_a[2][1];
		vert.m_a[0][1] = vert.m_left.m_a[2][1];
		vert.m_a[1][1] = vert.m_left.m_a[1][1];
		vert.m_a[2][1] = 0.0;
		vert.m_a[0][2] = vert.m_right.m_a[1][2];
		vert.m_a[1][2] = 0.0;
		vert.m_a[2][2] = vert.m_right.m_a[2][2];
		vert.m_b[0] = vert.m_left.m_b[2] + vert.m_right.m_b[1];
		vert.m_b[1] = vert.m_left.m_b[1];
		vert.m_b[2] = vert.m_right.m_b[2];
		return vert;
	}
}

class E2 extends Production {
	E2(Vertex vert, CyclicBarrier barrier) {
		super(vert, barrier);
	}

	Vertex apply(Vertex vert) {
//		System.out.println("E2");
		vert.m_b[0] /= vert.m_a[0][0];
		vert.m_a[0][2] /= vert.m_a[0][0];
		vert.m_a[0][1] /= vert.m_a[0][0];
		vert.m_a[0][0] /= vert.m_a[0][0];
		vert.m_b[1] -= vert.m_b[0] * vert.m_a[1][0];
		vert.m_a[1][2] -= vert.m_a[0][2] * vert.m_a[1][0];
		vert.m_a[1][1] -= vert.m_a[0][1] * vert.m_a[1][0];
		vert.m_a[1][0] -= vert.m_a[0][0] * vert.m_a[1][0];
		vert.m_b[2] -= vert.m_b[0] * vert.m_a[2][0];
		vert.m_a[2][2] -= vert.m_a[0][2] * vert.m_a[2][0];
		vert.m_a[2][1] -= vert.m_a[0][1] * vert.m_a[2][0];
		vert.m_a[2][0] -= vert.m_a[0][0] * vert.m_a[2][0];
		return vert;
	}
}

class Aroot extends A2 {

	Aroot(Vertex vert, CyclicBarrier barrier) {
		super(vert, barrier);
	}

}

class Eroot extends Production {
	Eroot(Vertex vert, CyclicBarrier barrier) {
		super(vert, barrier);
	}

	Vertex apply(Vertex vert) {
//		System.out.println("Eroot");

		// T.m_x = GaussianElimination.lsolve(T.m_a, T.m_b);

		vert.m_b[1] /= vert.m_a[1][1];
		vert.m_a[1][2] /= vert.m_a[1][1];
		vert.m_a[1][1] /= vert.m_a[1][1];
		vert.m_b[2] -= vert.m_b[1] * vert.m_a[2][1];
		vert.m_a[2][2] -= vert.m_a[1][2] * vert.m_a[2][1];
		vert.m_a[2][1] -= vert.m_a[1][1] * vert.m_a[2][1];
		vert.m_b[2] /= vert.m_a[2][2];
		vert.m_a[2][2] /= vert.m_a[2][2];
		vert.m_b[1] -= vert.m_b[2] * vert.m_a[1][2];
		vert.m_a[1][2] -= vert.m_a[2][2] * vert.m_a[1][2];
		vert.m_b[1] /= vert.m_a[1][1];
		vert.m_a[1][1] /= vert.m_a[1][1];
		vert.m_b[0] -= vert.m_b[2] * vert.m_a[0][2];
		vert.m_a[0][2] -= vert.m_a[2][2] * vert.m_a[0][2];
		vert.m_b[0] -= vert.m_b[1] * vert.m_a[0][1];
		vert.m_a[0][1] -= vert.m_a[1][1] * vert.m_a[0][1];
		vert.m_b[0] /= vert.m_a[0][0];
		vert.m_a[0][0] /= vert.m_a[0][0];

		vert.m_x[2] = vert.m_b[2];// T.m_b[2] / T.m_a[2][2];
		vert.m_x[1] = vert.m_b[1];// (T.m_b[1] - T.m_a[1][2] * T.m_x[2])/T.m_a[1][1];
		vert.m_x[0] = vert.m_b[0];// (T.m_b[0] - T.m_a[0][1] * T.m_x[1] - T.m_a[0][2]
							// * T.m_x[2])/T.m_a[0][0];

		return vert;
	}
}

class BS extends Production {
	BS(Vertex vert, CyclicBarrier barrier) {
		super(vert, barrier);
	}

	@Override
	Vertex apply(Vertex vert) {
//		System.out.println("BS");
		if (vert.m_label.equals("node"))
			return vert;

		vert.m_left.m_x[1] = vert.m_x[1];
		vert.m_left.m_x[2] = vert.m_x[0];

		vert.m_left.m_x[0] = (vert.m_left.m_b[0] - vert.m_left.m_a[0][1]
				* vert.m_left.m_x[1] - vert.m_left.m_a[0][2] * vert.m_left.m_x[2])
				/ vert.m_left.m_a[0][0];

		vert.m_right.m_x[1] = vert.m_x[0];
		vert.m_right.m_x[2] = vert.m_x[2];

		vert.m_right.m_x[0] = (vert.m_right.m_b[0] - vert.m_right.m_a[0][1]
				* vert.m_right.m_x[1] - vert.m_right.m_a[0][2] * vert.m_right.m_x[2])
				/ vert.m_right.m_a[0][0];

		return vert;
	}
}
