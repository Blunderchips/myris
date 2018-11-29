package dot.empire.myris.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.kotcrab.vis.ui.widget.VisTable;
import dot.empire.myris.Screen;
import dot.empire.myris.buttons.BtnPlay;
import dot.empire.myris.buttons.BtnToMain;
import dot.empire.myris.gfx.HighScoreLabel;
import dot.empire.myris.gfx.ScoreLabel;

/**
 * Shows current score & current high score to the user. Created 26/11/2018.
 *
 * @author Matthew 'siD' Van der Bijl
 */
public final class ScreenDeath extends Screen {

    /**
     * Current score.
     */
    private ScoreLabel score;

    /**
     * Defaults all attributes to null.
     */
    public ScreenDeath() {
        this.score = null;
    }

    /**
     * @param score the score of the current game
     */
    public ScreenDeath(ScoreLabel score) {
        this.score = score;
    }

    @Override
    public void show(AssetManager mngr) {
        add(new HighScoreLabel(score.getScore(), mngr));

        row();

        addSeparator();

        VisTable buttons = new VisTable(true);
        buttons.add(new BtnPlay(mngr, this));
        buttons.add(new BtnToMain(mngr, this));
        add(buttons);
    }
}
