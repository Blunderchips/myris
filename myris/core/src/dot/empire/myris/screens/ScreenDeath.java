package dot.empire.myris.screens;

import com.badlogic.gdx.assets.AssetManager;
import com.kotcrab.vis.ui.widget.VisLabel;
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
    private long score;
    /**
     * Old score.
     */
    private long old;

    /**
     * Defaults all attributes to null.
     */
    public ScreenDeath() {
        this(-1);
    }

    /**
     * @param score the score of the current game
     */
    public ScreenDeath(ScoreLabel score, long old) {
        this(score.getScore());
        this.old = old;
    }

    /**
     * @param score the score of the current game
     */
    public ScreenDeath(long score) {
        this.score = score;
        this.old = Long.MIN_VALUE;
    }

    @Override
    public void show(AssetManager mngr) {
        if (score == -1) {
            this.score = getEngine().getPreferences().getHighScore();
        }

        add(new HighScoreLabel(score, mngr));

        row();

        addSeparator();

        VisTable buttons = new VisTable(true);
        buttons.add(new BtnPlay(mngr, this));
        buttons.add(new BtnToMain(mngr, this));
        add(buttons);

        row();

//        if (old < score){
            add(new VisLabel("New Highscore!"));
//        }
    }
}
