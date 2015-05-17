package model;

import java.util.Observable;

import lombok.Data;

/**
 * 
 * @author Emil Axelsson
 *
 */
@Data
public abstract class EntityModel extends Observable{
	
	
	private float xPosition;
	private float yPosition;
	
	public void setPosition(float xPos, float yPos){
		xPosition = xPos;
		yPosition = yPos;
		
		
		setChanged();
		notifyObservers();
	}
	
}
