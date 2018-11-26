package dot.empire.myris.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Score Label.
 */
public class Score extends VisTable {

    private final AtomicLong score;
    private final AtomicLong tmp;
    private final VisLabel lblScore;
    private boolean bool;
    private boolean valid;

    public Score() {
        super(true);
        super.setFillParent(true);

        this.valid = true;
        // They default to 1
        this.tmp = new AtomicLong(0);
        this.score = new AtomicLong(0);
        // --

        this.lblScore = new VisLabel(score.toString(), Color.BLACK);
        this.lblScore.setFontScale(2); // FIXME: 18 Nov 2018
        super.align(Align.center).add(lblScore).row();

        // super.align(Align.center).add(new VisLabel("Test score", Color.BLACK));

        reset();
        super.setVisible(true); // TODO: 13 Nov 2018 Really needed?
    }

    /**
     * Update.
     *
     * @param dt Delta time
     */
    @Override
    public void act(float dt) {
        if (!valid) {
            this.score.addAndGet((long) Math.pow(2, tmp.get()));
            this.tmp.set(0);
            this.valid = true;
        }
        long tmp = Long.parseLong(lblScore.getText().toString());
        if (tmp != score.get()) {
            if (bool = !bool) {
                this.lblScore.setText(Long.toString(tmp + 1));
            }
        }
        super.act(dt);
    }

    public void updateScore(int delta) {
        // TODO: 18 Nov 2018
        // this.tmp.addAndGet(delta);
        // this.valid = false;
        // --
        this.score.addAndGet(delta);
    }

    public void reset() {
        this.tmp.set(0);
        this.score.set(0);
        this.lblScore.setText(0 + "");
    }
}
