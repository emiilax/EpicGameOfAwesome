package view;

import static controller.Variables.PPM;
import lombok.Data;

import view.Spike.spikeOrientation;
import model.CharacterModel;
import model.EGATimer;
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

import controller.CharacterController;
import controller.Variables;
import controller.EGA;
import controller.GameStateManager;

@Data
public class Level extends GameState{

	private boolean debug = true;

	private World world;
	private Box2DDebugRenderer b2br;
	private OrthographicCamera b2dCam;
	private MyContactListener cl;
	private TiledMap tiledMap;
	private float tilesize;
	private OrthogonalTiledMapRenderer tmr;
	private GameStateManager gsm;
	private HUD hud;
	//Entities
	private Character player;
	private Array<IStar> stars;
	private Array<Spike> spikes;
	private Array <IDoor> doors;
	private Array <Key> keys;

	//end Entities 
	private EGATimer timer;
	private boolean doorIsOpen;
	private boolean isPaused;
	
	
	private CharacterController chc;
	private CharacterModel chm;
	private CharacterView chv;
	


	//public Level(GameStateManager gsm){
		public Level(GameStateManager gsm, TiledMap tiledMap){

		super(gsm);
		
		this.gsm = gsm;
		this.tiledMap = tiledMap;
		
		doorIsOpen = false;
		isPaused = false;
		// set up box2d stuff
		world = new World(new Vector2(0,-9.81f), true);
		cl = new MyContactListener(this);
		world.setContactListener(cl);
		b2br = new Box2DDebugRenderer();

		stars = new Array<IStar>();
		doors = new Array <IDoor>();
		spikes = new Array<Spike>();
		keys = new Array<Key>();
		
		chc = new CharacterController(new CharacterModel(), new CharacterView());
		chc.setSpriteBatch(sb);
		
		createEntities();

		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, EGA.V_WIDTH / PPM, EGA.V_HEIGTH / PPM);

		// set up HUD
		hud = new HUD(player);
		
		timer = EGATimer.getTimer();
		timer.startTimer();

		//go through all the cells in the layer;

		// kinematic body, ex. a moving platform
		
		
		

	}

	public void handleInput(int i) {
		switch(i){
			case -1: playerStop(); chc.stop();
			break;
			
			case MyInput.BUTTON_FORWARD:  chc.moveForward();//playerMoveForward();
			break;
			
			case MyInput.BUTTON_BACKWARD:  chc.moveBackward();//playerMoveBackward();
			break;
			
			case MyInput.BUTTON_JUMP:  if(cl.isPlayerOnGround()) chc.jump();//playerJump();
			break;
			case MyInput.BUTTON_PAUSE: 
				if(!isPaused){
					isPaused = true;
					System.out.println("is now paused");
					timer.stopTimer();
				}else{
					isPaused = false;
					System.out.println("is pause");
					timer.resumeTimer();
				}
			break;
				
		}
	}

	public void update(float dt) {
		if(!isPaused){
			if(MyInput.isPressed(MyInput.BUTTON_LEVEL1)){
				renderNewLevel(1);
			} else if (MyInput.isPressed(MyInput.BUTTON_LEVEL2)) {
				renderNewLevel(2);
			}
			
			//gsm.getGame().handleInput();
			//player.handleInput(cl);
	
			world.step(dt, 6, 2);
	
			removeStars();
			removeKeys();
			removeDoors();
			
			//player.update(dt);
			
			chc.update(dt);
			
			for(IStar s: stars){
				s.update(dt);
			}
			
			for(Spike s: spikes){
				s.update(dt);
			}
			
			for(IDoor d: doors){
				d.update(dt);
			}
			
			for(Key k: keys){
				k.update(dt);
			}
	}
	
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
		//player.render(sb);
		
		chc.render();

		// draw crystals
		for(int i  = 0; i < stars.size; i++){
			stars.get(i).render(sb);
		}

		//sb.setProjectionMatrix(hudCam.combined);
		//hud.render(sb);
		
		for(Spike s: spikes){
			s.render(sb);
		}
		
		//door.render(sb);
		
		for(IDoor d: doors){
			d.render(sb);
		}
		for(Key k: keys){
			k.render(sb);
		}

		if(debug){
			b2br.render(world, b2dCam.combined);
		}
		
	}
	
	public void resetLevel(){
		createMapObjects();
	}

	public void renderNewLevel(int Level){
		
		createPlayer();

		createTiles();

		createStars();

		createLockedDoors();

		createSpikes();
		
		createKey();
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
					chc.setIsBig(false);
					changePlayerBody();
					//player.collectShrinkStar();
					chc.collectShrinkStar();
				} else {
					chc.setIsBig(true);
					changePlayerBody();
					//player.collectGrowStar();
					chc.collectGrowStar();
				}
			}
		}
		bodies.clear();
	}
	
	public void removeKeys(){
		Array<Body> bodies = cl.getKeysToRemove();

		if(bodies.size > 0 ){
			//String uData = bodies.get(0).getFixtureList().get(0).getUserData().toString();
			for(int i = 0; i < bodies.size; i++){
				Body b = bodies.get(i);
				keys.removeValue((Key) b.getUserData(), true);
				world.destroyBody(b);
			}
			setDoorIsOpen(true);
		}
		bodies.clear();
	}
	
	public void removeDoors(){
		Array<Body> bodies = cl.getDoorsToRemove();

		if(bodies.size > 0 && doorIsOpen){
			//String uData = bodies.get(0).getFixtureList().get(0).getUserData().toString();
			for(int i = 0; i < bodies.size; i++){
				Body b = bodies.get(i);
				doors.removeValue((IDoor) b.getUserData(), true);
				world.destroyBody(b);
				
				createOpenDoors();

//				if(uData.equals("closedDoor")){
//					createOpenDoors();
//				} else {
//					// new level or highscore
//					//this can be in myContactListener too, which it is now				
//				}
			}
		}
		bodies.clear();
	}
	
// 	CREATE METHODS --------------------------------------------------------------
	public void createEntities(){

		createPlayer();

		createTiles();

		createStars();

		createSpikes();

		createLockedDoors();
		
		createKey();
	}
	
	public void createMapObjects(){
		createStars();
		createSpikes();
		createLockedDoors();
		createKey();
	}
	
	/**
	 * Creates the character
	 */
	public void createPlayer(){

		BodyDef bdef = new BodyDef();
		bdef.position.set(100  / PPM, 45 / PPM);
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);

		//player = new Character(body);
		
		chc.setBody(body);
		
		body.setUserData(player);
	}
	
	/**
	 * Create the tiles of the map (ground and platforms) 
	 * @param level, what level that should be loaded
	 */
	public void createTiles(){
		//create renderer
		tmr = new OrthogonalTiledMapRenderer(tiledMap);

		tilesize = (Integer) tiledMap.getProperties().get("tilewidth");

		TiledMapTileLayer layer;

		layer = (TiledMapTileLayer) tiledMap.getLayers().get("ground");	
		createLayer(layer, Variables.BIT_GROUND);
		layer = (TiledMapTileLayer) tiledMap.getLayers().get("platform");
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
		//BodyDef bdef = new BodyDef();
		
		//Create small stars
		MapLayer layer = tiledMap.getLayers().get("stars");
		loopInStars(layer,true);

		// Create the big stars
		layer = tiledMap.getLayers().get("bigStars");
		loopInStars(layer,false);

	}
	
	private void createLockedDoors(){
		// Create LockedDoors
		MapLayer layer = tiledMap.getLayers().get("lockedDoor");
		loopInDoors(layer, "lockedDoor");
	}
	private void createOpenDoors(){
		//Create OpenDoors
		MapLayer layer = tiledMap.getLayers().get("openDoor");
		loopInDoors(layer, "openDoor");
	}
	
	private void createKey(){
		BodyDef bdef = new BodyDef();
		MapLayer layer = tiledMap.getLayers().get("key");
			
			for(MapObject mo: layer.getObjects()){

				bdef.type = BodyType.StaticBody;

				float x = mo.getProperties().get("x", Float.class) / PPM;
				float y = mo.getProperties().get("y", Float.class) / PPM;

				bdef.position.set(x, y);
				
				Body body = world.createBody(bdef);
				
				Key key = new Key(body);
				keys.add(key);

				body.setUserData(key);
				
			}
		}
	private void createSpikes(){
		//Create spikes
		MapLayer layer = tiledMap.getLayers().get("upSpikes");
		loopInSpikes(layer, spikeOrientation.UP);	
		
		layer = tiledMap.getLayers().get("downSpikes");
		loopInSpikes(layer, spikeOrientation.DOWN);
		
		layer = tiledMap.getLayers().get("leftSpikes");
		loopInSpikes(layer, spikeOrientation.LEFT);
		
		layer = tiledMap.getLayers().get("rightSpikes");
		loopInSpikes(layer, spikeOrientation.RIGHT);
	}
	
	// END CREATE METHODS ----------------------------------------------------
	
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
	private void loopInDoors(MapLayer layer, String texString){
		BodyDef bdef = new BodyDef();

		for(MapObject mo: layer.getObjects()){

			bdef.type = BodyType.StaticBody;

			float x = mo.getProperties().get("x", Float.class) / PPM;
			float y = mo.getProperties().get("y", Float.class) / PPM;

			bdef.position.set(x, y);

			Body body = world.createBody(bdef);

			IDoor id;
			if(texString.equals("openDoor")){
				id = new OpenDoor(body, "openDoor");
				doors.add(id);
				body.setUserData(id);
			} else{
				id = new LockedDoor(body, "lockedDoor");
				doors.add(id);
				body.setUserData(id);
			}	
		}	
	}
	
		
	public void playerJump(){
		if(cl.isPlayerOnGround()){
			
			//player.jump();
		}
	}
	
	public void playerMoveForward(){
		//player.moveForward();
	}
	
	public void playerMoveBackward(){
		//player.moveBackward();
	}
	
	public void playerStop(){
		//player.stop();
	}
	
	
	private void loopInSpikes(MapLayer layer, spikeOrientation ori){
		BodyDef bdef = new BodyDef();
		for(MapObject mo: layer.getObjects()){

			bdef.type = BodyType.StaticBody;

			float x = (mo.getProperties().get("x", Float.class)+10) / PPM;
			float y = (mo.getProperties().get("y", Float.class)+10) / PPM;
			
			bdef.position.set(x, y);
		
			Body body = world.createBody(bdef);
			
			Spike s;
			s = new Spike(body, ori);
			spikes.add(s);
			body.setUserData(s);
			
		}	
	}
	/**
	 * Used when character is growing or shrinking.
	 * The method destroys the existing body and replace
	 * it with a new.
	 */
	public void changePlayerBody(){
		//player.setCurrentVelocity();
		chc.setCurrentVelocity();
		
		//Body pb = player.getBody();
		Body pb = chc.getBody();
		world.destroyBody(pb);
		
		BodyDef bdef = new BodyDef();
		bdef.position.set(pb.getPosition().x , pb.getPosition().y+0.1f);
		
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);
		//player.setBody(body);
		chc.setBody(body);
		body.setUserData(player);
	}

	private void setDoorIsOpen(boolean b){
		doorIsOpen = b;
	}
//	public void addDoor(OpenDoor door){
//		doors.add(door);
//	}
}
