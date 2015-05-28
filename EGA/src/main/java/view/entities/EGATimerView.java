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
	private float timePassed;
	

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
		font = gen.generateFont(150);
		font.setColor(Color.RED);
	}

	public void update(Observable o, Object arg) {
		if (o instanceof EGATimer) {
			render();
		}		
	}

	private void render() {
		if(render){
			sb.begin();
			font.draw(sb, "testes" ,
					xPosition * Variables.PPM / 2, 
					yPosition * Variables.PPM/ 2);
			System.out.println("in EGATimerView.render()");
			sb.end();
		}
		
	}
	
	public void setSpriteBatch(SpriteBatch sb){
		this.sb = sb;
	}
	
}
