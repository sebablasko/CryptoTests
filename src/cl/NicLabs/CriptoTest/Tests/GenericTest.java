package cl.NicLabs.CriptoTest.Tests;

public abstract class GenericTest {
	
	// Experiment General Values Recorded
	public String startTime, endTime;
	public long timeStampStart, timeStampEnd;
	
	// Methods to be implemented
	abstract public void run() throws Exception;
	
	abstract protected void saveResults();
	
}
