package controller;

import model.MyInput;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

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
		return true;
	}

}
