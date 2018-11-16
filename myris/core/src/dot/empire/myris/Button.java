package dot.empire.myris;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kotcrab.vis.ui.widget.VisImageButton;

/**
 *
 */
public abstract class Button extends VisImageButton {

    public Button(String icon, AssetManager mngr, ChangeListener listener) {
        super(new SpriteDrawable(new Sprite(mngr.get(icon, Texture.class))));
        super.addListener(listener);
        // TODO: 16 Nov 2018 Tooltip text from parent class name
    }
}
