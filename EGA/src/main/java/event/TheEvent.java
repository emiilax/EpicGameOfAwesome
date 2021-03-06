package event;

import java.util.EventObject;

import lombok.*;

/**
 * 
 * @author Emil Axelsson
 * 
 * Class for the event that will be fired in EventSupport.
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper=false)
public class TheEvent extends EventObject {
	
	/** Instacevariable that contains the name of event */
	private String nameOfEvent;
	private int x, y;
	private int theLevelNumber;
	private boolean active;

	
	/**
	 * The constructor for the event
	 * 
	 * @param source, the class that fire the event
	 * @param nameOfEvent, the name of the event
	 */
	public TheEvent(Object source, String nameOfEvent) {
		super(source);
		this.nameOfEvent = nameOfEvent;
	}
	
	public TheEvent(Object source, String nameOfEvent, boolean active) {
		super(source);
		this.nameOfEvent = nameOfEvent;
		this.active = active;
	}
	
	public TheEvent(Object source, String nameOfEvent, int x, int y){
		this(source, nameOfEvent);
		this.x = x;
		this.y = y;
	}
	
	public TheEvent(Object source, String nameOfEvent, int theLevel){
		this(source, nameOfEvent);
		this.theLevelNumber = theLevel;
	}

}
