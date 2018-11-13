package dot.empire.myris.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import dot.empire.myris.Screen;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

public final class ScreenMenuMain extends Screen {

    @AnnotationAssetManager.Asset(Music.class)
    private static final String BG_MUSIC = "Kai_Engel_-_01_-_Brand_New_World.mp3";
    @AnnotationAssetManager.Asset(Texture.class)
    private static final String ICO_PLAY = "gfx/play-button.png";
    @AnnotationAssetManager.Asset(Texture.class)
    private static final String ICO_SCORE = "gfx/trophy-cup.png";
    @AnnotationAssetManager.Asset(Texture.class)
    private static final String ICO_SETTINGS = "gfx/cog.png";
    @AnnotationAssetManager.Asset(Texture.class)
    private static final String ICO_INFO = "gfx/info.png";

    private Music bgMusic;

    @Override
    public void show(AssetManager mngr) {
        this.bgMusic = mngr.get(BG_MUSIC);
        this.bgMusic.play();

        VisLabel lbl = new VisLabel("~ myris ~", Color.BLACK);
        add(lbl).align(Align.center).row();

        createButton(ICO_PLAY, new BtnPlay(), mngr);
        createButton(ICO_SCORE, new BtnScore(), mngr);

        row();

        createButton(ICO_SETTINGS, new BtnSettings(), mngr);
        createButton(ICO_INFO, new BtnInfo(), mngr);
    }

    private void createButton(String img, ChangeListener listener, AssetManager mngr) {
        VisImageButton btn = new VisImageButton(new TextureRegionDrawable(new TextureRegion(mngr.get(img, Texture.class))));
        btn.addListener(listener);
        super.add(btn);
    }


    @Override
    public void update(float dt) {
        if (Gdx.input.isTouched()) {
//            changeScreen(ScreenGame.class);
        }
    }


    @Override
    public void dispose() {
        this.bgMusic.stop();
        super.dispose();
    }

    private class BtnPlay extends ChangeListener {

        @Override
        public void changed(ChangeEvent evt, Actor actor) {
            changeScreen(ScreenGame.class);
        }
    }

    private class BtnSettings extends ChangeListener {

        @Override
        public void changed(ChangeEvent evt, Actor actor) {

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
