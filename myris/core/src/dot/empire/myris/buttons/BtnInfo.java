package dot.empire.myris.buttons;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dot.empire.myris.Button;
import dot.empire.myris.Screen;
import dot.empire.myris.screens.ScreenInfo;

import static dot.empire.myris.Defines.ICO_INFO;

/**
 * Button to the Information Screen.
 *
 * @see dot.empire.myris.screens.ScreenInfo
 * @see dot.empire.myris.Defines#ICO_INFO
 */
public final class BtnInfo extends Button {

    public BtnInfo(AssetManager mngr, final Screen parnet) {
        super(ICO_INFO, mngr, new ChangeListener() {

            @Override
            public void changed(ChangeEvent evt, Actor actor) {
                parnet.changeScreen(ScreenInfo.class);
            }
        });
    }
}
