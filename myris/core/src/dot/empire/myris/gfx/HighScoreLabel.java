package dot.empire.myris.gfx;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;

/**
 * Score display for menus.
 */
public final class HighScoreLabel extends VisTable {

    public HighScoreLabel(long score, AssetManager mngr) {
        super(true);
        for (char i : Long.toString(score).toCharArray()) {
            add(new VisImageButton(new SpriteDrawable(new Sprite(mngr.get("gfx/img_num_" + i + ".png", Texture.class)))));
        }
    }
}
