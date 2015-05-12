package controller;

import model.MyInput;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

import event.EventSupport;

public class MyInputProcessor extends InputAdapter{
	
	@Override
	public boolean keyDown(int k){
		if(k == Keys.RIGHT){
			MyInput.setKey(MyInput.BUTTON_FORWARD, true);
		}
		if(k == Keys.LEFT){
			MyInput.setKey(MyInput.BUTTON_BACKWARD, true);
		}
		if(k == Keys.UP){
			MyInput.setKey(MyInput.BUTTON_JUMP, true);
			
		}
		if(k == Keys.DOWN){
			MyInput.setKey(MyInput.BUTTON_DOWN, true);
		} 
		if(k == Keys.NUM_1){
			MyInput.setKey(MyInput.BUTTON_LEVEL1, true);
		}
		if(k == Keys.NUM_2){
			MyInput.setKey(MyInput.BUTTON_LEVEL2, true);
		}
		if(k == Keys.ENTER){
			MyInput.setKey(MyInput.BUTTON_ENTER, true);
		}if(k == Keys.R){
			MyInput.setKey(MyInput.BUTTON_RESTART, true);
		} 
		if(k == Keys.P){
			EventSupport.getInstance().fireNewEvent("pause");
		}
		
		return true;
	}
	
	@Override
	public boolean keyUp(int k){
		if(k == Keys.RIGHT){
			MyInput.setKey(MyInput.BUTTON_FORWARD, false);
		}
		if(k == Keys.LEFT){
			MyInput.setKey(MyInput.BUTTON_BACKWARD, false);
		}
		if(k == Keys.UP){
			MyInput.setKey(MyInput.BUTTON_JUMP, false);
		}
		if(k == Keys.DOWN){
			MyInput.setKey(MyInput.BUTTON_DOWN, false);
		} 
		if(k == Keys.NUM_1){
			MyInput.setKey(MyInput.BUTTON_LEVEL1, false);
		}
		if(k == Keys.NUM_2){
			MyInput.setKey(MyInput.BUTTON_LEVEL2, false);
		}
		if(k == Keys.ENTER){
			MyInput.setKey(MyInput.BUTTON_ENTER, false);
		}
		if(k == Keys.R){
			MyInput.setKey(MyInput.BUTTON_RESTART, false);
		} 
		return true;
	}
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button){
		if((x > 460 && x < 581) && (y > 269 && y < 316)){
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
		}
		return true;
	}
	
	@Override
	public boolean mouseMoved(int x, int y){
		if((x > 460 && x < 581) && (y > 269 && y < 316)){
			EventSupport.getInstance().fireNewEvent("currentMenuItem0");;
		}
		if((x > 460 && x < 811) && (y > 338 && y < 376)){
			EventSupport.getInstance().fireNewEvent("currentMenuItem1");
		}
		if((x > 460 && x < 689) && (y > 374 && y < 457)){
			EventSupport.getInstance().fireNewEvent("currentMenuItem2");
		}
		if((x > 460 && x < 568) && (y > 477 && y < 516)){
			EventSupport.getInstance().fireNewEvent("currentMenuItem3");
		}
		return true;
	}

}
