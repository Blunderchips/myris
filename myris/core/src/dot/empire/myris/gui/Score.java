package dot.empire.myris.gui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.concurrent.atomic.AtomicLong;

public class Score extends VisTable {

    private AtomicLong score;
    private VisLabel lblScore;

    public Score() {
        super(true);
        super.setFillParent(true);
        super.setDebug(Gdx.app.getLogLevel() == Application.LOG_DEBUG, true);

        this.score = new AtomicLong(0);

        this.lblScore = new VisLabel(score.toString(), Color.BLACK);
        super.align(Align.center).add(lblScore).row();

        super.align(Align.center).add(new VisLabel("Test score", Color.BLACK));

        super.setVisible(true);
    }

    public void updateScore(int delta) {
        this.score.addAndGet(delta);
        this.lblScore.setText(score.toString());
    }
}
