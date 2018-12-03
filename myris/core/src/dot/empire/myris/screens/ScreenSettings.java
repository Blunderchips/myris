package dot.empire.myris.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import dot.empire.myris.Screen;
import dot.empire.myris.buttons.BtnReset;
import dot.empire.myris.buttons.BtnToMain;

/**
 * @author Matthew 'siD' Van der Bijl
 */
public final class ScreenSettings extends Screen implements BtnReset.RestListener {

    /**
     * Contrast value.
     */
    private VisSlider sldContrast;
    /**
     * Brightness value.
     */
    private VisSlider sldBrightness;

    @Override
    public void show(AssetManager mngr) {
        this.sldContrast = new VisSlider(0, 2, 0.25f, false);
        this.sldContrast.setValue(getEngine().getPreferences().getContrast());
        this.sldContrast.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent evt, Actor actor) {
                getEngine().setContrast(sldContrast.getValue() * 100);
            }
        });

        this.sldBrightness = new VisSlider(0, 1, 0.1f, false);
        this.sldBrightness.setValue(getEngine().getPreferences().getBrightness());
        this.sldBrightness.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent evt, Actor actor) {
                getEngine().setBrightness(sldBrightness.getValue() * 100);
            }
        });

        add(new VisLabel("Contrast", Color.BLACK));
        add(sldContrast).row();

        row();

        add(new VisLabel("Brightness", Color.BLACK));
        add(sldBrightness).row();

        add(new BtnToMain(mngr, this));
        add(new BtnReset(mngr, this));
    }

    @Override
    public void reset_() {
        getEngine().setBrightness(0);
        getEngine().setContrast(1);
        changeScreen(ScreenSettings.class);
    }
}
