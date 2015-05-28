package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.Timer;


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
	
	public static EGATimer getTimer(){
		if(myTimer == null) myTimer = new EGATimer();
		return myTimer;
	}
	
	public void startTimer(){
		timePassed = 0;
		timer.start();
	}
	
	public void stopTimer(){
		timer.stop();
	}
	
	public void resumeTimer(){
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		timePassed++;
	}
	
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
