package controller;

import io.SaveHandler;
import io.Content;
import model.GameData;
import model.MyInput;

import com.badlogic.gdx.InputAdapter;

import event.EventSupport;
/**
 *  Class that detect it mouse or button is clicked
 *  
 * @author Emil Axelsson
 * 
 */
public class MyInputProcessor extends InputAdapter{
	/** The key that have been clicked*/
	private static int pressed;
	
	/** If true, handle mouse input */
	private static boolean active = true;
	
	/**
	 * changes active, a boolean that determines if mouse movement 
	 * should be handled.
	 * @param act
	 */
	public static void setActive(boolean act){
		active = act;
	}
	
	/**
	 * Called when a button is down
	 */
	@Override
	public boolean keyDown(int k){
		GameData gd = SaveHandler.getGameData();
		if(k == gd.right){
			MyInput.setKey(MyInput.BUTTON_FORWARD, true);
		}

		if(k == gd.left){
			MyInput.setKey(MyInput.BUTTON_BACKWARD, true);
		}
		if(k == gd.up){
			MyInput.setKey(MyInput.BUTTON_JUMP, true);
		}

		if(k == gd.down){
			MyInput.setKey(MyInput.BUTTON_DOWN, true);
		}

		if(k == gd.enter){
			MyInput.setKey(MyInput.BUTTON_ENTER, true);
		}if(k == gd.restart){
			MyInput.setKey(MyInput.BUTTON_RESTART, true);
		} 
		if(k == gd.escape){
			Content.getInstance().stopAllSounds();
			MyInput.setKey(MyInput.BUTTON_ESCAPE, true);
		}
		if(k == gd.pause){
			Content.getInstance().stopAllSounds();
			MyInput.setKey(MyInput.BUTTON_PAUSE, true);
		}
		EventSupport.getInstance().fireNewEvent("latestPressed", k);
		return true;
	}
	
	/**
	 * 
	 * @return pressed, the key that has been pressed
	 */
	public static int getPressed(){
		return pressed;
	}
	/**
	 * Called when a button is released
	 */
	@Override
	public boolean keyUp(int k){
		GameData gd = SaveHandler.getGameData();
		if(k == gd.right){
			MyInput.setKey(MyInput.BUTTON_FORWARD, false);
		}
		if(k == gd.left){
			MyInput.setKey(MyInput.BUTTON_BACKWARD, false);
		}
		if(k == gd.up){
			MyInput.setKey(MyInput.BUTTON_JUMP, false);
		}
		if(k == gd.down){
			MyInput.setKey(MyInput.BUTTON_DOWN, false);
		} 
		if(k == gd.enter){
			MyInput.setKey(MyInput.BUTTON_ENTER, false);
		}
		if(k == gd.restart){
			MyInput.setKey(MyInput.BUTTON_RESTART, false);
		} 
		if(k == gd.escape){
			MyInput.setKey(MyInput.BUTTON_ESCAPE, false);
		}
		if(k == gd.pause){
			MyInput.setKey(MyInput.BUTTON_PAUSE, false);
		}
		return true;
	}

	/**
	 * handles mouse presses
	 */
	@Override
	public boolean touchDown(int x, int y, int pointer, int button){
		if(active){
			EventSupport.getInstance().fireNewEvent("selectMenuItem", x, y);
			return true;
		}
		return false;
	}

	/**
	 * handles mouse movement
	 */
	@Override
	public boolean mouseMoved(int x, int y){
		if (active){
			EventSupport.getInstance().fireNewEvent("currentMenuItem", x, y);
			return true;
		} 
		return false;
	}
}
