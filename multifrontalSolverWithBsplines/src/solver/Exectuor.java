package solver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import solver.grammar.Production;

public class Exectuor {
	
	private ExecutorService executorService;
	private CountDownLatch countDownLatch;
	
	public Exectuor(){
		this.executorService = Executors.newFixedThreadPool(4);
	}
	
	public void beginStage(int productionCount){
		countDownLatch = new CountDownLatch(productionCount);
	}
	
	public void waitForEnd(){
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void submitProduction(Production production){
		production.setLatch(countDownLatch);
		executorService.submit(production);
	}
	
	public void shutdown(){
		executorService.shutdown();
		try {
			executorService.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

}
