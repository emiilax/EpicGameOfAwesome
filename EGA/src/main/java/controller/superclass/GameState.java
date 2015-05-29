package controller.superclass;


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
	
	//protected GameStateManager gsm;
	
	
	protected SpriteBatch sb;
	private OrthographicCamera cam; 
	private OrthographicCamera hudCam;
	
	/*protected GameState(GameStateManager gsm){
		this.gsm = gsm;
		game = gsm.getGame();
		sb = game.getSb();
		cam = game.getCam();
		cam.setToOrtho(false, EGA.V_WIDTH, EGA.V_HEIGTH);
		
		hudCam = game.getHudCam();
		hudCam.setToOrtho(false, EGA.V_WIDTH, EGA.V_HEIGTH);
	}*/
	
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
	
	
	public void perform(TheEvent evt){};
	
}
