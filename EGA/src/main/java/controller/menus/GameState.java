package controller.menus;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import controller.EGA;
import event.TheEvent;
import lombok.*;

/**
 * A class used for all different states in a game.
 * Sets camera and view.
 * @author Emil, Erik, Hampus, Rebecca
 *
 */
@Data
public abstract class GameState {
	

	
	protected SpriteBatch sb;
	private OrthographicCamera cam; 
	private OrthographicCamera hudCam;

	
	protected GameState(){
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		hudCam = new OrthographicCamera();
		cam.setToOrtho(false, EGA.V_WIDTH, EGA.V_HEIGTH);
		hudCam.setToOrtho(false, EGA.V_WIDTH, EGA.V_HEIGTH);
	}
	
	/**
	 * This method is called when a button is pressed and handles
	 * input accordingly
	 * @author Emil, Erik, Hampus, Rebecca
	 * @param i the integer value of the key pressed
	 */
	public abstract void handleInput(int i);
	
	/**
	 * Called when the game updates data
	 * @param dt Intervall in which the game updates
	 * @author Emil, Erik, Hampus, Rebecca
	 */
	public abstract void update(float dt);
	
	/**
	 * Is called from the libGDX backend every 60 second and updates
	 * the view
	 * @author Emil, Erik, Hampus, Rebecca
	 */
	public abstract void render();
	
	/**
	 * When a event has been recived, the mainclass call this method
	 * @param evt
	 */
	public void perform(TheEvent evt){};
	
}
