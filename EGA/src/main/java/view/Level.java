package view;

import static controller.Variables.PPM;
import lombok.Data;
import controller.SpikeController.spikeOrientation;
import model.CharacterModel;
import model.EGATimer;
import model.EntityModel;
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

import controller.EntityController;
import controller.CharacterController;
import controller.SpikeController;
import controller.KeyController;
import controller.StarController;
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
	
	//Entities
	private Character player;
	private Array<StarController> stars;
	private Array<IDoor> doors;
	private Array<SpikeController> spikes;
	private Array<KeyController> keys;
	private Array<EntityController> entities;
	
	//end Entities 
	private EGATimer timer;
	private boolean doorIsOpen;
	private boolean isPaused;
	
	//MVC Character 
	private EntityController chc;
	private CharacterModel chm;
	private CharacterView chv;
	private SpikeController spc;
	
	//MVC Key
	private KeyController kc;
	private EntityModel km;
	private KeyView kv;
	
	
	
	
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

		stars = new Array<StarController>();
		doors = new Array <IDoor>();
		spikes = new Array<SpikeController>();
		keys = new Array<KeyController>();
		
		entities = new Array<EntityController>(); 
		
		chc = new CharacterController(new CharacterModel(), new CharacterView());
		chc.setSpriteBatch(sb); //set this in constructor 
		kc = new KeyController(new EntityModel(), new KeyView());
		kc.setSpriteBatch(sb); //set this in contructor
		//door controller 
		
		
		createEntities();

		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, EGA.V_WIDTH / PPM, EGA.V_HEIGTH / PPM);

		// set up HUD
		
		timer = EGATimer.getTimer();
		timer.startTimer();

		//go through all the cells in the layer;

		// kinematic body, ex. a moving platform

	}
		
	PauseMenu m;
	public void handleInput(int i) {
		switch(i){
			case -1: ((CharacterController)chc).stop();
			break;
			
			case MyInput.BUTTON_FORWARD:  ((CharacterController)chc).moveForward();//playerMoveForward();
			break;
			
			case MyInput.BUTTON_BACKWARD:  ((CharacterController)chc).moveBackward();//playerMoveBackward();
			break;
			
			case MyInput.BUTTON_JUMP:  if(cl.isPlayerOnGround()) ((CharacterController)chc).jump();//playerJump();
			break;

			case MyInput.BUTTON_PAUSE: 
				if(!isPaused){
					m = new PauseMenu(gsm, this); 
					game.setLevel(m);
					isPaused = true;
					timer.stopTimer();
				}else{
					//game.setTheLevel(m.getTheGame());
					isPaused = false;
					resumeTimer();
				} // vi borde kunna ta bort en hel del av det här iom att man aldrig är i level 
				  // när isPaused är true...
			break;
			
			case MyInput.BUTTON_RESTART: gsm.getGame().setLevel(new Level(gsm, gsm.getCurrentTiledMap()));
			break;
			
			case MyInput.BUTTON_ESCAPE: gsm.getGame().setLevel(new MenuState(gsm));
			break;
		}
	}
	
	public void setIsPaused(boolean b){
		isPaused = false;
	}
	
	public void resumeTimer(){
		timer.resumeTimer();
	}

	public void update(float dt) {
		if(!isPaused){
	
			world.step(dt, 6, 2);
			
			removeEntities();
			//removeStars();
			//removeKeys();
			removeDoors();
			
			
			chc.update(dt);
			
			kc.update(dt);
			
			for(EntityController ec: entities){
				ec.update(dt);
			}

			
			for(SpikeController s: spikes){
				s.update(dt);
			}
			
			for(IDoor d: doors){
				d.update(dt);
			}

	}
	
	}

	public void render() {

		//clear screen
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// draw tile map
		tmr.setView(cam);
		tmr.render();

		// draw player 
		sb.setProjectionMatrix(cam.combined);
		//player.render(sb);
		
		chc.render();
		//kc.render();

		for(EntityController ec: entities){
			ec.render();
		}
		/*for(StarController s: stars){
			s.render();
		}
		
		for(SpikeController s: spikes){
			s.render();
		}*/
		
		for(IDoor d: doors){
			d.render(sb);
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

	public void removeEntities(){
		Array<Body> bodies = cl.getBodiesToRemove();
		
		if(bodies.size >0){
			for(Body b: bodies){
				//entities.removeValue((EntityController)b.getUserData(), true);
				
				if(b.getUserData() instanceof StarController){
					collectedStar((StarController)b.getUserData());
					
				}
				
				if(b.getUserData() instanceof KeyController) setDoorIsOpen(true);
				
				System.out.println("remove");
				entities.removeValue((EntityController)b.getUserData(), true);
				world.destroyBody(b);
			}
		}
		
		bodies.clear();
	}
	
	public void collectedStar(StarController s){
		if(!s.isBig()){
			((CharacterController)chc).setIsBig(false);
			changePlayerBody();
			//player.collectShrinkStar();
			((CharacterController)chc).collectShrinkStar();
		}else{
			((CharacterController)chc).setIsBig(true);
			changePlayerBody();
			//player.collectGrowStar();
			((CharacterController)chc).collectGrowStar();
		}
	}
	
	
	
	
	public void removeDoors(){
		Array<Body> bodies = cl.getDoorsToRemove();

		if(bodies.size > 0 && doorIsOpen){
			for(int i = 0; i < bodies.size; i++){
				Body b = bodies.get(i);
				doors.removeValue((IDoor) b.getUserData(), true);
				world.destroyBody(b);
				
				EGA.res.getSound("unlock").play();
				createOpenDoors();

			}
		}
		bodies.clear();
	}
	
// 	CREATE METHODS --------------------------------------------------------------
	public void createEntities(){
		createPlayer();
		createTiles();
		createMapObjects();
	}
	
	public void createMapObjects(){
		//createStars();
		createEntitiestest();
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
		loopEntity(layer, new StarController(new EntityModel(), new StarView(false)));

		// Create the big stars
		layer = tiledMap.getLayers().get("bigStars");
		loopEntity(layer, new StarController(new EntityModel(), new StarView(true)));

	}
	
	public Body createBody(MapObject mo){
		BodyDef bdef = new BodyDef();
		
		bdef.type = BodyType.StaticBody;

		float x = mo.getProperties().get("x", Float.class) / PPM;
		float y = mo.getProperties().get("y", Float.class) / PPM;

		bdef.position.set(x, y);

		return world.createBody(bdef);
	}
	
	public void loopEntity(MapLayer layer, EntityController ec){
		for(MapObject mo: layer.getObjects()){
			EntityController theController = null;
		
			if(ec instanceof StarController){
				boolean isBig = ((StarController)ec).isBig();
				theController = new StarController(new EntityModel(), new StarView(isBig));
			} 
			if(ec instanceof KeyController){
				theController = new KeyController(new EntityModel(), new KeyView());
			}
			if(ec instanceof SpikeController){
				spikeOrientation ori = ((SpikeController)ec).getSpikeOrientation();
				theController = new SpikeController(new EntityModel(), new SpikeView(ori));
			}
			
			Body body = null;
			BodyDef bdef = new BodyDef();
			
			if(!(ec instanceof SpikeController)){
				body = createBody(mo);
			}else{
				bdef.type = BodyType.StaticBody;

				float x = (mo.getProperties().get("x", Float.class)+10) / PPM;
				float y = (mo.getProperties().get("y", Float.class)+10) / PPM;
				
				bdef.position.set(x, y);
			
				body = world.createBody(bdef);
			}
			
			theController.setSpriteBatch(sb);
			theController.setBody(body);
			body.setUserData(theController);
			entities.add(theController);
			
		}
	}
	
	public void createEntitiestest(){
		
		createStars();
		createSpikes();
		createKey();
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
	
		MapLayer layer = tiledMap.getLayers().get("key");
		Body body = createBody(layer.getObjects().get(0));
		body.setUserData(kc);
		kc.setBody(body);
		entities.add(kc);
	}
	
	
	private void createSpikes(){
		//Create spikes
		MapLayer layer = tiledMap.getLayers().get("upSpikes");
		loopEntity(layer, new SpikeController(new EntityModel(), new SpikeView(spikeOrientation.UP)));
		
		layer = tiledMap.getLayers().get("downSpikes");
		loopEntity(layer, new SpikeController(new EntityModel(), new SpikeView(spikeOrientation.DOWN)));
		
		layer = tiledMap.getLayers().get("leftSpikes");
		loopEntity(layer, new SpikeController(new EntityModel(), new SpikeView(spikeOrientation.LEFT)));
		
		layer = tiledMap.getLayers().get("rightSpikes");
	}
	
	// END CREATE METHODS ----------------------------------------------------
	
	
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
	
	
	/**
	 * Used when character is growing or shrinking.
	 * The method destroys the existing body and replace
	 * it with a new.
	 */
	public void changePlayerBody(){
		((CharacterController)chc).setCurrentVelocity();

		Body pb = chc.getBody();
		world.destroyBody(pb);
		
		BodyDef bdef = new BodyDef();
		bdef.position.set(pb.getPosition().x , pb.getPosition().y);
		
		bdef.type = BodyType.DynamicBody;
		Body body = world.createBody(bdef);

		chc.setBody(body);
		body.setUserData(player);
	}

	private void setDoorIsOpen(boolean b){
		doorIsOpen = b;
	}
//	public void addDoor(OpenDoor door){
//		doors.add(door);
//	}
	
	/*
	private void loopInSpikes(MapLayer layer, spikeOrientation ori){
		BodyDef bdef = new BodyDef();
		for(MapObject mo: layer.getObjects()){

			bdef.type = BodyType.StaticBody;

			float x = (mo.getProperties().get("x", Float.class)+10) / PPM;
			float y = (mo.getProperties().get("y", Float.class)+10) / PPM;
			
			bdef.position.set(x, y);
		
			Body body = world.createBody(bdef);
			
			SpikeController s;
			s = new SpikeController(new EntityModel(), new SpikeView(), ori);
			s.setSpriteBatch(sb);
			s.setBody(body);
			body.setUserData(s);
			//spikes.add(s);
			entities.add(s);
		}	
	}*/
	
	/**
	 * Used to loop in the stars to the map. A help
	 * method to createStars();
	 * @param layer, the layer that should be filled
	 * @param isSmallStar, boolean that says if its a small or big star
	 */
	/*private void loopInStars(MapLayer layer, boolean isSmallStar){
		BodyDef bdef = new BodyDef();

		for(MapObject mo: layer.getObjects()){

			bdef.type = BodyType.StaticBody;

			float x = mo.getProperties().get("x", Float.class) / PPM;
			float y = mo.getProperties().get("y", Float.class) / PPM;

			bdef.position.set(x, y);

			Body body = world.createBody(bdef);

			StarController s;
			s = new StarController(new EntityModel(), new StarView(!isSmallStar));
			s.setSpriteBatch(sb);
			body.setUserData(s);
			s.setBody(body);
			
			entities.add(s);
			//stars.add(s);

		}	
		
	}*/
	
	/*	
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
	*/
	
	/**
	 * Removes the stars that have been collected
	 */
	/*
	public void removeStars(){
		Array<Body> bodies = cl.getBodiesToRemove();

		if(bodies.size > 0){
			String uData = bodies.get(0).getFixtureList().get(0).getUserData().toString();
			for(int i = 0; i < bodies.size; i++){
				Body b = bodies.get(i);
				entities.removeValue((EntityController)b.getUserData(), true);
				world.destroyBody(b);
				System.out.println(kc.getTheView().toString());
				if(uData.equals("smallStar")){
					((CharacterController)chc).setIsBig(false);
					changePlayerBody();
					//player.collectShrinkStar();
					((CharacterController)chc).collectShrinkStar();
				} else {
					((CharacterController)chc).setIsBig(true);
					changePlayerBody();
					//player.collectGrowStar();
					((CharacterController)chc).collectGrowStar();
				}
			}
		}
		bodies.clear();
	}*/
	/*
	public void removeKeys(){
		Array<Body> bodies = cl.getKeysToRemove();

		if(bodies.size > 0 ){
			for(int i = 0; i < bodies.size; i++){
				Body b = bodies.get(i);
				keys.removeValue((KeyController)b.getUserData(), true);
				world.destroyBody(b);
			}
			setDoorIsOpen(true);
		}
		bodies.clear();
	}*/
}
