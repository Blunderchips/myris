package dot.empire.myris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dot.empire.myris.screens.ScreenTransition;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

/**
 * @author Matthew 'siD' Van der Bijl
 */
public class Screen implements GameObject, SimpleDirectionGestureDetector.DirectionListener {

    /**
     * Main game engine.
     */
    private final BaseEngine engine;

    public Screen(BaseEngine engine) {
        this.engine = engine;
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
    public void render(ShapeRenderer renderer, SpriteBatch batch) {
    }

    /**
     * Releases all resources of this object.
     *
     * @see com.badlogic.gdx.utils.Disposable
     */
    @Override
    public void dispose() {
    }

    // @Override
    // public String toString() {
    //    return String.format("Screen[%s]", getName());
    // }

    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void onLeft() {
        Gdx.app.debug("Direction Listener", "LEFT");
    }

    @Override
    public void onRight() {
        Gdx.app.debug("Direction Listener", "RIGHT");
    }

    @Override
    public void onUp() {
        Gdx.app.debug("Direction Listener", "UP");
    }

    @Override
    public void onDown() {
        Gdx.app.debug("Direction Listener", "DOWN");
    }

    public void changeScreen(final Class<? extends Screen> next) {
        Gdx.app.postRunnable(new Runnable() {

            @Override
            public void run() {
                Screen.this.engine.setScreen(new ScreenTransition(next, engine));
            }
        });
    }

    public BaseEngine getEngine() {
        return this.engine;
    }
}
