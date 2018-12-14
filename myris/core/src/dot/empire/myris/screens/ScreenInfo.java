package dot.empire.myris.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.kotcrab.vis.ui.widget.VisImage;
import dot.empire.myris.Screen;
import dot.empire.myris.buttons.BtnToMain;

import static dot.empire.myris.Defines.IMG_TITLE;

/**
 *
 */
public final class ScreenInfo extends Screen {

    @Override
    public void show(AssetManager mngr) {
        add(new VisImage(mngr.get(IMG_TITLE, Texture.class)));

        row();

        add(new BtnToMain(mngr, this));
        super.show(mngr);
    }
}
