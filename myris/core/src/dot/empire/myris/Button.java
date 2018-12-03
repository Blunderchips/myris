package dot.empire.myris;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
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

    private final Screen parent;

    /**
     * @param icon     Path to {@link Texture}
     * @param mngr     {@link net.dermetfan.gdx.assets.AnnotationAssetManager} from parameter
     * @param listener Invoked on click
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public Button(String icon, AssetManager mngr, Screen parent, ChangeListener listener) {
        super(new SpriteDrawable(new Sprite(mngr.get(icon, Texture.class))));
        super.addListener(listener);
        super.addListener(new ClickOnPress(mngr));
        TableUtils.setSpacingDefaults(this); // LeakingThisInConstructor
        this.parent = parent;
    }

    /**
     * Play sound whenever a button is pressed.
     *
     * @see Defines#SFX_CLICK
     */
    private class ClickOnPress extends ChangeListener {

        /**
         * Sound to play.
         */
        private final Sound click;

        public ClickOnPress(AssetManager mngr) {
            this.click = mngr.get(Defines.SFX_CLICK, Sound.class);
        }

        @Override
        public void changed(ChangeEvent evt, Actor actor) {
            parent.play(click);
        }
    }
}
