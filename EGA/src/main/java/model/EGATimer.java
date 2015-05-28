package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.Timer;

import lombok.Data;

/**
 * A class that handles the timer in the game. Uses singleton pattern.
 * The time is stored in seconds with two decimals.  
 * @author Erik
 *
 */
@Data
public class EGATimer extends Observable implements ActionListener {
	private float xPosition;
	private float yPosition;
	
	private Timer timer;
	private static EGATimer myTimer;
	private float timePassed;
	
	private int interval = 0;
	
	private EGATimer(){
		timer = new Timer(10,this);
	}
	
	/**
	 * If EGATimer has not been initialized, create a new timer.  
	 * @return The timer
	 */
	public static EGATimer getTimer(){
		if(myTimer == null) myTimer = new EGATimer();
		return myTimer;
	}
	
	/**
	 * Starts the timer
	 */
	public void startTimer(){
		timePassed = 0;
		timer.start();
	}
	
	/**
	 * Stops the timer
	 */
	public void stopTimer(){
		timer.stop();
	}
	
	/**
	 * Resumes the timer
	 */
	public void resumeTimer(){
		timer.start();
	}
	
	/**
	 * Called every 10 millisecond add increments the time passed. 
	 */
	public void actionPerformed(ActionEvent e) {
		timePassed++;
	}
	
	/**
	 * @return Time passed
	 */
	public float getTimePassed(){
		return timePassed/100;
	}

	public void setPosition(float x, float y) {
		xPosition = x;
		yPosition = y;	
		
		setChanged();
		notifyObservers();		
	}
}
