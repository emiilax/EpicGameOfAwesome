package view.entities;

import java.util.Observable;
import java.util.Observer;

import lombok.Data;
import model.EGATimer;
import model.entities.EntityModel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import controller.EGA;
import controller.Variables;

@Data
public class EGATimerView implements Observer {
	private boolean render;
	private BitmapFont font;
	private String currentTime;
	
	private float xPosition;
	private float yPosition;

	
	private SpriteBatch sb;
	
	public EGATimerView(){
		init();
		render=true;
	}

	private void init() {
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("res/fonts/orbitron-black.otf")
				);
		font = gen.generateFont(50);
		font.setColor(Color.RED);
	}

	public void update(Observable o, Object arg) {
		if (o instanceof EGATimer) {
			EGATimer et = (EGATimer)o;
			currentTime = Float.toString(et.getTimePassed());
			setXPosition(et.getXPosition());
			setYPosition(et.getYPosition());
			render();
		}		
	}

	private void render() {
		if(render){
			sb.begin();
			font.draw(sb, currentTime ,
					xPosition * Variables.PPM , 
					yPosition * Variables.PPM);
			sb.end();
		}
		
	}
	
	public void setSpriteBatch(SpriteBatch sb){
		this.sb = sb;
	}
	
}
