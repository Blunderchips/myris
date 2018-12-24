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
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import dot.empire.myris.gfx.ShaderBatch;
import dot.empire.myris.screens.ScreenLoading;
import dot.empire.myris.screens.ScreenSplash;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

import java.util.Locale;

import static com.badlogic.gdx.Application.LOG_DEBUG;
import static com.badlogic.gdx.graphics.GL20.*;
import static dot.empire.myris.Defines.Messages.*;
import static dot.empire.myris.Defines.SCREEN_HEIGHT;
import static dot.empire.myris.Defines.SCREEN_WIDTH;

/**
 * Main game file. Created 07/11/2018.
 *
 * @author Matthew 'siD' Van der Bijl
 */
public final class Myris extends ApplicationAdapter implements Disposable, Telegraph {

    /**
     * For logging. All other constants should be in {@link Defines}.
     */
    public static final String TAG = "myris";

    /**
     * Contains all VisUI components.
     */
    private Stage uiLayer;
    private Screen screen;
    private InputMultiplexer input;

    private ShapeRenderer renderer;
    private ShaderBatch batch;
    private AnnotationAssetManager assetManager;
    private Settings preferences;

    private FrameBuffer fbo;
    private Sprite display;
    private float alpha;

//    private float muteAlpha;
//    private Sprite icoMute;

    /**
     * @see OrthographicCamera
     */
    private Camera camera;
    /**
     * @see StretchViewport
     */
    private Viewport viewPort;

    public Myris() {
        this.alpha = 0;
//        this.muteAlpha = 1;
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(LOG_DEBUG);
        Gdx.app.log(Myris.TAG, String.format(Locale.ENGLISH, "Size = %dx%d",
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(input = new InputMultiplexer());

        MessageManager.getInstance().addListener(this, MUTE);
        MessageManager.getInstance().addListener(this, UNMUTE);
        MessageManager.getInstance().addListener(this, OPEN_ABOUT);


        this.preferences = new Settings();
        this.camera = new OrthographicCamera(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.viewPort = new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        this.viewPort.apply(true);
        this.camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        this.batch = new ShaderBatch(1);
        this.batch.enableBlending();
        this.batch.setBlendFunction(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        this.batch.setBrightness(preferences.getBrightness());
        this.batch.setContrast(preferences.getContrast());
        Gdx.app.debug(Myris.TAG, "Sprite batch = " + batch.toString());

        VisUI.load(VisUI.SkinScale.X2);
        this.uiLayer = new Stage(new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT), batch);
        this.assetManager = new AnnotationAssetManager();

        this.renderer = new ShapeRenderer();
        this.renderer.setAutoShapeType(true);

        this.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, SCREEN_WIDTH, SCREEN_HEIGHT, false);
        this.display = new Sprite(fbo.getColorBufferTexture());
        this.display.flip(false, true);

//        this.icoMute = new Sprite();
//        this.icoMute.setPosition(
//                (SCREEN_WIDTH - icoMute.getWidth()) / 2,
//                (SCREEN_HEIGHT - icoMute.getHeight()) / 2
//        );

        // Do last
        //assetManager.load(new Defines()); // siD 15/11/2018: hacky but works
        Defines.loadAllAssets(assetManager);
        this.setScreen(new ScreenSplash());
    }

    @Override
    public void render() {
        final float dt = Gdx.graphics.getDeltaTime();
        this.camera.update();
        this.uiLayer.act(dt);
        this.screen.update(dt);

        this.setAlpha(alpha + (dt * 8));
//        this.setMuteAlpha(muteAlpha - dt);

        this.fbo.begin();
        {
            Gdx.gl.glClearColor(
                    245 / 255f,
                    245 / 255f,
                    245 / 255f,
                    1
            );
            Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

            this.batch.setProjectionMatrix(camera.combined);
            this.batch.begin(false);
            this.renderer.setProjectionMatrix(camera.combined);
            this.renderer.begin(ShapeRenderer.ShapeType.Filled);
            {
                // viewPort.apply();
                this.screen.render(renderer, batch);
            }
            this.renderer.end();
            this.batch.end();

            this.uiLayer.draw();

//            if (muteAlpha != 0 && icoMute != null) {
//                this.batch.begin(false);
//                {
//                    Gdx.app.debug(Myris.TAG, "Render mute icon " + muteAlpha);
//                    this.icoMute.draw(batch, muteAlpha == 0 ? 0 : 1);
//                }
//                this.batch.end();
//            }
        }
        this.fbo.end();

        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        this.batch.setProjectionMatrix(camera.combined);
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
        // FIXME: 14 Dec 2018 Really needed?
        MessageManager.getInstance().removeListener(this, MUTE);
        MessageManager.getInstance().removeListener(this, UNMUTE);
        MessageManager.getInstance().removeListener(this, OPEN_ABOUT);
        // --

        this.fbo.dispose();
        this.display.getTexture().dispose();

        this.screen.dispose();
        this.assetManager.dispose();
        this.renderer.dispose();
        this.batch.dispose();

        this.uiLayer.dispose();
        VisUI.dispose();
        Gdx.app.log(Myris.TAG, "Goodbye(:");
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
        this.uiLayer.addActor(screen);
        this.screen = screen;

        this.input.addProcessor(uiLayer);
        this.input.addProcessor(new SimpleDirectionGestureDetector(this.screen));

        Gdx.app.log(Myris.TAG, "Screen = " + this.screen.getName());
        this.screen.show(assetManager);
    }

    public void setAlpha(float alpha) {
        this.alpha = MathUtils.clamp(alpha, 0, 1);
    }

//    public void setMuteAlpha(float alpha) {
//        this.muteAlpha = MathUtils.clamp(alpha, 0, 1);
//    }

    public void setContrast(float contrast) {
        if (contrast > 1) {
            contrast /= 100;
        }

        Gdx.app.log(Myris.TAG, String.format(Locale.ENGLISH, "Contrast = %.2f", contrast));
        this.batch.setContrast(contrast);
        this.preferences.setContrast(batch.getContrast());
    }

    public void setBrightness(float brightness) {
        if (brightness > 1) {
            brightness /= 100;
        }

        Gdx.app.log(Myris.TAG, String.format(Locale.ENGLISH, "Brightness = %.2f", brightness));
        this.batch.setBrightness(brightness);
        this.preferences.setBrightness(batch.getBrightness());
    }

    public Settings getPreferences() {
        return this.preferences;
    }

    @Override
    public void resize(int width, int height) {
        this.viewPort.update(width, height, true);
        this.camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    @Override
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public boolean handleMessage(Telegram msg) {
        Gdx.app.debug(Myris.TAG, String.format(Locale.ENGLISH, "Message = %d", msg.message));
        switch (msg.message) {
            case MUTE:
//                Gdx.app.postRunnable(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        setMuteAlpha(1);
//                        icoMute.setTexture(assetManager.get(ICO_MUTE, Texture.class));
//                    }
//                });
                this.preferences.setIsMuted(true);
                return true;
            case UNMUTE:
//                Gdx.app.postRunnable(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        setMuteAlpha(1);
//                        icoMute.setTexture(assetManager.get(ICO_UNMUTE, Texture.class));
//                    }
//                });
                this.preferences.setIsMuted(false);
                return true;
            case OPEN_ABOUT:
                Gdx.app.log(Myris.TAG, "Open about");
                return true;
        }
        return false;
    }
}
