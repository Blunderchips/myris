package dot.empire.myris.buttons;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dot.empire.myris.Button;
import dot.empire.myris.Screen;
import dot.empire.myris.screens.ScreenMenuMain;

import static dot.empire.myris.Defines.ICO_BTN_BACK;

/**
 * Button to the {@link ScreenMenuMain}.
 *
 * @author Matthew 'siD' Van der Bijl
 * @see dot.empire.myris.screens.ScreenMenuMain
 */
public class BtnToMain extends Button {

    public BtnToMain(AssetManager mngr, final Screen parent) {
        super(ICO_BTN_BACK, mngr, new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                parent.changeScreen(ScreenMenuMain.class);
            }
        });
    }
}
