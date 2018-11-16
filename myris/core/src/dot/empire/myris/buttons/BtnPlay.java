package dot.empire.myris.buttons;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dot.empire.myris.Button;
import dot.empire.myris.Screen;
import dot.empire.myris.screens.ScreenGame;

import static dot.empire.myris.Defines.ICO_PLAY;

public final class BtnPlay extends Button {

    public BtnPlay(AssetManager mngr, final Screen parent) {
        super(ICO_PLAY, mngr, new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(ScreenGame.class);
            }
        });
    }
}
