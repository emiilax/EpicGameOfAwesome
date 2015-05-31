package model.entities;


import lombok.*;

/**
 * The model class for the character 
 * @author Emil Axelsson
 * 
 * 
 */
public class CharacterModel extends EntityModel {
	
	/** The x-velocity */
	@Setter @Getter private float xVelocity;
	
	/** The x-velocity */
	@Setter @Getter private float yVelocity;
	
	public CharacterModel(){}
	
	/** Sets the current velocity and notify observers*/
	public void setVelocity(float xVel, float yVel){
		xVelocity = xVel;
		yVelocity = yVel;
		
		setChanged();
		notifyObservers();
	}

	
}
