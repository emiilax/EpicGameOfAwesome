package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import lombok.Data;
import model.LevelModel;

@Data
public class LevelRender {
	
	private LevelModel levelModel;
	private SpriteBatch sb;
	public LevelRender(LevelModel mm, SpriteBatch sb){
		this.levelModel = mm;
		this.sb = sb;
		
	}
	
	public void render(OrthographicCamera cam, TiledMapRenderer tmr, 
			World world, Box2DDebugRenderer b2dr, OrthographicCamera b2dCam){
		
		
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tmr.setView(cam);
		tmr.render();
		
		sb.setProjectionMatrix(cam.combined);
		
		if(levelModel.isDebug()){
			b2dr.render(world, b2dCam.combined);
		}
	}
	
}
