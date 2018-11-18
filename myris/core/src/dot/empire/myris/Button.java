package dot.empire.myris;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisImageButton;

/**
 * Predefined {@code Button}.
 *
 * @see TableUtils#setSpacingDefaults(Table)
 */
public abstract class Button extends VisImageButton {

    /**
     * @param icon     Path to {@link Texture}
     * @param mngr     {@link Myris#assetManager}
     * @param listener Invoked on click
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public Button(String icon, AssetManager mngr, ChangeListener listener) {
        super(new SpriteDrawable(new Sprite(mngr.get(icon, Texture.class))));
        super.addListener(listener);
        TableUtils.setSpacingDefaults(this);
        // TODO: 16 Nov 2018 Tooltip text from parent class name
    }
}
