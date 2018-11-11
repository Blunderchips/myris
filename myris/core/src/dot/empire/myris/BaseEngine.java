package dot.empire.myris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kotcrab.vis.ui.VisUI;
import dot.empire.myris.screens.ScreenMenuMain;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

import static com.badlogic.gdx.Application.LOG_DEBUG;

/**
 * Main game file. Created 07/11/2018.
 *
 * @author Matthew 'siD' Van der Bijl
 */
public class BaseEngine extends ApplicationAdapter {

    /**
     *
     */
    public static final String TAG = "myris";

    private Screen screen;
    private ShapeRenderer renderer;
    private ShaderBatch batch;
    private AnnotationAssetManager assetManager;

    @Override
    public void create() {
        Gdx.app.setLogLevel(LOG_DEBUG);

        VisUI.load();

        this.renderer = new ShapeRenderer();
        this.renderer.setAutoShapeType(true);
        // this.renderer.setColor(Color.BLACK);

        this.batch = new ShaderBatch(1);

        this.assetManager = new AnnotationAssetManager();

        // Do last
        this.setScreen(new ScreenMenuMain(this));
    }

    @Override
    public void render() {
        this.screen.update(Gdx.graphics.getDeltaTime());

        // https://www.color-hex.com/color/fbfbfb
        Gdx.gl.glClearColor(
                251 / 255f,
                251 / 255f,
                251 / 255f,
                1
        );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.batch.begin(false);
        this.renderer.begin(ShapeRenderer.ShapeType.Filled);
        {
            this.screen.render(renderer, batch);
        }
        this.renderer.end();
        this.batch.end();
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.screen.dispose();
        this.assetManager.dispose();
        this.renderer.dispose();
        this.batch.dispose();

        VisUI.dispose();
    }

    public void setScreen(Screen screen) {
        this.assetManager.load(screen);
        this.assetManager.finishLoading(); // TODO: 08 Nov 2018 Loading screen
        if (this.screen != null) {
            this.screen.dispose();
        }
        this.screen = screen;
        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(this.screen));
        Gdx.app.log(BaseEngine.TAG, "Screen = " + this.screen.getName());
        this.screen.show(assetManager);
    }
}
