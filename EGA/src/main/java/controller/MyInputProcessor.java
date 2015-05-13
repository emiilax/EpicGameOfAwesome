package controller;

import model.GameData;
import model.MyInput;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

import event.EventSupport;

public class MyInputProcessor extends InputAdapter{
	private static int pressed;
	
	
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
		/*if((x > 460 && x < 581) && (y > 269 && y < 316)){
			EventSupport.getInstance().fireNewEvent("menuItem", x, y);
			EventSupport.getInstance().fireNewEvent("startLevel");
		}
		if((x > 460 && x < 811) && (y > 338 && y < 376)){
			EventSupport.getInstance().fireNewEvent("levelSelect");
		}
		if((x > 460 && x < 689) && (y > 374 && y < 457)){
			EventSupport.getInstance().fireNewEvent("setting");
		}
		if((x > 460 && x < 568) && (y > 477 && y < 516)){
			EventSupport.getInstance().fireNewEvent("quit");
		}*/
		EventSupport.getInstance().fireNewEvent("selectMenuItem", x, y);
		
		return true;
	}
	
	@Override
	public boolean mouseMoved(int x, int y){
		EventSupport.getInstance().fireNewEvent("currentMenuItem", x, y);
		return true;
	}

}
