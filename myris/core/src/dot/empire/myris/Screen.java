package dot.empire.myris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

/**
 * @author Matthew 'siD' Van der Bijl
 */
public class Screen implements GameObject, SimpleDirectionGestureDetector.DirectionListener {

    public Screen() {
    }

    public void show(AnnotationAssetManager mngr) {
    }

    /**
     * @param dt Delta time
     */
    @Override
    public void update(final float dt) {
    }

    @Override
    public void render(final ShapeRenderer renderer) {
    }

    /**
     * Releases all resources of this object.
     *
     * @see com.badlogic.gdx.utils.Disposable
     */
    @Override
    public void dispose() {
    }

    public boolean load(AnnotationAssetManager mngr) {
        return !mngr.update();
    }

    @Override
    public String toString() {
        return String.format("Screen[%s]", getName());
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void onLeft() {
        Gdx.app.debug("DirectionListener", "LEFT");
    }

    @Override
    public void onRight() {
        Gdx.app.debug("DirectionListener", "RIGHT");
    }

    @Override
    public void onUp() {
        Gdx.app.debug("DirectionListener", "UP");
    }

    @Override
    public void onDown() {
        Gdx.app.debug("DirectionListener", "DOWN");
    }
}
