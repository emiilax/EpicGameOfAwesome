package view.entities;

import java.util.Observable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import controller.EGA;
import model.entities.CharacterModel;
import model.entities.EntityModel;

/**
 * 
 * @author Rebecka Reitmaier
 *
 */

public class KeyView extends EntityView {
	
	public KeyView (){
		setTexture();
	}

	public void update(Observable o, Object arg) {
		if (o instanceof EntityModel) {
			super.setXPosition(((EntityModel) o).getXPosition());
			super.setYPosition(((EntityModel) o).getYPosition());
			render();
		}
	}

	public void setTexture (){

		Texture tex;
		tex = EGA.res.getTexture("key"); 
		TextureRegion[] sprites = TextureRegion.split(tex,  50,  50)[0];
		
		setAnimation(sprites, 1/ 6f);
	}
}
