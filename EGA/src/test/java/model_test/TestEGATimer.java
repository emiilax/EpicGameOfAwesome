package model_test;

import static org.junit.Assert.*;
import model.EGATimer;

import org.junit.Before;
import org.junit.Test;

public class TestEGATimer {
	private EGATimer tester;
	
	@Before
	public void before(){
		tester = EGATimer.getTimer();
	}
	
	@Test
	public void timePassedShouldIncrementWhenTimerIsActive() throws InterruptedException {
		tester.startTimer();
		Thread.sleep(100);
		float initialTimePassed = tester.getTimePassed();
		Thread.sleep(100);
		tester.stopTimer();
		float finalTimePassed = tester.getTimePassed();
		assertTrue(initialTimePassed < finalTimePassed);
	}
	
	@Test
	public void timePassedShouldBeGreaterWhenTimerRunsLonger() throws InterruptedException{
		tester.startTimer();
		Thread.sleep(1000);
		float firstTimePassed = tester.getTimePassed();
		tester.stopTimer();	
		
		tester.startTimer();
		Thread.sleep(500);
		float secondTimePassed = tester.getTimePassed();
		tester.stopTimer();
			
		assertTrue(firstTimePassed > secondTimePassed);
	}
	
	@Test
	public void testResumeTimer() throws InterruptedException{
		tester.startTimer();
		Thread.sleep(500);
		tester.stopTimer();
		float firstTimePassed = tester.getTimePassed();
		
		tester.resumeTimer();
		Thread.sleep(100);
		tester.stopTimer();
		float secondTimePassed = tester.getTimePassed();
		
		assertTrue(firstTimePassed < secondTimePassed);
	}
	
	@Test
	public void testSetPosition(){
		tester.setPosition(150, 250);
		
		assertTrue(tester.getXPosition() == 150 && tester.getYPosition() == 250);
	}

}
