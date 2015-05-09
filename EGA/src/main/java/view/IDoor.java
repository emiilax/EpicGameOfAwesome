package view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
 * @author Rebecka Reitmaier
 * 
 * interface for OpenDoor and ClosedDoor
 */
public interface IDoor {
	public void update(float dt);
	public void render(SpriteBatch sb);
}
