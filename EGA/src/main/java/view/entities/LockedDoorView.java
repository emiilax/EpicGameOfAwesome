package view.entities;

import io.Content;

import java.util.Observable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import model.entities.EntityModel;

public class LockedDoorView extends EntityView{
	
	public LockedDoorView(){
		setTexture();
	}
	

	/**
	 * Method update() updates the EntityModel with current positions
	 * and calls the render method.
	 * 
	 * @param o, Observable
	 * @param arg, Object 
	 */
	public void update(Observable o, Object arg) {
		if (o instanceof EntityModel) {
			super.setXPosition(((EntityModel) o).getXPosition());
			super.setYPosition(((EntityModel) o).getYPosition());
			render();
		}
		
	}

	/**
	 * Method setTexture() sets the texture and gets the picture
	 * from an instance from Content.
	 */
	public void setTexture(){
		Texture tex;
		tex = Content.getInstance().getTexture("lockedDoor");
		TextureRegion[] sprites = TextureRegion.split(tex,  50,  50)[0];
		setAnimation(sprites, 1/ 12f);
	}

}
