package handlers;

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
		return true;
	}

}
