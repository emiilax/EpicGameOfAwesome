package model;

public class CharacterModel {
	
	private float yVelocity;
	private float xVelocity;
	
	
	private float speed;
	private float force;
	
	private boolean isBig;
	
	public void setForce(int theInput){
		
		//getCurrentSpeed();
		
		switch(theInput){
			case -1: xVelocity = 0;
			break;
			
			case MyInput.BUTTON_FORWARD: xVelocity = speed;
			break;
			
			case MyInput.BUTTON_BACKWARD: xVelocity = -speed;
			break;
			
			case MyInput.BUTTON_JUMP: yVelocity = force;
			break;
		}
		
	}
	
	public void setIsBig(boolean isBig){
		this.isBig = isBig;
		
		if(isBig){
			force = 350;
			speed = 1.5f;
		}else{
			force = 250;
			speed = 2.5f;
		}
	}
	
	
	

}
