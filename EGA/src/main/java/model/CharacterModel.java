package model;

import java.util.Observable;

import view.CharacterView;
import lombok.Data;

@Data
public class CharacterModel extends EntityModel {
	
	private float xVelocity;
	private float yVelocity;
	
	private float speed;
	private float force;
	
	private boolean isBig;
	
	//private float xPosition;
	//private float yPosition;
	
	public CharacterModel(){}
	
	public void setVelocity(float xVel, float yVel){
		xVelocity = xVel;
		yVelocity = yVel;
		
		setChanged();
		notifyObservers();
	}
	
	
	
	/*
	public void setPosition(float xPos, float yPos){
		xPosition = xPos;
		yPosition = yPos;
		
		
		setChanged();
		notifyObservers();
	}*/
	/*
	public static void main(String []args){
		CharacterModel chm = new CharacterModel();
		CharacterView chv = new CharacterView();
		
		chm.addObserver(chv);
		
		chm.setPosition(10, 10);
	}*/

	
}
