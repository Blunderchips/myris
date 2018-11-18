package dot.empire.myris.buttons;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dot.empire.myris.Button;
import dot.empire.myris.Screen;
import dot.empire.myris.screens.ScreenSettings;

import static dot.empire.myris.Defines.ICO_SETTINGS;

/**
 * Button to the Settings Screen.
 *
 * @see dot.empire.myris.screens.ScreenSettings
 */
public final class BtnSettings extends Button {

    public BtnSettings(AssetManager mngr, final Screen parent) {
        super(ICO_SETTINGS, mngr, new ChangeListener() {

            @Override
            public void changed(ChangeEvent evt, Actor actor) {
                parent.changeScreen(ScreenSettings.class);
            }
        });
    }
}
