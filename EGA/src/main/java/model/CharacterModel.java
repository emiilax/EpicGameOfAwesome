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
	
	public CharacterModel(){}
	
	public void setVelocity(float xVel, float yVel){
		xVelocity = xVel;
		yVelocity = yVel;
		
		setChanged();
		notifyObservers();
	}
	
}
