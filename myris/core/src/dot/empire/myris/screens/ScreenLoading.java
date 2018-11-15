package dot.empire.myris.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dot.empire.myris.BaseEngine;
import dot.empire.myris.Screen;
import dot.empire.myris.gfx.GifDecoder;

import java.util.Locale;

import static dot.empire.myris.Defines.LOADING_GIF;

public final class ScreenLoading extends Screen {

    private final Screen target;
    private final AssetManager mngr;
    private final Animation<TextureRegion> loadGif;
    private float elasped; // TODO: 11 Nov 2018 Clamp/reset

    public ScreenLoading(Screen target, AssetManager mngr) {
        this.mngr = mngr;
        this.elasped = 0;
        this.target = target;

        this.loadGif = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP,
                Gdx.files.internal(LOADING_GIF).read());
    }

    @Override
    public void update(float dt) {
        this.elasped += dt;
        Gdx.app.log(BaseEngine.TAG, String.format(Locale.ENGLISH, "Loading %.2f", mngr.getProgress() * 100) + "%");
        if (mngr.update()) {
            changeScreen(target);
        }
    }

    @Override
    public void render(ShapeRenderer renderer, SpriteBatch batch) {
        TextureRegion tex = loadGif.getKeyFrame(elasped, true);
        batch.draw(tex,
                (Gdx.graphics.getWidth() - tex.getRegionWidth()) / 2f,
                (Gdx.graphics.getHeight() - tex.getRegionHeight()) / 2f
        );
    }

    @Override
    public String getName() {
        return String.format("Loading => %s", target.getName());
    }
}
