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

    // TODO: 11 Nov 2018 Pull generic dt definition
    /**
     * @param dt Delta time
     * @see Graphics#getDeltaTime()
     */
    void update(final float dt);

    /**
     * @param renderer used to render shapes to the screen
     * @see dot.empire.myris.BaseEngine#renderer
     * @see dot.empire.myris.BaseEngine#batch
     */
    void render(final ShapeRenderer renderer, final SpriteBatch batch);
}
