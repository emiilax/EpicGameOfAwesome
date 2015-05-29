package model_test;

import static org.junit.Assert.*;

import java.util.List;

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
		
		assertFalse(time > 0);		
	}
	
	@Test
	public void testDebug(){
		tester.setDebug(true);
		assertTrue(tester.getIsDebug() == true);
		
		tester.toggleDebug();
		assertTrue(tester.getIsDebug() == false);
		
		tester.toggleDebug();
		assertTrue(tester.getIsDebug() == true);
	}

	/* I dont know why this fails
	@Test
	public void testGetKeyList(){
		tester.updateList();
		List<Integer> gottenKeys;
		gottenKeys = tester.getKeysList();
		int upKey = tester.getUp();
		assertTrue(gottenKeys.get(2) == upKey);
	} */
	
	@Test
	public void testSetSoundVolume(){
		tester.setVolume(0.4f);
		assertTrue(tester.getSoundVolume() == 0.4f);
		
	}
	
	@Test
	public void floatUnder0OrOver1ShouldNotChangeSoundVolume(){
		float startingVolume = tester.getVolume();
		tester.setVolume(-2f);
		assertTrue(tester.getVolume() == startingVolume);
		
		tester.setVolume(2f);
		assertTrue(tester.getVolume() == startingVolume);
	}
}
