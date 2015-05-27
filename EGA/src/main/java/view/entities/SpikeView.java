package view.entities;

import io.Content;

import java.util.Arrays;
import java.util.Observable;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import lombok.Data;
import lombok.EqualsAndHashCode;
import model.entities.EntityModel;
import model.entities.SpikeModel.spikeOrientation;

@Data
@EqualsAndHashCode(callSuper=false)
public class SpikeView extends EntityView {
	
	private int spikeStyle = 1;
	
	
	public void setTexture(spikeOrientation ori){
		TextureRegion[] sprites = getTextureRegion(ori);
		
		//Range is which sprite to use
		setAnimation(Arrays.copyOfRange(sprites, spikeStyle,  spikeStyle+1), 0/ 12f);
		
	}
	
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

	public void update(Observable o, Object arg) {
		if(o instanceof EntityModel){		
			EntityModel em = (EntityModel)o;
			setXPosition(em.getXPosition());
			setYPosition(em.getYPosition());
			render();
		}		
	}

}
