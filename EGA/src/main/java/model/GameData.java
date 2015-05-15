package model;

import java.io.Serializable;
import java.util.HashMap;

import com.badlogic.gdx.Input.Keys;

import lombok.Data;

/**
 * 
 * @author Erik Karlvist 
 *
 */
@Data
public class GameData implements Serializable {
	
	public int left = Keys.LEFT;
	public int right = Keys.RIGHT;
	public int up = Keys.UP;
	public int down = Keys.DOWN;
	public int enter = Keys.ENTER;
	public int pause = Keys.P;
	public int restart = Keys.R;
	public int escape = Keys.ESCAPE;
	
	
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
