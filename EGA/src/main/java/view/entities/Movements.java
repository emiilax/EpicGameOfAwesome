package view.entities;

import lombok.Data;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 
 * @author Rebecka Reitmaier, Emil Axelsson
 *
 * Class that are used for animations. (Character running for example)
 */
@Data
public class Movements {
	
	/** The array or textures that should be looped*/
	private TextureRegion[] frames;
	
	/** */
	private float time;
	
	/** How fast the textures should change */
	private float delay;
	
	/** The current frame */
	private int currentFrame;
	
	/** How many times the texture region have looped*/
	private int timesPlayed;
	
	public Movements() {}
	
	/** 
	 * Constructor that sets the frame and a delay of 
	 * 1/12.
	 */
	public Movements(TextureRegion[] frames) {
		this(frames, 1/ 12f);
	}
	
	/** 
	 * Constructor that sets the frame and a specific delay
	 */
	public Movements(TextureRegion[] frames, float delay){
		setFrames(frames, delay);
	}
	
	/**
	 * Method that sets the frames
	 * @param frames, the array with textures that should be looped
	 * @param delay, how fast it should loop
	 */
	public void setFrames(TextureRegion[] frames, float delay){
		this.frames = frames;
		this.delay = delay;
		time = 0;
		currentFrame = 0;
		timesPlayed = 0;
	}
	
	/**
	 * Updates the movements. 
	 * @param dt, the update speed
	 */
	public void update(float dt){
		if(delay <= 0) return;
		time += dt;
		while(time >= delay){
			step();
		}
	}
	
	/**
	 * Steps the current frame. Changes when update calls on it.
	 */
	public void step(){
		time -= delay;
		currentFrame++;
		if(currentFrame == frames.length){
			currentFrame = 0;
			timesPlayed++;
		}
	}
	
	/**
	 * @return the current texture region
	 */
	public TextureRegion getFrame(){ return frames[currentFrame];}
	
	
}
