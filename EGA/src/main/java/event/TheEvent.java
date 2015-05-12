package event;

import java.util.EventObject;

import lombok.*;

/**
 * 
 * @author emil axelsson
 * 
 * Class for the event that will be fired in EventSupport.
 */
@Data
public class TheEvent extends EventObject {
	
	/** Instacevariable that contains the name of event */
	private String nameOfEvent;
	
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
	
	public TheEvent(Object source, String nameOfEvent, int x, int y){
		TheEvent(source, nameOfEvent);
	}
	

}
