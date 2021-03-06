package event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Emil Axelsson
 * 
 * A singelton class that will be used to 
 * fire events. When a event is fired, it 
 * message the classes that are listeners.
 */
public class EventSupport {
	
	/** The instance */
	private static EventSupport instance = null;
	
	/** The list that will be holding the listeners */
	private List<TheChangeListener> theListeners;
	
	/**
	 * Private constructor that will initiate the list theListeners. 
	 * 
	 */
	private EventSupport(){
		theListeners = new ArrayList<TheChangeListener>();
	}
	
	/**
	 * The method that returns the singelton object. If the 
	 * instance is = null, it will construvt a new instance
	 * 
	 * @return the instance
	 */
	public synchronized static EventSupport getInstance(){
		if(instance == null){
			instance = new EventSupport();
		}
		return instance;
	}
	
	/**
	 * Adds a listener
	 * 
	 * @param listener, the listener that should be added
	 */
	public synchronized void addListner(TheChangeListener listener){
		theListeners.add(listener);
	}
	
	/**
	 * Removes a listener
	 * 
	 * @param listener, the listener that should be removed
	 */
	public synchronized void removeListner(TheChangeListener listener){
		theListeners.remove(listener);

	}
	
	/**
	 * The method that create the event and 
	 * message the listeners
	 * 
	 * @param nameOfEvent, the name of the event
	 */
	public void fireNewEvent(String nameOfEvent){
		int x = -1;
		int y = -1;
		fireNewEvent(nameOfEvent, x, y);
	}
	
	/**
	 * Fire events with a name and x, y. (Used for mouse events)
	 * 
	 * @param nameOfEvent, the name
	 * @param x, x-position
	 * @param y, y-position
	 */
	public void fireNewEvent(String nameOfEvent, int x, int y){
		TheEvent event = null;
		
		event = new TheEvent(this, nameOfEvent, x, y);
		
		Iterator<TheChangeListener> i = theListeners.iterator();
		
		while(i.hasNext()){
			((TheChangeListener) i.next()).eventRecieved(event);
		}
	}
	
	/**
	 * Fire events with a name and a level number
	 * 
	 * @param nameOfEvent, the name
	 * @param theLevel, the level number
	 */
	public void fireNewEvent(String nameOfEvent, int theLevel){
		TheEvent event = null;
		
		event = new TheEvent(this, nameOfEvent, theLevel);
		
		Iterator<TheChangeListener> i = theListeners.iterator();
		
		while(i.hasNext()){
			((TheChangeListener) i.next()).eventRecieved(event);
		}
	}
	
	/**
	 * Fire events with a name and boolean for if the mouse should be active or not
	 * 
	 * @param nameOfEvent, the name
	 * @param active, if active
	 */
	public void fireNewEvent(String nameOfEvent, boolean active){
		TheEvent event = null;
		
		event = new TheEvent(this, nameOfEvent, active);
		
		Iterator<TheChangeListener> i = theListeners.iterator();
		
		while(i.hasNext()){
			((TheChangeListener) i.next()).eventRecieved(event);
		}
	}
	
}
