package dot.empire.myris.buttons;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dot.empire.myris.Button;
import dot.empire.myris.Screen;
import dot.empire.myris.screens.ScreenScore;

import static dot.empire.myris.Defines.ICO_SCORE;

/**
 * Button to the score screen.
 *
 * @see dot.empire.myris.screens.ScreenScore
 * @see dot.empire.myris.Defines#ICO_SCORE
 */
public final class BtnScore extends Button {

    public BtnScore(AssetManager mngr, final Screen parent) {
        super(ICO_SCORE, mngr, new ChangeListener() {

            @Override
            public void changed(ChangeEvent evt, Actor actor) {
                parent.changeScreen(ScreenScore.class);
            }
        });
    }
}
