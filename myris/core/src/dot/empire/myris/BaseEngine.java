package dot.empire.myris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.VisUI;
import dot.empire.myris.screens.ScreenLoading;
import dot.empire.myris.screens.ScreenMenuMain;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

import static com.badlogic.gdx.Application.LOG_DEBUG;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

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

    private Stage uiLayer;
    private Screen screen;

    private ShapeRenderer renderer;
    private ShaderBatch batch;
    private AnnotationAssetManager assetManager;

    private FrameBuffer fbo;
    private Sprite display;
    private float alpha;

    @Override
    public void create() {
        Gdx.app.setLogLevel(LOG_DEBUG);

        VisUI.load(VisUI.SkinScale.X2);
        this.uiLayer = new Stage(); // TODO: 12 Nov 2018 Set constructor values
        this.assetManager = new AnnotationAssetManager();

        this.renderer = new ShapeRenderer();
        this.renderer.setAutoShapeType(true);
        // this.renderer.setColor(Color.BLACK);

        this.batch = new ShaderBatch(1);

        final int width = Gdx.graphics.getWidth();
        final int height = Gdx.graphics.getHeight();

        this.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        this.display = new Sprite(fbo.getColorBufferTexture());
        this.display.flip(false, true);

        // Do last
        this.setScreen(new ScreenMenuMain());
    }

    @Override
    public void render() {
        final float dt = Gdx.graphics.getDeltaTime();
        this.screen.update(dt);
        this.setAlpha(alpha + (dt * 10));

        this.fbo.begin();
        {
            // https://www.color-hex.com/color/fbfbfb
            Gdx.gl.glClearColor(
                    251 / 255f,
                    251 / 255f,
                    251 / 255f,
                    1
            );
            Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

            this.batch.begin(false);
            this.renderer.begin(ShapeRenderer.ShapeType.Filled);
            {
                this.screen.render(renderer, batch);
            }
            this.renderer.end();
            this.batch.end();

            this.uiLayer.draw(); // render last
        }
        this.fbo.end();

        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        this.batch.begin(true);
        {
            this.display.draw(batch, alpha);
        }
        this.batch.end();
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.fbo.dispose();
        this.display.getTexture().dispose();

        this.screen.dispose();
        this.assetManager.dispose();
        this.renderer.dispose();
        this.batch.dispose();

        this.uiLayer.dispose();
        VisUI.dispose();
        Gdx.app.log(BaseEngine.TAG, "Goodbye(:");
    }

    public void setScreen(Screen screen) {
        this.setAlpha(0);
        screen.setEngine(this);
        this.assetManager.load(screen);

        if (!assetManager.update() && !(screen instanceof ScreenLoading)) {
            // this.assetManager.finishLoading();
            setScreen(new ScreenLoading(screen, assetManager));
            return;
        }
        if (this.screen != null) {
            this.screen.dispose();
        }
        this.screen = screen;
        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(this.screen));
        Gdx.app.log(BaseEngine.TAG, "Screen = " + this.screen.getName());
        this.screen.show(assetManager);
    }

    public void setAlpha(float alpha) {
        this.alpha = MathUtils.clamp(alpha, 0, 1);
    }

    public Stage getUILayer() {
        return this.uiLayer;
    }
}
