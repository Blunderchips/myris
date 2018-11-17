package dot.empire.myris.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import dot.empire.myris.Screen;
import dot.empire.myris.buttons.BtnPlay;

import static dot.empire.myris.Defines.*;

public final class ScreenMenuMain extends Screen {

    private Music bgMusic;

    @Override
    public void show(AssetManager mngr) {
        this.bgMusic = mngr.get(BG_MUSIC);
        this.bgMusic.play();

        align(Align.center).add(new VisImage(mngr.get(IMG_TITLE, Texture.class))).fillX().row();

        VisTable tblButtons = new VisTable(true);

        tblButtons.add(new BtnPlay(mngr, this));
        createButton(ICO_SCORE, new BtnScore(), mngr, tblButtons);

        tblButtons.row();

        createButton(ICO_SETTINGS, new BtnSettings(), mngr, tblButtons);
        createButton(ICO_INFO, new BtnInfo(), mngr, tblButtons);

        super.add(tblButtons);
    }

    private void createButton(String img, ChangeListener listener, AssetManager mngr, VisTable tbl) {
        VisImageButton btn = new VisImageButton(new TextureRegionDrawable(new TextureRegion(mngr.get(img, Texture.class))));
        btn.addListener(listener);
        tbl.add(btn);
    }


//    @Override
//    public void update(float dt) {
//        if (Gdx.input.isTouched()) {
//            changeScreen(ScreenGame.class);
//        }
//    }


    @Override
    public void dispose() {
        this.bgMusic.stop();
        super.dispose();
    }

    private class BtnSettings extends ChangeListener {

        @Override
        public void changed(ChangeEvent evt, Actor actor) {
            changeScreen(ScreenSettings.class);
        }
    }

    private class BtnScore extends ChangeListener {

        @Override
        public void changed(ChangeEvent evt, Actor actor) {

        }
    }

    private class BtnInfo extends ChangeListener {

        @Override
        public void changed(ChangeEvent evt, Actor actor) {

        }
    }
}
