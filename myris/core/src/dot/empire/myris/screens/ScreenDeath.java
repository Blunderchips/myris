package dot.empire.myris.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dot.empire.myris.Screen;
import dot.empire.myris.gfx.Score;

/**
 * Show final score. Created 14/11/2018.
 */
public final class ScreenDeath extends Screen {

    private Score score;

    public ScreenDeath(Score score) {
        this.score = score;
    }

    @Override
    public void show(AssetManager mngr) {
        // add(new VisImageButton(new SpriteDrawable()));
    }
}
