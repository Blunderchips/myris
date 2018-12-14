package dot.empire.myris.buttons;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import dot.empire.myris.Button;
import dot.empire.myris.Screen;

import static dot.empire.myris.Defines.ICO_INFO;
import static dot.empire.myris.Defines.Messages.OPEN_ABOUT;

/**
 * Opens GitHub page.
 *
 * @see dot.empire.myris.Defines#ICO_INFO
 * @see dot.empire.myris.Defines.Messages#OPEN_ABOUT
 */
public final class BtnInfo extends Button {

    public BtnInfo(AssetManager mngr, final Screen parent) {
        super(ICO_INFO, mngr, parent, new ChangeListener() {

            @Override
            public void changed(ChangeEvent evt, Actor actor) {
                MessageManager.getInstance().dispatchMessage(OPEN_ABOUT);
            }
        });
    }
}
