package controller;

import static model.Variables.PPM;
import view.entities.CharacterView;
import view.entities.EGATimerView;
import view.entities.KeyView;
import view.entities.SpikeView;
import view.entities.StarView;
import view.renders.LevelRender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import controller.entities.CharacterController;
import controller.entities.EGATimerController;
import controller.entities.DoorController;
import controller.entities.EntityController;
import controller.entities.KeyController;
import controller.entities.SpikeController;
import controller.entities.StarController;
import controller.savehandler.SaveHandler;
import controller.superstate.GameState;
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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import view.entities.DoorView;
import event.EventSupport;
import event.TheEvent;

@Data
@EqualsAndHashCode(callSuper=false)
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

	//Array with the entities
	private Array<EntityController> entities;

	
	private EGATimer timer;
	//private EGATimerView timerView;
	private EGATimerController etc;
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
	
	private Boolean keyIsTaken;

	//MVC Doors
	private DoorController doorC; //ldc
	private DoorView doorV; //ldv
	private EntityModel doorM;

	private LevelRender lvlRender;
	private LevelModel lvlModel;
	
	private EGA game;

	//public Level(GameStateManager gsm){
	public Level(EGA game ,TiledMap tiledMap){

		//super(gsm);
		super();
		this.game = game;
		//this.gsm = gsm;
		this.tiledMap = tiledMap;
		lvlModel = new LevelModel();

		lvlModel.setDebug(SaveHandler.getGameData().getIsDebug());

		lvlRender = new LevelRender(lvlModel, getSb());
		//doorIsOpen = false;
		isPaused = false;
		
		// set up box2d stuff
		world = new World(new Vector2(0,-9.81f), true);
		cl = new MyContactListener(this);
		world.setContactListener(cl);
		b2br = new Box2DDebugRenderer();

		entities = new Array<EntityController>(); 

		//create controllers for the game and set the spritebatch
		chc = new CharacterController(new CharacterModel(), new CharacterView());
		chc.setSpriteBatch(getSb());
		
		debug = SaveHandler.getGameData().getIsDebug();
		
		// set up box2d cam
		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, EGA.V_WIDTH / PPM, EGA.V_HEIGTH / PPM);
		//set up the game timer
		EGATimer.resetTimer();
		timer = EGATimer.getTimer();
		etc = new EGATimerController(timer, new EGATimerView());
		etc.setSpriteBatch(sb);
		
		createEntities();
		timer.startTimer();
	}

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
				//gsm.pushState((new MenuFactory()).getMenu("pause", gsm));
				isPaused = true;
				timer.stopTimer();
			}
			
			break;

		case MyInput.BUTTON_RESTART: //gsm.setState(new Level(gsm, gsm.getCurrentTiledMap()));
			EventSupport.getInstance().fireNewEvent("level", 0);
		break;
		
		// Pause
		case MyInput.BUTTON_ESCAPE: 
			EventSupport.getInstance().fireNewEvent("pause");

			//gsm.pushState((new MenuFactory()).getMenu("pause", gsm));
			isPaused = true;
			timer.stopTimer();
		break;
		}
	}
	
	@Override
	public void perform(TheEvent evt){}

	public void setIsPaused(boolean b){
		isPaused = false;
	}

	public void resumeTimer(){
		timer.resumeTimer();
	}

	public void update(float dt) {

		lvlModel.setDebug(SaveHandler.getGameData().getIsDebug());

		//lvlModel.update();
		
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

	public void render() {
		
		lvlRender.render(getCam(), tmr, world, b2br, b2dCam);
		
		chc.render();

		for(EntityController ec: entities){
			ec.render();
		}
		
		etc.render();
	}


	public void dispose() {}

	public void removeEntities(){
		Array<Body> bodies = cl.getBodiesToRemove();

		if(bodies.size >0){
			for(Body b: bodies){
				
				if(b.getUserData() instanceof StarController) collectedStar((StarController)b.getUserData());

				if(b.getUserData() instanceof KeyController) {
					getDoorC().setDoorIsLocked(false);
				}
				//if(b.getUserData() instanceof DoorController) doorV.setDoorIsLocked(false);;

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



	// 	CREATE METHODS --------------------------------------------------------------
	public void createEntities(){
		createPlayer();
		createTiles();
		createTimer();
		createMapObjects();
	}

	public void createMapObjects(){

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
//			if(ec instanceof OpenDoorController){
//				theController = new OpenDoorController(new EntityModel(), new OpenDoorView());
//			}
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

	private void createDoor(){

		MapLayer layer = tiledMap.getLayers().get("lockedDoor");
		doorM = new EntityModel();
		doorV = new DoorView();
		doorC = new DoorController(doorM, doorV);
		loopEntity(layer, doorC);

	}


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

	private void setDoorIsOpen(boolean b){
		doorIsOpen = b;
	}

	public Boolean getDoorIsOpen(){
		return doorIsOpen;
	}
}


