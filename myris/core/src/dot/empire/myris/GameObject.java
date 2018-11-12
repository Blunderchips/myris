package dot.empire.myris;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

/**
 * Definition of anything that can be updated and/or rendered by the game engine.
 * Created 07/11/2018.
 *
 * @author Matthew 'siD' Van der Bijl
 */
public interface GameObject extends Disposable {

    /**
     * @param dt Delta Time is the time it takes for the computer to go through all the
     *           processing/rendering for a single frame. It is dynamically updated, so it
     *           can fluctuate depending on what level of processing the last frame
     *           required
     *
     * @see Graphics#getDeltaTime()
     */
    void update(final float dt);

    /**
     * @param renderer Used to render shapes to the screen
     * @param batch    Used to render textures to the screen
     *
     * @see dot.empire.myris.BaseEngine#renderer
     * @see dot.empire.myris.BaseEngine#batch
     */
    void render(ShapeRenderer renderer, SpriteBatch batch);
}
