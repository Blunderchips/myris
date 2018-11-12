package dot.empire.myris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author Matthew 'siD' Van der Bijl
 */
public class Screen implements GameObject, SimpleDirectionGestureDetector.DirectionListener {

    /**
     * Main game engine.
     */
    private BaseEngine engine;

    public Screen() {
    }

    public void show(AssetManager mngr) {
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
    //    return String.format("Screen[%display]", getName());
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

    public void changeScreen(final Screen screen) {
        Gdx.app.postRunnable(new Runnable() {

            @Override
            public void run() {
                getEngine().setScreen(screen);
            }
        });
    }

    /**
     * @param screen The {@link Screen} to change to
     */
    public void changeScreen(Class<? extends Screen> screen) {
        try {
            changeScreen(screen.getConstructor().newInstance());
        } catch (Exception ex) { // TODO: 11 Nov 2018 Get specific exceptions
            Gdx.app.error(BaseEngine.TAG, "Cannot change next", ex);
        }
    }

    public BaseEngine getEngine() {
        return this.engine;
    }

    public void setEngine(BaseEngine engine) {
        this.engine = engine;
    }
}
