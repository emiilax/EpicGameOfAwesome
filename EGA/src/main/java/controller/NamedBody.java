package controller;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class NamedBody extends Body {
	
	String name;
	Body body;
	
	public NamedBody(World world, long l){
		super(world,l);
	}
	
	public NamedBody(World world, long l, String name){
		super(world,l);
		
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String newName){
		this.name = newName;
	}
}
