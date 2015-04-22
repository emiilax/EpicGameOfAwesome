package states;

import static handlers.B2DVars.PPM; 
import main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import enteties.Crystal;
import enteties.HUD;
import enteties.Player;
import handlers.B2DVars;
import handlers.GameStateManager;
import handlers.MyContactListener;
import handlers.MyInput;

public class Play extends GameState{
	
	private boolean debug = true;
	
	private World world;
	private Box2DDebugRenderer b2br;
	
	private OrthographicCamera b2dCam;
	
	private MyContactListener cl;
	
	private TiledMap tileMap;
	private float tilesize;
	private OrthogonalTiledMapRenderer tmr;
	
	private Player player;
	
	private Array<Crystal> crystals;
	
	private HUD hud;
	
	public Play(GameStateManager gsm){
		
		super(gsm);
		
		// set up box2d stuff
		world = new World(new Vector2(0,-9.81f), true);
		cl = new MyContactListener();
		world.setContactListener(cl);
		b2br = new Box2DDebugRenderer();
		
		// create player
		createPlayer();
		
		// create tiles
		createTiles();
		
		// create crystals 
		createCrystals();
		
		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGTH / PPM);
		
		// set up HUD
		
		hud = new HUD(player);
		
		//go through all the cells in the layer;
		
		// kinematic body, ex. a moving platform
		
	}

	


	public void handleInput() {
		
		if(MyInput.isPressed(MyInput.BUTTON_JUMP)){
			if(cl.isPlayerOnGround()){
				
				player.getBody().applyForceToCenter(0, 250, true);
				//player.getBody().localVector.y
				
			}
		}
		
		if(MyInput.isDown(MyInput.BUTTON_FORWARD)){
			//player.getBody().applyForceToCenter(20, 0, true);
			player.getBody().setLinearVelocity(1.5f, player.getBody().getLinearVelocity().y);
		}else if(MyInput.isDown(MyInput.BUTTON_BACKWARD)){
			player.getBody().setLinearVelocity(-1.5f, player.getBody().getLinearVelocity().y);
			//player.getBody().applyForceToCenter(-1, 0, true);
		}else if(!MyInput.isDown(MyInput.BUTTON_FORWARD) || !MyInput.isDown(MyInput.BUTTON_BACKWARD)){
			player.getBody().setLinearVelocity(0, player.getBody().getLinearVelocity().y);
		}
		
	}


	public void update(float dt) {
		handleInput();
		
		world.step(dt, 6, 2);
		
		// remove crystals
		Array<Body> bodies = cl.getBodiesToRemove();
		
		for(int i = 0; i < bodies.size; i++){
			Body b = bodies.get(i);
			crystals.removeValue((Crystal) b.getUserData(), true);
			world.destroyBody(b);
			player.collectCrystal();
		}
		
		bodies.clear();
		
		player.update(dt);
		/*
		for(int i  = 0; i < crystals.size; i++){
			crystals.get(i).update(dt);
		}*/
	}


	public void render() {
		
		//clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// set camera to follow player
		//cam.position.set(player.getPosition().x * PPM + Game.V_WIDTH / 4,
						//	Game.V_HEIGTH / 2, 
						//	0);
		
		cam.update();
		
		// draw tile map
		tmr.setView(cam);
		tmr.render();
		
		// draw player 
		sb.setProjectionMatrix(cam.combined);
		player.render(sb);
		
		// draw crystals
		/*
		for(int i  = 0; i < crystals.size; i++){
			crystals.get(i).render(sb);
		}*/
		
		//sb.setProjectionMatrix(hudCam.combined);
		//hud.render(sb);
		
		if(debug){
			b2br.render(world, b2dCam.combined);
		}
		
	}


	public void dispose() {}
	
	
	public void createPlayer(){
		
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fDef = new FixtureDef();
		
		//Create player
		//dynamic body, always get affected by forces
		bdef.position.set(100  / PPM, 45 / PPM);
		bdef.type = BodyType.DynamicBody;
		//bdef.linearVelocity.set(0, 0);
		Body body = world.createBody(bdef);
		
		shape.setAsBox(10 / PPM, 9 / PPM);
		fDef.shape = shape;
		fDef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fDef.filter.maskBits = B2DVars.BIT_RED | B2DVars.BIT_GREEN | B2DVars.BIT_BLUE | B2DVars.BIT_CRYSTAL;
		//fDef.restitution = 0.5f;
		body.createFixture(fDef).setUserData("player");
		
		// create foot sensor
		shape.setAsBox( 10/PPM,  1 / PPM, new Vector2(0, -10/ PPM), 0);
		fDef.filter.categoryBits = B2DVars.BIT_PLAYER;
		fDef.filter.maskBits = B2DVars.BIT_RED | B2DVars.BIT_GREEN | B2DVars.BIT_BLUE;
		fDef.isSensor = true;
		body.createFixture(fDef).setUserData("foot");
		
		//create player
		player = new Player(body);
		
		body.setUserData(player);
		
		
	}
	
	public void createTiles(){
		
		// load tiled map
		tileMap = new TmxMapLoader().load("res/maps/testmap.tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		
		
		tilesize = (Integer) tileMap.getProperties().get("tilewidth");
		
		TiledMapTileLayer layer;
		
		layer = (TiledMapTileLayer) tileMap.getLayers().get("ground");	
		createLayer(layer, B2DVars.BIT_RED);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("platform");	
		createLayer(layer, B2DVars.BIT_GREEN);
		//layer = (TiledMapTileLayer) tileMap.getLayers().get("blue");	
		//createLayer(layer, B2DVars.BIT_BLUE);
		
				
	}
	
	public void createLayer(TiledMapTileLayer layer, short bits){
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
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
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-tilesize / 2 / PPM, - tilesize / 2 / PPM);
				v[1] = new Vector2(-tilesize / 2 / PPM, tilesize / 2 / PPM);
				v[2] = new Vector2(tilesize / 2 / PPM, tilesize / 2 / PPM);
				v[3] = new Vector2(tilesize / 2 / PPM, -tilesize / 2 / PPM);
				v[4] = new Vector2(-tilesize / 2 / PPM, -tilesize / 2 / PPM);
				
				cs.createChain(v);
				fdef.friction = 0;
				fdef.shape = cs;
				fdef.filter.categoryBits = bits;
				fdef.filter.maskBits = B2DVars.BIT_PLAYER;
				fdef.isSensor = false;
				world.createBody(bdef).createFixture(fdef);
			}
		}
		
		bdef.position.set(Game.V_WIDTH / PPM, Game.V_HEIGTH / PPM);
		
		Vector2[] v = new Vector2[5];
		v[0] = new Vector2(-Game.V_WIDTH / PPM, -Game.V_HEIGTH/PPM);
		v[1] = new Vector2(-Game.V_WIDTH/PPM, Game.V_HEIGTH/PPM);
		v[2] = new Vector2(Game.V_WIDTH/PPM, Game.V_HEIGTH/PPM);
		v[3] = new Vector2(Game.V_WIDTH/PPM, -Game.V_HEIGTH/PPM);
		v[4] = new Vector2(-Game.V_WIDTH/PPM, -Game.V_HEIGTH/PPM);
		
		ChainShape cs = new ChainShape();
		cs.createChain(v);
		fdef.friction = 0;
		fdef.shape = cs;
		fdef.filter.categoryBits = bits;
		fdef.filter.maskBits = B2DVars.BIT_PLAYER;
		fdef.isSensor = false;
		world.createBody(bdef).createFixture(fdef);
		
	}
	
	public void createCrystals(){
		/*
		crystals = new Array<Crystal>();
		
		MapLayer layer = tileMap.getLayers().get("crystals");
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		
		
		for(MapObject mo: layer.getObjects()){
			
			bdef.type = BodyType.StaticBody;
			
			float x = mo.getProperties().get("x", Float.class) / PPM;
			float y = mo.getProperties().get("y", Float.class) / PPM;
			
			bdef.position.set(x, y);
			
			CircleShape cshape = new CircleShape();
			cshape.setRadius(8 / PPM);
			
			fdef.shape = cshape;
			fdef.isSensor = true;
			
			fdef.filter.categoryBits = B2DVars.BIT_CRYSTAL;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			
			Body body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("crystal");;
			
			Crystal c = new Crystal(body);
			crystals.add(c);
			
			body.setUserData(c);
			
		}
		*/
	}
	private void switchBlocks(){
		
		Filter filter = player.getBody().getFixtureList().first().getFilterData();
		
		short bits = filter.maskBits;
		
		// switch to next color
		
		// red -> green -> blue -> red
		if((bits & B2DVars.BIT_RED) != 0){
			bits &= ~B2DVars.BIT_RED;
			bits |= B2DVars.BIT_GREEN;
		}else if((bits & B2DVars.BIT_GREEN) != 0){
			bits &= ~B2DVars.BIT_GREEN;
			bits |= B2DVars.BIT_BLUE;
		}else if((bits & B2DVars.BIT_BLUE) != 0){
			bits &= ~B2DVars.BIT_BLUE;
			bits |= B2DVars.BIT_RED;
		}
		
		// set new mask bits
		filter.maskBits = bits;
		player.getBody().getFixtureList().first().setFilterData(filter);
		
		// set new mask bits for foot
		filter = player.getBody().getFixtureList().get(1).getFilterData();
		bits &= ~B2DVars.BIT_CRYSTAL;
		filter.maskBits = bits;
		player.getBody().getFixtureList().get(1).setFilterData(filter);
		
			
	}
}
