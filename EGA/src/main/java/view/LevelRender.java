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
/**
 * 
 * @author Emil Axelsson
 *
 * A view class that renders the level
 */
@Data
public class LevelRender {
	
	/** The Level model that keeps the data */
	private LevelModel levelModel;
	
	/** The spritebatch where it should draw*/
	private SpriteBatch sb;
	
	/**
	 * The constructor, sets the LevelModel and spritebatch
	 * @param mm, the LevelModel
	 * @param sb, the SpriteBatch
	 */
	public LevelRender(LevelModel mm, SpriteBatch sb){
		this.levelModel = mm;
		this.sb = sb;
		
	}
	
	/**
	 * Renders the level view.
	 * 
	 * @param cam, the camera that are pointed at the level
	 * @param tmr, renders the map of the level
	 * @param world, the world that is in the level
	 * @param b2dr, a debug render that show all the bodys in the map.
	 * @param b2dCam, the camera that is pointed to the map
	 */
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
