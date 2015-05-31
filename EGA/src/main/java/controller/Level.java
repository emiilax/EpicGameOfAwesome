package controller;

import static model.Variables.PPM;
import io.SaveHandler;
import view.entities.CharacterView;
import view.entities.EGATimerView;
import view.entities.KeyView;
import view.entities.SpikeView;
import view.entities.StarView;
import view.renders.ViewRender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import controller.entities.CharacterController;
import controller.entities.EGATimerController;
import controller.entities.DoorController;
import controller.entities.EntityController;
import controller.entities.KeyController;
import controller.entities.SpikeController;
import controller.entities.StarController;
import controller.menus.GameState;
import model.EGATimer;
import model.LevelModel;
import model.MyInput;
import model.Variables;
import model.entities.CharacterModel;
import model.entities.EntityModel;
import model.entities.SpikeModel;
import model.entities.SpikeModel.spikeOrientation;

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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import view.entities.DoorView;
import event.EventSupport;
import event.TheEvent;

@Data
@EqualsAndHashCode(callSuper=false)
public class Level extends GameState{

	private boolean debug;

	private World world;
	private Box2DDebugRenderer b2br;
	private OrthographicCamera b2dCam;
	private MyContactListener cl;
	private TiledMap tiledMap;
	private float tilesize;
	private OrthogonalTiledMapRenderer tmr;
	private GameStateManager gsm;

	//Array with the entities
	private Array<EntityController> entities;

	
	private EGATimer timer;
	private EGATimerController etc;
	private boolean doorIsOpen;
	private boolean isPaused;

	//MVC Character 
	private CharacterController chc;
	private CharacterModel chm;
	private CharacterView chv;
	

	//MVC Doors
	private DoorController doorC; //ldc
	private DoorView doorV; //ldv
	private EntityModel doorM;

	private ViewRender lvlRender;
	private LevelModel lvlModel;
	
	private EGA game;
	
	public Level(EGA game ,TiledMap tiledMap){

		super();
		this.game = game;
		this.tiledMap = tiledMap;
		lvlModel = new LevelModel();

		lvlModel.setDebug(SaveHandler.getGameData().getIsDebug());

		lvlRender = new ViewRender(lvlModel, sb);

		isPaused = false;
		
		// set up box2d parts
		world = new World(new Vector2(0,-9.81f), true);
		cl = new MyContactListener(this);
		world.setContactListener(cl);
		b2br = new Box2DDebugRenderer();
		
		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, EGA.V_WIDTH / PPM, EGA.V_HEIGTH / PPM);
		

		debug = SaveHandler.getGameData().getIsDebug();
		
		
		entities = new Array<EntityController>(); 

		//create controllers for the game and set the spritebatch
		chc = new CharacterController(new CharacterModel(), new CharacterView());
		chc.setSpriteBatch(getSb());
		
		
		//set up the game timer
		EGATimer.resetTimer();
		timer = EGATimer.getTimer();
		etc = new EGATimerController(timer, new EGATimerView());
		etc.setSpriteBatch(sb);
		
		createEntities();
		timer.startTimer();
	}
	
	@Override
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
				EventSupport.getInstance().fireNewEvent("pause");
				isPaused = true;
				timer.stopTimer();
			}
			
			break;

		case MyInput.BUTTON_RESTART:
			EventSupport.getInstance().fireNewEvent("level", 0);
		break;
		
		// Pause
		case MyInput.BUTTON_ESCAPE: 
			EventSupport.getInstance().fireNewEvent("pause");

			isPaused = true;
			timer.stopTimer();
		break;
		}
	}
	
	@Override
	public void perform(TheEvent evt){}

	/**
	 * Resumes timer
	 */
	public void resumeTimer(){
		timer.resumeTimer();
	}
	
	@Override
	public void update(float dt) {

		lvlModel.setDebug(SaveHandler.getGameData().getIsDebug());
		
		doorC.update(dt);

		
		if(isPaused){
			isPaused = false;
			resumeTimer();
		}
		
		if(!isPaused){

			world.step(dt, 6, 2);

			removeEntities();

			//update controllers
			chc.update(dt);

			for(EntityController ec: entities){
				ec.update(dt);
			}
		}

	}
	
	@Override
	public void render() {
		
		lvlRender.render(getCam(), tmr, world, b2br, b2dCam);
		
		chc.render();

		for(EntityController ec: entities){
			ec.render();
		}
		
		etc.render();
	}

	public void dispose() {}
	
	/**
	 * Loops through the array of bodies to remove, and removes the
	 * bodys in it.
	 */
	public void removeEntities(){
		Array<Body> bodies = cl.getBodiesToRemove();

		if(bodies.size >0){
			for(Body b: bodies){
				
				if(b.getUserData() instanceof StarController) collectedStar((StarController)b.getUserData());

				if(b.getUserData() instanceof KeyController) {
					getDoorC().setDoorIsLocked(false);
				}

				entities.removeValue((EntityController)b.getUserData(), true);
				world.destroyBody(b);
			}
		}
		bodies.clear();
	}
	
	
	/**
	 * Called when a star is collected
	 * @param s, the star that have been collected
	 */
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



	// 	CREATE METHODS --------------------------------------------------------------
	public void createEntities(){
		createPlayer();
		createTiles();
		createTimer();
		createStars();
		createSpikes();
		createKey();
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

		chc.setBody(body);

		body.setUserData(chc);
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
	
	public void createTimer(){
		BodyDef bdef = new BodyDef();
		bdef.position.set((EGA.V_WIDTH/2)/PPM, (EGA.V_HEIGTH-60)/PPM);
		bdef.type = BodyType.StaticBody;
		Body body = world.createBody(bdef);

		etc.setBody(body);

		body.setUserData(etc);
	}

	
	
	/**
	 * Creates the body around the layer
	 * @param layer, what layer that should be surrounded with a body
	 * @param bits, what category bits that should be set to it
	 */
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

				Vector2[] v = createSquare(tilesize, tilesize);

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

		Vector2[] v = createSquare(EGA.V_WIDTH, EGA.V_HEIGTH);

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
	 * Creates a square out of vectors
	 * @param width, the width of the square
	 * @param heigth, the heigth of the square
	 * 
	 * @return a array of vectors
	 */
	public Vector2[] createSquare(float width, float heigth){
		Vector2[] v = new Vector2[5];
		v[0] = new Vector2(-width /2/ PPM, -heigth/2/PPM);
		v[1] = new Vector2(-width/2/PPM, heigth/2/PPM);
		v[2] = new Vector2(width/2/PPM, heigth/2/PPM);
		v[3] = new Vector2(width/2/PPM, -heigth/2/PPM);
		v[4] = new Vector2(-width/2/PPM, -heigth/2/PPM);
		return v;
		
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

	/**
	 * creates a body 
	 * @param mo, what object that should have the body
	 * 
	 * @return the body
	 */
	public Body createBody(MapObject mo){
		BodyDef bdef = new BodyDef();

		bdef.type = BodyType.StaticBody;

		float x = mo.getProperties().get("x", Float.class) / PPM;
		float y = mo.getProperties().get("y", Float.class) / PPM;

		bdef.position.set(x, y);

		return world.createBody(bdef);
	}
	
	/**
	 * Loops an entitylayer into the level
	 * @param layer, the layer
	 * @param ec, what kind of entity
	 */
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
			if(ec instanceof DoorController){
				theController = (DoorController)ec;
			}
			if(ec instanceof SpikeController){
				theController = new SpikeController(new SpikeModel(((SpikeController)ec).getSpikeOrientation()), 
															new SpikeView());
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

			theController.setSpriteBatch(getSb());
			theController.setBody(body);
			body.setUserData(theController);
			entities.add(theController);

		}
	}
	
	/**
	 * Creates the door
	 */
	private void createDoor(){

		MapLayer layer = tiledMap.getLayers().get("lockedDoor");
		doorM = new EntityModel();
		doorV = new DoorView();
		doorC = new DoorController(doorM, doorV);
		loopEntity(layer, doorC);

	}

	/**
	 * Creates the key
	 */
	private void createKey(){

		MapLayer layer = tiledMap.getLayers().get("key");
		loopEntity(layer, new KeyController(new EntityModel(), new KeyView()));
	
	}

	/**
	 * create spikes in all 4 orientations
	 */
	private void createSpikes(){
		MapLayer layer = tiledMap.getLayers().get("upSpikes");
		loopEntity(layer, new SpikeController(new SpikeModel(spikeOrientation.UP), new SpikeView()));

		layer = tiledMap.getLayers().get("downSpikes");
		loopEntity(layer, new SpikeController(new SpikeModel(spikeOrientation.DOWN), new SpikeView()));

		layer = tiledMap.getLayers().get("leftSpikes");
		loopEntity(layer, new SpikeController(new SpikeModel(spikeOrientation.LEFT), new SpikeView()));

		layer = tiledMap.getLayers().get("rightSpikes");
		loopEntity(layer, new SpikeController(new SpikeModel(spikeOrientation.RIGHT), new SpikeView()));
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
		body.setUserData(chc);
	}



	public Boolean getDoorIsOpen(){
		return doorIsOpen;
	}
}


