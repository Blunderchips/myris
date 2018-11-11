package dot.empire.myris.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dot.empire.myris.BaseEngine;
import dot.empire.myris.GifDecoder;
import dot.empire.myris.Screen;

public final class ScreenLoading extends Screen {

    private static final String LOADING_GIF = "gfx/cube-1.3s-200px.gif";

    private Animation<TextureRegion> loadGif;
    private float elasped; // TODO: 11 Nov 2018 Clamp/reset

    public ScreenLoading(BaseEngine engine) {
        super(engine);
        this.elasped = 0;
        this.loadGif = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP,
                Gdx.files.internal(LOADING_GIF).read());
    }

    @Override
    public void update(float dt) {
        this.elasped += dt;
    }

    @Override
    public void render(ShapeRenderer renderer, SpriteBatch batch) {
        TextureRegion tex = loadGif.getKeyFrame(elasped, true);
        batch.draw(tex,
                (Gdx.graphics.getWidth() - tex.getRegionWidth()) / 2f,
                (Gdx.graphics.getHeight() - tex.getRegionHeight()) / 2f
        );
    }

//    @Override
//    public String getName() {
//        return String.format("Loading => %s", parent.getName());
//    }
}
