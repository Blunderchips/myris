package dot.empire.myris.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.kotcrab.vis.ui.widget.VisImage;
import dot.empire.myris.Screen;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

/**
 * Screen shown on start up. Created 14/11/2018.
 *
 * @author Matthew 'siD' Van der Bijl
 */
public final class ScreenSplash extends Screen {

    @AnnotationAssetManager.Asset(Texture.class)
    private static final String IMG_LOGO = "gfx/logo.png";
    @AnnotationAssetManager.Asset(Music.class)
    private static final String SFX_INTRO = "sfx/intro.ogg";

    /**
     * @see Music#isPlaying()
     */
    private Music sfxIntro;

    @Override
    public void show(AssetManager mngr) {
        add(new VisImage(mngr.get(IMG_LOGO, Texture.class))).center().fillX();

        this.sfxIntro = mngr.get(SFX_INTRO, Music.class);
        this.sfxIntro.play();
    }

    @Override
    public void update(float dt) {
        if (!sfxIntro.isPlaying() || Gdx.input.isTouched()) {
            changeScreen(ScreenMenuMain.class);
        }
    }

    @Override
    public void dispose() {
        this.sfxIntro.stop();
        super.dispose();
    }
}
