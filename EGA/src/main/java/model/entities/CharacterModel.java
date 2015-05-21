package model.entities;

import java.util.Observable;

import view.entities.CharacterView;
import lombok.*;


public class CharacterModel extends EntityModel {
	
	@Setter @Getter private float xVelocity;
	@Setter @Getter private float yVelocity;
	
	@Setter @Getter private float speed;
	@Setter @Getter private float force;
	
	@Setter @Getter private boolean isBig;
	
	public CharacterModel(){}
	
	public void setVelocity(float xVel, float yVel){
		xVelocity = xVel;
		yVelocity = yVel;
		
		setChanged();
		notifyObservers();
	}

	
}
