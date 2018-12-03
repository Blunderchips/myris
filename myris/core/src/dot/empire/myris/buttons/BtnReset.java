package dot.empire.myris.buttons;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dot.empire.myris.Button;
import dot.empire.myris.Screen;

import static dot.empire.myris.Defines.ICO_BTN_RESET;

public class BtnReset extends Button {

    // FIXME: 03 Dec 2018
    public BtnReset(AssetManager mngr, final RestListener listener) {
        super(ICO_BTN_RESET, mngr, (Screen) listener, new ChangeListener() {

            @Override
            public void changed(ChangeEvent evt, Actor actor) {
                listener.reset_();
            }
        });
    }

    public interface RestListener {

        // reset() in use
        void reset_();
    }
}
