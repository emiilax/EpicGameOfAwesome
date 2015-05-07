package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class EGATimer implements ActionListener {
	
	private Timer timer;
	private static EGATimer myTimer;
	private float timePassed;
	
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

	public void actionPerformed(ActionEvent e) {
		timePassed++;
	}
	
	public float getTimePassed(){
		return timePassed/100;
	}
}
