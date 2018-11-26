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
 * Predefined {@code Button}. Using VisUI default table spacing. All subclasses should be final.
 *
 * @see dot.empire.myris.buttons
 * @see TableUtils#setSpacingDefaults(Table)
 */
public abstract class Button extends VisImageButton {

    /**
     * @param icon     Path to {@link Texture}
     * @param mngr     {@link net.dermetfan.gdx.assets.AnnotationAssetManager} from parameter
     * @param listener Invoked on click
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public Button(String icon, AssetManager mngr, ChangeListener listener) {
        super(new SpriteDrawable(new Sprite(mngr.get(icon, Texture.class))));
        super.addListener(listener);
        TableUtils.setSpacingDefaults(this); // LeakingThisInConstructor
        // TODO: 16 Nov 2018 Tooltip text from parent class name
        // TODO: 26 Nov 2018 Maybe
    }
}
