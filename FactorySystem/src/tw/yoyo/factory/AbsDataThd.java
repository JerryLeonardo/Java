package tw.yoyo.factory;

public abstract class AbsDataThd implements Runnable {
	
	protected int sleepPeriod = 10000;
	protected int showTimes = 1000;
	public boolean runFlag = true;

}
