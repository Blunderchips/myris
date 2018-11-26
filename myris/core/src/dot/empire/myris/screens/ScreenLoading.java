package dot.empire.myris.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dot.empire.myris.Myris;
import dot.empire.myris.Screen;
import dot.empire.myris.gfx.GifDecoder;

import java.util.Locale;

import static dot.empire.myris.Defines.*;

public final class ScreenLoading extends Screen {

    private final Screen target;
    private final AssetManager mngr;
    private final Animation<TextureRegion> loadGif;
    private float elasped;

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
        Gdx.app.log(Myris.TAG, String.format(Locale.ENGLISH, "Loading %.2f", mngr.getProgress() * 100) + "%");
        if (mngr.update()) {
            changeScreen(target);
        }
    }

    @Override
    public void render(ShapeRenderer renderer, SpriteBatch batch) {
        TextureRegion tex = loadGif.getKeyFrame(elasped, true);
        batch.draw(tex,
                (SCREEN_WIDTH - tex.getRegionWidth()) / 2f,
                (SCREEN_HEIGHT - tex.getRegionHeight()) / 2f
        );
    }

    @Override
    public String getName() {
        return String.format("Loading => %s", target.getName());
    }
}
