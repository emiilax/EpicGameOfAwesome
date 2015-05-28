package view.entities;

import io.Content;

import java.util.Arrays;
import java.util.Observable;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import lombok.Data;
import lombok.EqualsAndHashCode;
import model.entities.EntityModel;
import model.entities.SpikeModel.spikeOrientation;
/**
 * a class that sets the correct texture for a spike and renders it.
 * @author Hampus Rönström
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class SpikeView extends EntityView {
	/** decides which spike to use from the TextureRegion[] */
	private int spikeStyle = 1;
	
	/**
	 * sets the texture of a spike. The parameter ori decides which orientation
	 * the spike has.
	 * @param ori
	 */
	public void setTexture(spikeOrientation ori){
		TextureRegion[] sprites = getTextureRegion(ori);
		
		//Range is which sprite to use
		setAnimation(Arrays.copyOfRange(sprites, spikeStyle,  spikeStyle+1), 0/ 12f);
		
	}
	
	/**
	 * returns a TextureRegion[] with spikes in the correct orientation.
	 * @param ori
	 * @return TextureRegion[]
	 */
	private TextureRegion[] getTextureRegion(spikeOrientation ori){
		switch(ori){
		case UP:
			return TextureRegion.split(Content.getInstance().getTexture("upSpike"),  16,  21)[0];
		case DOWN:
			return TextureRegion.split(Content.getInstance().getTexture("downSpike"),  16,  21)[0];
		case LEFT:
			return TextureRegion.split(Content.getInstance().getTexture("leftSpike"),  21,  16)[0];
		case RIGHT:
			return TextureRegion.split(Content.getInstance().getTexture("rightSpike"),  21,  16)[0];
		default:
			return TextureRegion.split(Content.getInstance().getTexture("upSpike"),  16,  21)[0];
		}
	}

	/**
	 * updates the position of the spike with values from the EntityModel.
	 * @param o
	 * @param arg
	 */
	public void update(Observable o, Object arg) {
		if(o instanceof EntityModel){		
			EntityModel em = (EntityModel)o;
			setXPosition(em.getXPosition());
			setYPosition(em.getYPosition());
			render();
		}		
	}

}
