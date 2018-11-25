package dot.empire.myris.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;
import dot.empire.myris.Screen;
import dot.empire.myris.buttons.BtnInfo;
import dot.empire.myris.buttons.BtnPlay;
import dot.empire.myris.buttons.BtnScore;
import dot.empire.myris.buttons.BtnSettings;

import static dot.empire.myris.Defines.BG_MUSIC;
import static dot.empire.myris.Defines.IMG_TITLE;

public final class ScreenMenuMain extends Screen {

    private Music bgMusic;

    @Override
    public void show(AssetManager mngr) {
        this.bgMusic = mngr.get(BG_MUSIC);
        this.bgMusic.play();

        align(Align.center).add(new VisImage(mngr.get(IMG_TITLE, Texture.class))).fillX().row();

        VisTable tblButtons = new VisTable(true);

        tblButtons.add(new BtnPlay(mngr, this));
        tblButtons.add(new BtnScore(mngr, this));

        tblButtons.row();

        tblButtons.add(new BtnSettings(mngr, this));
        tblButtons.add(new BtnInfo(mngr, this));

        super.add(tblButtons);
    }

    @Override
    public void changeScreen(Screen screen) {
        if (screen instanceof ScreenGame) {
            this.bgMusic.stop();
        }
        super.changeScreen(screen);
    }
}
