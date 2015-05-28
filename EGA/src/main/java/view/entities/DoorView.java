package view.entities;

import io.Content;

import java.util.Observable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import model.entities.EntityModel;

public class DoorView extends EntityView{
	
	private Boolean doorIsLocked;
	
	public DoorView(){
		doorIsLocked = true;
		setTexture();
	}

	/**
	 * Updates the EntityModel with current positions
	 * and calls the render method.
	 * 
	 * @param o, Observable
	 * @param arg, Object 
	 */
	public void update(Observable o, Object arg) {
		if (o instanceof EntityModel) {
			super.setXPosition(((EntityModel) o).getXPosition());
			super.setYPosition(((EntityModel) o).getYPosition());
			setTexture();
			render();
		}	
	}
	
	@Override
	public void update(float dt){
		super.update(dt);
		setTexture();
	}

	/**
	 * Sets the texture and gets the picture
	 * from an instance from Content.
	 */
	public void setTexture(){
		Texture tex;
		if(doorIsLocked){
			tex = Content.getInstance().getTexture("lockedDoor");
		}else{
			tex = Content.getInstance().getTexture("openDoor");
		}

		TextureRegion[] sprites = TextureRegion.split(tex,  50,  50)[0];
		setAnimation(sprites, 1/ 12f);
	}
	
	public void setDoorIsLocked(Boolean b){
		doorIsLocked = b;
		setTexture();
	}
	public boolean getDoorIsLocked(){
		return doorIsLocked;
	}

}
