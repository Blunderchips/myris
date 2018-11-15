package dot.empire.myris.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import dot.empire.myris.Screen;

/**
 * @author Matthew 'siD' Van der Bijl
 */
public final class ScreenSettings extends Screen {

    /**
     * Contrast value.
     */
    private VisSlider sldContrast;
    /**
     * Brightness value.
     */
    private VisSlider sldBrightness;

    private boolean flag = false;

    @Override
    public void show(AssetManager mngr) {
        flag = false;

        this.sldContrast = new VisSlider(0, 100, 1, false);
        this.sldContrast.setValue(getEngine().getPreferences().getFloat("contrast"));
        this.sldContrast.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent evt, Actor actor) {
                if (flag) {
                    //getEngine().setContrast(sldContrast.getValue());
                }
            }
        });

        this.sldBrightness = new VisSlider(0, 100, 1, false);
        this.sldBrightness.setValue(getEngine().getPreferences().getFloat("brightness"));
        this.sldBrightness.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent evt, Actor actor) {
                if (flag) {
                    //getEngine().setBrightness(sldBrightness.getValue());
                }
            }
        });

        add(new VisLabel("Contrast", Color.BLACK));
        add(sldContrast).row();

        row();

        add(new VisLabel("Brightness", Color.BLACK));
        add(sldBrightness).row();

        flag = true;
    }
}
