package dot.empire.myris.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.kotcrab.vis.ui.widget.VisImage;
import dot.empire.myris.Screen;

import static dot.empire.myris.Defines.IMG_LOGO;
import static dot.empire.myris.Defines.SFX_INTRO;

/**
 * Screen shown on start up. Created 14/11/2018.
 *
 * @author Matthew 'siD' Van der Bijl
 */
public final class ScreenSplash extends Screen {

    /**
     * @see Music#isPlaying()
     */
    private Music sfxIntro;

    @Override
    public void show(AssetManager mngr) {
        add(new VisImage(mngr.get(IMG_LOGO, Texture.class))).center().fillX();

        this.sfxIntro = mngr.get(SFX_INTRO, Music.class);
        play(sfxIntro);
        super.show(mngr);
    }

    @Override
    public void update(float dt) {
        if (/*!sfxIntro.isPlaying() ||*/ Gdx.input.isTouched()) {
            changeScreen(ScreenMenuMain.class);
        }
    }

    @Override
    public void dispose() {
        this.sfxIntro.stop();
        super.dispose();
    }
}
