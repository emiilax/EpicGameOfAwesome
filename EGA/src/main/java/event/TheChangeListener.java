package event;

/**
 * interface that all of the event-listeners implements
 * @author Emil Axelsson
 *
 */
public interface TheChangeListener{
	
	/**
	 * Called when a event happens
	 * @param evt, the event
	 */
	public void eventRecieved(TheEvent evt);
	
}
