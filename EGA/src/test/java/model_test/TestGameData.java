package model_test;

import static org.junit.Assert.*;
import model.GameData;

import org.junit.Before;
import org.junit.Test;
/**
 * Test class for GameData
 * 
 * @author Hampus Rönström
 *
 */
public class TestGameData {
	private GameData tester;
	
	@Before
	public void before(){
		tester = new GameData();
	}

	@Test
	public void aSmallerTimeShouldReturnTrue() {
		tester.addTime(0, 16f);
		
		float time = tester.getTime(0);
		assertTrue(tester.isBetterTime(0, time-1));	
		assertFalse(tester.isBetterTime(0, time + 1));
	}
	
	@Test
	public void smallerTimeShouldUpdateTheTime(){		
		tester.addTime(0, 100f);
		float time1 = tester.getTime(0);
		
		tester.addTime(0, 50f);
		float time2 = tester.getTime(0);
		
		assertTrue(time1 == 100f && time2 == 50f);
	}
	
	@Test
	public void biggerTimeShouldNotUpdateTheTime(){
		tester.addTime(0, 100f);
		float time1 = tester.getTime(0);
		
		tester.addTime(0, 150f);
		float time2 = tester.getTime(0);
		
		assertTrue(time1 == 100f && time2 == 100f);	 
	}
	
	/**
	 * Don't know if this method is needed. But I guess if a 
	 * number smaller than or equal to 0 should throw an exception 
	 * or something? So then the test method should have some
	 * "@Test (expected = Exception.class)"
	 */
	@Test
	public void timeSmallerThanOrEqualToZeroSholudNotBeOk(){
		tester.addTime(0, 0f-10f);
		
		float time = tester.getTime(0);
		
		assertTrue(time > 0);		
	}

}
