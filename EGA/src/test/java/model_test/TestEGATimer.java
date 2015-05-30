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
	public void timePassedShouldIncrementWhenTimerIsActive() {
		float initialTimePassed = tester.getTimePassed();
		tester.startTimer();
		int i = 0;
		while(i<100){
			i++;
		}
		tester.stopTimer();
		assertTrue(initialTimePassed < tester.getTimePassed());
	}
	
	@Test
	public void timePassedShouldBeGreaterWhenTimerRunsLonger(){
		tester.startTimer();
		int i = 0;
		while(i<2000){
			i++;
		}
		tester.stopTimer();
		float firstTimePassed = tester.getTimePassed();
		
		tester.startTimer();
		int j = 0;
		while(j<500){
			j++;
		}
		float secondTimePassed = tester.getTimePassed();
		
		assertTrue(firstTimePassed > secondTimePassed);
	}

}
