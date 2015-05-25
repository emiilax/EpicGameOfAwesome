package view;

import static controller.Variables.PPM;
import view.entities.CharacterView;
import view.entities.KeyView;
import view.entities.LockedDoorView;
import view.entities.OpenDoorView;
import view.menus.PauseMenu;
import lombok.Data;
import controller.SpikeController.spikeOrientation;
import controller.entities.CharacterController;
import controller.entities.EntityController;
import controller.entities.KeyController;
import controller.entities.LockedDoorController;
import controller.entities.OpenDoorController;
import model.EGATimer;
import model.MyInput;
import model.entities.CharacterModel;
import model.entities.EntityModel;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import controller.MyContactListener;

import controller.SaveHandler;
import controller.SpikeController;
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
	//private Array<IDoor> doors;
	private Array<LockedDoorController> lockedDoor;
	private Array<OpenDoorController> openDoor;
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

	//MVC Doors
	private LockedDoorController ldc;
	private LockedDoorView ldv;
	private EntityModel ldm;

	private OpenDoorController odc;
	private OpenDoorView odv;
	private EntityModel odm;



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
		//doors = new Array <IDoor>();
		spikes = new Array<SpikeController>();
		keys = new Array<KeyController>();

		entities = new Array<EntityController>(); 

		//create controllers for the game and set the spritebatch
		chc = new CharacterController(new CharacterModel(), new CharacterView());
		chc.setSpriteBatch(sb);
		kc = new KeyController(new EntityModel(), new KeyView());
		kc.setSpriteBatch(sb); 
		ldc = new LockedDoorController(new EntityModel(), new LockedDoorView());
		ldc.setSpriteBatch(sb);
		odc = new OpenDoorController(new EntityModel(), new OpenDoorView());
		odc.setSpriteBatch(sb);
		
		debug = SaveHandler.getGameData().getIsDebug();

		createEntities();
		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, EGA.V_WIDTH / PPM, EGA.V_HEIGTH / PPM);
		//set up the game timer
		timer = EGATimer.getTimer();
		timer.startTimer();


	}

	public void toggleDebug(){
		EGA game = gsm.getGame();
		game.toggleDebug();
		debug = game.getDebug(); 
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
		
		// Pause
		case MyInput.BUTTON_PAUSE: 
			if(!isPaused){
				m = new PauseMenu(gsm);
				gsm.pushState(new PauseMenu(gsm));
				//game.setLevel(m);
				System.out.println("paus");
				isPaused = true;
				timer.stopTimer();
			}
			
			break;

		case MyInput.BUTTON_RESTART: gsm.setState(new Level(gsm, gsm.getCurrentTiledMap()));
		break;
		
		// Pause
		case MyInput.BUTTON_ESCAPE: 
			gsm.pushState(new PauseMenu(gsm));
			isPaused = true;
			timer.stopTimer();
			//gsm.setState(new MenuState(gsm));
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
		debug = SaveHandler.getGameData().getIsDebug();
		if(isPaused){
			isPaused = false;
			resumeTimer();
		}
		
		if(!isPaused){

			world.step(dt, 6, 2);

			removeEntities();

			//update controllers
			ldc.update(dt);
			odc.update(dt);	
			chc.update(dt);
			kc.update(dt);

			for(EntityController ec: entities){
				ec.update(dt);
			}
			for(SpikeController s: spikes){
				s.update(dt);
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

		createLockedDoor();

		createSpikes();

		createKey();

		//createEntities(); 
	}

	public void dispose() {}

	public void removeEntities(){
		Array<Body> bodies = cl.getBodiesToRemove();

		if(bodies.size >0){
			for(Body b: bodies){
				//entities.removeValue((EntityController)b.getUserData(), true);

				if(b.getUserData() instanceof StarController) collectedStar((StarController)b.getUserData());

				if(b.getUserData() instanceof KeyController) setDoorIsOpen(true);

				if(b.getUserData() instanceof LockedDoorController) createOpenDoor();

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
			((CharacterController)chc).collectShrinkStar();
		}else{
			((CharacterController)chc).setIsBig(true);
			changePlayerBody();
			((CharacterController)chc).collectGrowStar();
		}
	}




//	public void removeDoors(){
//		Array<Body> bodies = cl.getDoorsToRemove();
//
//		if(bodies.size > 0 && doorIsOpen){
//			for(int i = 0; i < bodies.size; i++){
//				Body b = bodies.get(i);
//				doors.removeValue( (IDoor) b.getUserData(), true); 
//				world.destroyBody(b);
//
//				EGA.res.getSound("unlock").play();
//				createOpenDoor();
//
//			}
//		}
//		bodies.clear();
//	}

	// 	CREATE METHODS --------------------------------------------------------------
	public void createEntities(){
		createPlayer();
		createTiles();
		createMapObjects();
	}

	public void createMapObjects(){

		createStars();
		createSpikes();
		createKey();
		createLockedDoor();
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

		//Create small stars
		MapLayer layer = tiledMap.getLayers().get("stars");
		loopEntity(layer, new StarController(new EntityModel(), new StarView(false), false));

		// Create the big stars
		layer = tiledMap.getLayers().get("bigStars");
		loopEntity(layer, new StarController(new EntityModel(), new StarView(true),true));

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
				theController = new StarController(new EntityModel(), new StarView(isBig),isBig);
			} 
			if(ec instanceof KeyController){
				theController = new KeyController(new EntityModel(), new KeyView());
			}
			if(ec instanceof OpenDoorController){
				theController = new OpenDoorController(new EntityModel(), new OpenDoorView());
			}
			if(ec instanceof LockedDoorController){
				theController = new LockedDoorController(new EntityModel(), new LockedDoorView());
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

	private void createLockedDoor(){

		MapLayer layer = tiledMap.getLayers().get("lockedDoor");
		Body body = createBody(layer.getObjects().get(0));
		body.setUserData(ldc);
		ldc.setBody(body);
		entities.add(ldc);
	}
	private void createOpenDoor(){

		MapLayer layer = tiledMap.getLayers().get("openDoor");
		Body body = createBody(layer.getObjects().get(0));
		body.setUserData(odc);
		odc.setBody(body);
		entities.add(odc);
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
		loopEntity(layer, new SpikeController(new EntityModel(), new SpikeView(spikeOrientation.RIGHT)));
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

	public Boolean getDoorIsOpen(){
		return doorIsOpen;
	}
}


