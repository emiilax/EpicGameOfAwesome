package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import controller.EGA;
import controller.Variables;
import lombok.Data;
import model.LevelModel;

@Data
public class LevelRender {
	
	private GlyphLayout layout = new GlyphLayout();
	private BitmapFont titleFont;
	private String title = "testtesttesttest";
	
	private LevelModel levelModel;
	private SpriteBatch sb;
	private Sprite sprite;
	public LevelRender(LevelModel mm, SpriteBatch sb){
		this.levelModel = mm;
		this.sb = sb;
		

		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
				Gdx.files.internal("res/fonts/orbitron-black.otf")
				);

		titleFont = gen.generateFont(150);
		titleFont.setColor(Color.RED);
		
	}
	
	public void render(OrthographicCamera cam, TiledMapRenderer tmr, 
			World world, Box2DDebugRenderer b2dr, OrthographicCamera b2dCam){
		
		
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sb.setProjectionMatrix(cam.combined);
		
		sb.begin();
		
		tmr.setView(cam);
		tmr.render();		
		
		layout.setText(titleFont, title);
		titleFont.draw(sb, title, EGA.V_WIDTH/2, EGA.V_HEIGTH/2);
		
		
		if(levelModel.isDebug()){
			b2dr.render(world, b2dCam.combined);
		}
		
		sb.end();
	}
	
}
