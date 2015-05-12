package model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * @author Erik Karlvist 
 *
 */

public class GameData implements Serializable {

	private HashMap<Integer, Float> times;

	public GameData(){
		times = new HashMap<Integer, Float>();
	}

	public void addTime(int level, Float time){
		if(isBetterTime(level, time)){
			times.put(level, time);
		} 
	}

	public boolean isBetterTime(int level, Float time){
		if(times.containsKey(level)){
			return times.get(level) > time;
		} else {
			return true;
		}
	}

	public float getTime(int level){
		return times.get(level);
	}
}
