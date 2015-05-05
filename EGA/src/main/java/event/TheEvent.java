package event;

import java.util.EventObject;
import lombok.*;

@Data
public class TheEvent extends EventObject {
	
	private String nameOfEvent;
	
	
	public TheEvent(Object source) {
		super(source);
		this.nameOfEvent = "";
	}
	
	
	public TheEvent(Object source, String nameOfEvent) {
		super(source);
		this.nameOfEvent = nameOfEvent;
	}
	
	
	public String getNameOFEvent(){
		return nameOfEvent;
	}
	
}
