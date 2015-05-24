package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	private float soundVolume = 0.6f;

	private HashMap<Integer, Float> times;

	private List<Integer> keys;

	public GameData(){
		times = new HashMap<Integer, Float>();
		keys = new ArrayList<Integer>();
		updateList();
	}

	public float getVolume(){
		return soundVolume;
	}

	public void setVolume(float f){
		if(f <= 1f && f >= 0){
			soundVolume = f;
		}
	}

	public void updateList(){
		keys.clear();
		keys.add(enter);
		keys.add(up);
		keys.add(down);
		keys.add(left);
		keys.add(right);
		keys.add(pause);
		keys.add(restart);
		keys.add(escape);
	}

	public List<Integer> getKeysList(){
		return keys;
	}

	public void addTime(int level, Float time){
		if(isBetterTime(level, time)){
			times.put(level, time);
		} 
	}

	public boolean isBetterTime(int level, Float time){
		if(times.containsKey(level)){
			return times.get(level).equals(time)|| times.get(level)> time;
		} else {
			return true;
		}
	}

	public float getTime(int level){
		return times.get(level);
	}
}
