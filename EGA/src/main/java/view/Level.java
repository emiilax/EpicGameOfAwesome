package view;
//TODO uncomment testmap.tmx and stuff

import static controller.Variables.PPM;
import lombok.Lombok;

import java.util.ArrayList;
import java.util.List;

import model.MyContactListener;
import model.MyInput;

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


import controller.Variables;
import controller.EGA;
import controller.GameStateManager;

public class Level extends GameState{

	private boolean debug = true;

	private World world;
	private Box2DDebugRenderer b2br;

	private OrthographicCamera b2dCam;

	private MyContactListener cl;

	private TiledMap tileMap;
	private float tilesize;
	private OrthogonalTiledMapRenderer tmr;

	private Character player;

	private Array<IStar> stars;
	private Door door;
	private Array<Spike> spikes;
	private GameStateManager gsm;
	//private Array<BigStar> bigStars;

	private HUD hud;

	public Level(GameStateManager gsm){

		super(gsm);
		
		this.gsm = gsm;

		// set up box2d stuff
		world = new World(new Vector2(0,-9.81f), true);
		cl = new MyContactListener(this);
		world.setContactListener(cl);
		b2br = new Box2DDebugRenderer();

		stars = new Array<IStar>();
		spikes = new Array<Spike>();
		//doors = new Array<Door>();

		createEntities();

		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, EGA.V_WIDTH / PPM, EGA.V_HEIGTH / PPM);

		// set up HUD

		hud = new HUD(player);

		//go through all the cells in the layer;

		// kinematic body, ex. a moving platform

	}

	public void handleInput() {
		//player.handleInput(cl);
	}

	public void update(float dt) {
		
		if(MyInput.isPressed(MyInput.BUTTON_LEVEL1)){
			renderNewLevel(1);
		} else if (MyInput.isPressed(MyInput.BUTTON_LEVEL2)) {
			renderNewLevel(2);
		}
		
		gsm.getGame().handleInput();
		//player.handleInput(cl);

		world.step(dt, 6, 2);

		removeStars();
		player.update(dt);

		for(int i  = 0; i < stars.size; i++){
			stars.get(i).update(dt);
		}
		
		for(Spike s: spikes){
			s.update(dt);
		}
		
		door.update(dt);
	}

	public void render() {

		//clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// set camera to follow player
		//cam.position.set(player.getPosition().x * PPM + Game.V_WIDTH / 4,
		//	Game.V_HEIGTH / 2, 
		//	0);

		//we never move the cam?
		//cam.update();

		// draw tile map
		tmr.setView(cam);
		tmr.render();

		// draw player 
		sb.setProjectionMatrix(cam.combined);
		player.render(sb);

		// draw crystals

		for(int i  = 0; i < stars.size; i++){
			stars.get(i).render(sb);
		}

		//sb.setProjectionMatrix(hudCam.combined);
		//hud.render(sb);
		
		for(Spike s: spikes){
			s.render(sb);
		}
		
		door.render(sb);

		if(debug){
			b2br.render(world, b2dCam.combined);
		}

	}
	
	public void resetLevel(){
		createMapObjects();
	}

	public void renderNewLevel(int pressedButton){
		
		
		// create player
		createPlayer();

		// create tiles
		createTiles(pressedButton);

		// create stars 
		createStars();
		
		createDoor();
		// create spikes
		createSpikes();
	}

	public void dispose() {}

	/**
	 * Removes the stars that have been collected
	 */
	public void removeStars(){
		Array<Body> bodies = cl.getBodiesToRemove();

		if(bodies.size > 0){
			String uData = bodies.get(0).getFixtureList().get(0).getUserData().toString();
			for(int i = 0; i < bodies.size; i++){
				Body b = bodies.get(i);
				stars.removeValue((IStar) b.getUserData(), true);
				world.destroyBody(b);

				if(uData.equals("smallStar")){
					changePlayerBody();
					player.collectShrinkStar();
				} else {
					changePlayerBody();
					player.collectGrowStar();
				}
			}
		}
		bodies.clear();
	}

	public void createEntities(){

		// create player
		createPlayer();

		// create tiles
		createTiles(1);

		// create stars
		createStars();

		// create spikes
		createSpikes();
		
		//create big Stars
		//createBigStars();
		
		// create door
		createDoor();
	}
	
	public void createMapObjects(){
		createStars();
		createSpikes();
		createDoor();
	}
	
	/**
	 * Creates the character
	 */
	public void createPlayer(){

		BodyDef bdef = new BodyDef();
		bdef.position.set(100  / PPM, 45 / PPM);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);

		player = new Character(body);

		body.setUserData(player);
	}

	/**
	 * Used when character is growing or shrinking.
	 * The method destroys the existing body and replace
	 * it with a new.
	 */
	public void changePlayerBody(){
		player.setCurrentVelocity();
		Body pb = player.getBody();
		world.destroyBody(pb);
		BodyDef bdef = new BodyDef();
		bdef.position.set(pb.getPosition().x , pb.getPosition().y);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);
		player.setBody(body);

		body.setUserData(player);
	}


	/**
	 * Create the tiles of the map (ground and platforms) 
	 * @param level, what level that should be loaded
	 */
	public void createTiles(int level){
		// load tiled map
		if(level == 1){
			tileMap = new TmxMapLoader().load("res/maps/testmap.tmx");
			
		} else {
			tileMap = new TmxMapLoader().load("res/maps/map2.tmx");
		}
		// This is if you want to try with a different map
		//tileMap = new TmxMapLoader().load("res/maps/testmap_ClausX.tmx");
		
		
		tmr = new OrthogonalTiledMapRenderer(tileMap);


		tilesize = (Integer) tileMap.getProperties().get("tilewidth");

		TiledMapTileLayer layer;

		layer = (TiledMapTileLayer) tileMap.getLayers().get("ground");	
		createLayer(layer, Variables.BIT_GROUND);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("platform");
		createLayer(layer, Variables.BIT_PLATFORM);

		//MapLayer ml = tileMap.getLayers().get("thePlatforms");
		//testCreateLayer(ml, Variables.BIT_PLATFORM);

	}

	public void testCreateLayer(MapLayer layer, short bits){
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		for(MapObject mo: layer.getObjects()){
			bdef.type = BodyType.StaticBody;


			float width = mo.getProperties().get("width", Float.class) / PPM / 2;
			float height = mo.getProperties().get("height", Float.class) / PPM / 2;

			float x = (mo.getProperties().get("x", Float.class) + width*PPM) / PPM;
			float y = (mo.getProperties().get("y", Float.class) + height*PPM) / PPM;

			bdef.position.set(x, y);
			shape.setAsBox(width, height);
			fdef.friction = 0;
			fdef.shape = shape;
			fdef.filter.categoryBits = bits;
			fdef.filter.maskBits = Variables.BIT_PLAYER;
			fdef.isSensor = false;

			world.createBody(bdef).createFixture(fdef);
		}
	}
	// Get rid of this eventually! use the method above instead
	public void createLayer(TiledMapTileLayer layer, short bits){

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		for(int row = 0; row < layer.getHeight(); row++){
			for(int col = 0; col < layer.getWidth(); col++){

				// get cell
				Cell cell = layer.getCell(col, row);

				// check if it exist
				if(cell == null) continue;
				if(cell.getTile() == null) continue;


				// create a body + fixure from cell
				bdef.type = BodyType.StaticBody;
				bdef.position.set((col + 0.5f)* tilesize / PPM, (row + 0.5f) * tilesize / PPM);

				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-tilesize / 2 / PPM, - tilesize / 2 / PPM);
				v[1] = new Vector2(-tilesize / 2 / PPM, tilesize / 2 / PPM);
				v[2] = new Vector2(tilesize / 2 / PPM, tilesize / 2 / PPM);
				v[3] = new Vector2(tilesize / 2 / PPM, -tilesize / 2 / PPM);
				v[4] = new Vector2(-tilesize / 2 / PPM, -tilesize / 2 / PPM);

				ChainShape cs = new ChainShape();
				cs.createChain(v);
				fdef.friction = 0;
				fdef.shape = cs;
				fdef.filter.categoryBits = bits;
				fdef.filter.maskBits = Variables.BIT_PLAYER;
				fdef.isSensor = false;
				world.createBody(bdef).createFixture(fdef);
			}
		}

		bdef.position.set(EGA.V_WIDTH /2/ PPM, EGA.V_HEIGTH /2/ PPM);

		Vector2[] v = new Vector2[5];
		v[0] = new Vector2(-EGA.V_WIDTH /2/ PPM, -EGA.V_HEIGTH/2/PPM);
		v[1] = new Vector2(-EGA.V_WIDTH/2/PPM, EGA.V_HEIGTH/2/PPM);
		v[2] = new Vector2(EGA.V_WIDTH/2/PPM, EGA.V_HEIGTH/2/PPM);
		v[3] = new Vector2(EGA.V_WIDTH/2/PPM, -EGA.V_HEIGTH/2/PPM);
		v[4] = new Vector2(-EGA.V_WIDTH/2/PPM, -EGA.V_HEIGTH/2/PPM);

		ChainShape cs = new ChainShape();
		cs.createChain(v);
		fdef.friction = 0;
		fdef.shape = cs;
		fdef.filter.categoryBits = bits;
		fdef.filter.maskBits = Variables.BIT_PLAYER;
		fdef.isSensor = false;
		world.createBody(bdef).createFixture(fdef);

	}

	/**
	 * Creates the stars on the map
	 */
	private void createStars(){
		BodyDef bdef = new BodyDef();
		//Create small stars
		MapLayer layer = tileMap.getLayers().get("stars");
		loopInStars(layer,true);

		// Create the big stars
		layer = tileMap.getLayers().get("bigStars");
		loopInStars(layer,false);

	}

	private void createDoor(){
		BodyDef bdef = new BodyDef();
		MapLayer layer = tileMap.getLayers().get("bigdoor");
			
			for(MapObject mo: layer.getObjects()){

				bdef.type = BodyType.StaticBody;

				float x = mo.getProperties().get("x", Float.class) / PPM;
				float y = mo.getProperties().get("y", Float.class) / PPM;

				bdef.position.set(x, y);
				
				Body body = world.createBody(bdef);
				
				door = new Door(body);

				body.setUserData(door);
				
			}
		}
	/**
	 * Used to loop in the stars to the map. A help
	 * method to createStars();
	 * @param layer, the layer that should be filled
	 * @param isSmallStar, boolean that says if its a small or big star
	 */
	private void loopInStars(MapLayer layer, boolean isSmallStar){
		BodyDef bdef = new BodyDef();

		for(MapObject mo: layer.getObjects()){

			bdef.type = BodyType.StaticBody;

			float x = mo.getProperties().get("x", Float.class) / PPM;
			float y = mo.getProperties().get("y", Float.class) / PPM;

			bdef.position.set(x, y);

			Body body = world.createBody(bdef);

			IStar s;
			if(isSmallStar){
				//body.setUserData("smallstar");
				s = new SmallStar(body);
			} else {
				//body.setUserData("bigstar");
				s = new BigStar(body);
			}
			stars.add(s);
			body.setUserData(s);

		}	
	}
		
	public void playerJump(){
		if(cl.isPlayerOnGround()){
			
			player.jump();
		}
	}
	
	public void playerMoveForward(){
		player.moveForward();
	}
	
	public void playerMoveBackward(){
		player.moveBackward();
	}
	
	public void playerStop(){
		player.stop();
	}
	
	private void createSpikes(){
		//Create spikes
		MapLayer layer = tileMap.getLayers().get("spikes");
		loopInSpikes(layer);	
	}
	
	private void loopInSpikes(MapLayer layer){
		BodyDef bdef = new BodyDef();
		for(MapObject mo: layer.getObjects()){

			bdef.type = BodyType.StaticBody;

			float x = mo.getProperties().get("x", Float.class) / PPM;
			float y = (mo.getProperties().get("y", Float.class)+10) / PPM;
			
			bdef.position.set(x, y);
			
			Body body = world.createBody(bdef);
			
			Spike s;
			s = new Spike(body);
			spikes.add(s);
			body.setUserData(s);
			
		}	
	}

}
