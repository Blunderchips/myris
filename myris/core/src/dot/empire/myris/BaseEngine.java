package dot.empire.myris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
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

    private FrameBuffer fbo;
    private TextureRegion fboRegion;

    @Override
    public void create() {
        Gdx.app.setLogLevel(LOG_DEBUG);

        VisUI.load();

        this.renderer = new ShapeRenderer();
        this.renderer.setAutoShapeType(true);
        // this.renderer.setColor(Color.BLACK);

        this.batch = new ShaderBatch(1);

        this.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(), false);
        this.fboRegion = new TextureRegion(fbo.getColorBufferTexture(), 0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.fboRegion.flip(false, true);

        this.assetManager = new AnnotationAssetManager();

        // Do last
        this.setScreen(new ScreenMenuMain(this));
    }

    @Override
    public void render() {
        this.screen.update(Gdx.graphics.getDeltaTime());

        this.fbo.begin();
        {
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
        this.fbo.end();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.batch.begin(true);
        {
            this.batch.draw(fboRegion, 0, 0);
        }
        this.batch.end();
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.fbo.dispose();
        this.fboRegion.getTexture().dispose();

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

    // public void setAlpha(float alpha) {
    //     this.alpha = 1 - MathUtils.clamp(alpha, 0, 1);
    // }
}
