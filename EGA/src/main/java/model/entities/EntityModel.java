package model.entities;

import java.util.Observable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Superclass for the entity models
 * 
 * @author Emil Axelsson
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class EntityModel extends Observable{
	/** The x-position */
	private float xPosition;
	
	/** The y-position */
	private float yPosition;
	
	/** Sets the x- and y- positions and notyfy the observers */
	public void setPosition(float xPos, float yPos){
		xPosition = xPos;
		yPosition = yPos;	
		
		setChanged();
		notifyObservers();
	}
	
}
