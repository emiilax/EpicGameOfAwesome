package controller;

import model.GameData;
import model.MyInput;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

import event.EventSupport;

public class MyInputProcessor extends InputAdapter{
	private static int pressed;
	private static boolean active = true;

	public static void setActive(boolean act){
		active = act;
	}


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
			MyInput.setKey(MyInput.BUTTON_ESCAPE, true);
		}
		if(k == gd.pause){
			EventSupport.getInstance().fireNewEvent("pause");
		}
		pressed = k;
		return true;
	}

	public static int getPressed(){
		return pressed;
	}

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
		return true;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button){
		if(active){
			EventSupport.getInstance().fireNewEvent("selectMenuItem", x, y);

			return true;
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y){
		if (active){
			EventSupport.getInstance().fireNewEvent("currentMenuItem", x, y);
			return true;
		} 
		return false;
	}

}
