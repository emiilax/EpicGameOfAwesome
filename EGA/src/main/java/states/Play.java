package states;

import static handlers.B2DVars.PPM; 
import main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import handlers.B2DVars;
import handlers.GameStateManager;
import handlers.MyContactListener;
import handlers.MyInput;

public class Play extends GameState{
	
	private World world;
	private Box2DDebugRenderer b2br;
	
	private OrthographicCamera b2dCam;
	
	private Body playerBody;
	
	private MyContactListener cl;
	
	private TiledMap tileMap;
	private float tilesize;
	private OrthogonalTiledMapRenderer tmr;
	
	
	public Play(GameStateManager gsm){
		
		super(gsm);
		
		// creating world
		world = new World(new Vector2(0,-1.81f), true);
		cl = new MyContactListener();
		world.setContactListener(cl);
		
		b2br = new Box2DDebugRenderer();
		
		// creating platform
		BodyDef bdef = new BodyDef();
		//bdef.position.set(160 / PPM, 120 / PPM);
		// Static body, don't move, unaffected by forces
		//bdef.type = BodyType.StaticBody;
		//Body body = world.createBody(bdef);
		PolygonShape shape = new PolygonShape();
		FixtureDef fDef = new FixtureDef();
		
		//Creating player
		//dynamic body, always get affected by forces
		bdef.position.set(160  / PPM, 200 / PPM);
		bdef.type = BodyType.DynamicBody;
		playerBody = world.createBody(bdef);
		shape.setAsBox(5 / PPM, 5 / PPM);
		
		
		fDef.shape = shape;
		fDef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fDef.filter.maskBits = B2DVars.BIT_GROUND;
		//fDef.restitution = 0.5f;
		playerBody.createFixture(fDef).setUserData("player");
		
		// create foot sensor
		shape.setAsBox(2/PPM,  2 / PPM, new Vector2(0, -5/ PPM), 0);
		fDef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fDef.filter.maskBits = B2DVars.BIT_GROUND;
		fDef.isSensor = true;
		playerBody.createFixture(fDef).setUserData("foot");
		
		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGTH / PPM);
		
		///////////////////
		
		// load tiled map
		tileMap = new TmxMapLoader().load("res/maps/firstmap.tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		
		TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get("Ground");
		
		tilesize = layer.getTileHeight();
		
		//go through all the cells in the layer;
		for(int row = 0; row < layer.getHeight(); row++){
			for(int col = 0; col < layer.getWidth(); col++){
				
				// get cell
				Cell cell = layer.getCell(col, row);
				
				//check if it exist
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
				
				
				// create a body + fixure from cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col + 0.5f)* tilesize / PPM, (row + 0.5f) * tilesize / PPM);
				
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[3];
				v[0] = new Vector2(-tilesize / 2 / PPM, - tilesize / 2 / PPM);
				v[1] = new Vector2(-tilesize / 2 / PPM, tilesize / 2 / PPM);
				v[2] = new Vector2(tilesize / 2 / PPM, tilesize / 2 / PPM);
				cs.createChain(v);
				fDef.friction = 0;
				fDef.shape = cs;
				fDef.filter.categoryBits = B2DVars.BIT_GROUND;
				fDef.filter.maskBits = B2DVars.BIT_PLAYER;
				fDef.isSensor = false;
				world.createBody(bdef).createFixture(fDef);
			}
		}
		// kinematic body, ex. a moving platform
		
	}

	


	public void handleInput() {
		if(MyInput.isDown(MyInput.BUTTON1)){
			if(cl.isPlayerOnGround()){
				playerBody.applyForceToCenter(2, 0, true);
			}
		}
		if(MyInput.isDown(MyInput.BUTTON2)){
			playerBody.applyForceToCenter(-2, 0, true);
		}
	}


	public void update(float dt) {
		handleInput();
		
		world.step(dt, 6, 2);
	}


	public void render() {
		
		//clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// draw tile map
		tmr.setView(cam);
		tmr.render();
		
		b2br.render(world, b2dCam.combined);
	}


	public void dispose() {}

}
