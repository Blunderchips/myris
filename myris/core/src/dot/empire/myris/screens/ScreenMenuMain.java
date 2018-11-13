package dot.empire.myris.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import dot.empire.myris.Screen;
import net.dermetfan.gdx.assets.AnnotationAssetManager;
import org.jetbrains.annotations.NotNull;

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
    public void show(@NotNull AssetManager mngr) {
        this.bgMusic = mngr.get(BG_MUSIC);
        this.bgMusic.play();

        VisLabel lbl = new VisLabel("~ myris ~", Color.BLACK);
        add(lbl).align(Align.center).row();

        add(new VisImageButton(new TextureRegionDrawable(new TextureRegion(mngr.get(ICO_PLAY, Texture.class)))));
        add(new VisImageButton(new TextureRegionDrawable(new TextureRegion(mngr.get(ICO_SCORE, Texture.class)))));

        row();

        add(new VisImageButton(new TextureRegionDrawable(new TextureRegion(mngr.get(ICO_SETTINGS, Texture.class)))));
        add(new VisImageButton(new TextureRegionDrawable(new TextureRegion(mngr.get(ICO_INFO, Texture.class)))));
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isTouched()) {
            changeScreen(ScreenGame.class);
        }
    }

    @Override
    public void dispose() {
        this.bgMusic.stop();
        super.dispose();
    }
}
