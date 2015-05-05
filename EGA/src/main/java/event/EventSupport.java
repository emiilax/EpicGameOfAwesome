package event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EventSupport {
	
	private static EventSupport instance = null;
	
	private List<TheChangeListener> pcs;
	
	private EventSupport(){
		pcs = new ArrayList<TheChangeListener>();
	}
	
	
	public static EventSupport getInstance(){
		if(instance == null){
			instance = new EventSupport();
		}
		return instance;
	}
	
	public synchronized void addListner(TheChangeListener listener){
		pcs.add(listener);
	}
	
	public synchronized void removeListner(TheChangeListener listener){
		pcs.remove(listener);

	}
	
	public void fireNewEvent(String nameOfEvent){
		TheEvent event = null;
		
		event = new TheEvent(this, nameOfEvent);
		
		Iterator<TheChangeListener> i = pcs.iterator();
		
		while(i.hasNext()){
			((TheChangeListener) i.next()).eventRecieved(event);
		}
	}
}
