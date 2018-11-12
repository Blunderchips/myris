package dot.empire.myris.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dot.empire.myris.Screen;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

public final class ScreenMenuMain extends Screen {

    @AnnotationAssetManager.Asset(Music.class)
    private static final String BG_MUSIC = "Kai_Engel_-_01_-_Brand_New_World.mp3";

    private Music bgMusic;

    @Override
    public void show(AnnotationAssetManager mngr) {
        this.bgMusic = mngr.get(BG_MUSIC);
        this.bgMusic.play();
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isTouched()) {
            changeScreen(ScreenGame.class);
        }
    }

    @Override
    public void render(ShapeRenderer renderer, SpriteBatch batch) {
        renderer.setColor(Color.RED);
        renderer.rect(100, 100, 100, 100);
    }

    @Override
    public void dispose() {
        this.bgMusic.stop();
    }
}
