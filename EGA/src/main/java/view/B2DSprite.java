package view;

import lombok.Data;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import controller.B2DVars;


public interface B2DSprite {
	
	public void setAnimation(TextureRegion[] reg, float delay);
	public void update(float dt);
	public abstract void render(SpriteBatch sb);
	public abstract Vector2 getPosition(); 
}
