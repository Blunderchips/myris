package dot.empire.myris.screens;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;
import dot.empire.myris.Screen;
import dot.empire.myris.buttons.BtnInfo;
import dot.empire.myris.buttons.BtnPlay;
import dot.empire.myris.buttons.BtnScore;
import dot.empire.myris.buttons.BtnSettings;

import static dot.empire.myris.Defines.BG_MUSIC;
import static dot.empire.myris.Defines.IMG_TITLE;
import static dot.empire.myris.Defines.Messages.MUTE;
import static dot.empire.myris.Defines.Messages.UNMUTE;

public final class ScreenMenuMain extends Screen implements Telegraph {

    private Music bgMusic;

    @Override
    public void show(AssetManager mngr) {
        MessageManager.getInstance().addListener(this, MUTE);
        MessageManager.getInstance().addListener(this, UNMUTE);

        this.bgMusic = mngr.get(BG_MUSIC);
        play(bgMusic);

        add(new VisImage(mngr.get(IMG_TITLE, Texture.class)));

        row();

        VisTable tblButtons = new VisTable(true);

        tblButtons.add(new BtnPlay(mngr, this));
        tblButtons.add(new BtnScore(mngr, this));

        tblButtons.row();

        tblButtons.add(new BtnSettings(mngr, this));
        tblButtons.add(new BtnInfo(mngr, this));

        add(tblButtons);
    }

    @Override
    public void changeScreen(Screen screen) {
        if (screen instanceof ScreenGame) {
            this.bgMusic.stop();
        }
        super.changeScreen(screen);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message) {
            case MUTE:
                if (bgMusic.isPlaying()) {
                    this.bgMusic.pause();
                }
                return true;
            case UNMUTE:
                if (!bgMusic.isPlaying()) {
                    this.bgMusic.play();
                }
                return true;
        }
        return false;
    }

    @Override
    public void dispose() {
        MessageManager.getInstance().removeListener(this, MUTE);
        MessageManager.getInstance().removeListener(this, UNMUTE);
        super.dispose();
    }
}
