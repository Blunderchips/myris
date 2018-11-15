/*                       _
 *  _ __ ___  _   _ _ __(_)___
 * | '_ ` _ \| | | | '__| / __|
 * | | | | | | |_| | |  | \__ \
 * |_| |_| |_|\__, |_|  |_|___/
 *            |___/
 */
package dot.empire.myris;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.VisUI;
import dot.empire.myris.gfx.ShaderBatch;
import dot.empire.myris.screens.ScreenLoading;
import dot.empire.myris.screens.ScreenSplash;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

import java.util.Locale;

import static com.badlogic.gdx.Application.LOG_DEBUG;
import static com.badlogic.gdx.graphics.GL20.*;

/**
 * Main game file. Created 07/11/2018.
 *
 * @author Matthew 'siD' Van der Bijl
 */
public final class BaseEngine extends ApplicationAdapter {

    /**
     *
     */
    public static final String TAG = "myris";

    private Stage uiLayer;
    private Screen screen;
    private InputMultiplexer input;

    private ShapeRenderer renderer;
    private ShaderBatch batch;
    private AnnotationAssetManager assetManager;
    private Preferences preferences;

    private FrameBuffer fbo;
    private Sprite display;
    private float alpha;

    @Override
    public void create() {
        Gdx.app.setLogLevel(LOG_DEBUG);

        VisUI.load(VisUI.SkinScale.X2);
        this.uiLayer = new Stage(); // TODO: 12 Nov 2018 Set constructor values
        this.assetManager = new AnnotationAssetManager();
        this.preferences = Gdx.app.getPreferences(BaseEngine.TAG);

        this.input = new InputMultiplexer();
        Gdx.input.setInputProcessor(input);

        this.renderer = new ShapeRenderer();
        this.renderer.setAutoShapeType(true);
        // this.renderer.setColor(Color.BLACK);

        this.batch = new ShaderBatch(1);
        this.batch.enableBlending();
        this.batch.setBlendFunction(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        this.setBrightness(preferences.getFloat("brightness", 0));
        this.setContrast(preferences.getFloat("contrast", 1));
        Gdx.app.debug(BaseEngine.TAG, "Sprite batch = " + batch.toString());

        final int width = Gdx.graphics.getWidth();
        final int height = Gdx.graphics.getHeight();

        this.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        this.display = new Sprite(fbo.getColorBufferTexture());
        this.display.flip(false, true);

        // this.assetManager.load(ScreenMenuMain.class);
        // this.assetManager.load(ScreenGame.class);

        // Do last
        assetManager.load(new Defines()); // siD 15/11/2018: hacky but works
        this.setScreen(new ScreenSplash());
    }

    @Override
    public void render() {
        final float dt = Gdx.graphics.getDeltaTime();
        this.uiLayer.act(dt);
        this.screen.update(dt);
        this.setAlpha(alpha + (dt * 8));

        this.fbo.begin();
        {
            Gdx.gl.glClearColor(
                    245 / 255f,
                    245 / 255f,
                    245 / 255f,
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
        this.preferences.flush(); // just to be safe

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
            this.input.clear();
            this.screen.dispose();
        }
        this.getUILayer().addActor(screen);
        this.screen = screen;

        this.input.addProcessor(uiLayer);
        this.input.addProcessor(new SimpleDirectionGestureDetector(this.screen));

        Gdx.app.log(BaseEngine.TAG, "Screen = " + this.screen.getName());
        this.screen.show(assetManager);
    }

    public void setAlpha(float alpha) {
        this.alpha = MathUtils.clamp(alpha, 0, 1);
    }

    public Stage getUILayer() {
        return this.uiLayer;
    }

    public void setContrast(float contrast) {
        contrast /= 100;

        Gdx.app.log(BaseEngine.TAG, String.format(Locale.ENGLISH, "Contrast = %.2f", contrast));
        //this.batch.setContrast(contrast);
        this.preferences.putFloat("contrast", batch.getContrast());
        this.preferences.flush();
    }

    public void setBrightness(float brightness) {
        brightness /= 100;

        Gdx.app.log(BaseEngine.TAG, String.format(Locale.ENGLISH, "Brightness = %.2f", brightness));
        //this.batch.setBrightness(brightness);
        this.preferences.putFloat("brightness", batch.getBrightness());
        this.preferences.flush();
    }

    public Preferences getPreferences() {
        return this.preferences;
    }
}
