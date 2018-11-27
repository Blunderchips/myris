package dot.empire.myris.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Score Label.
 */
public final class Score extends VisTable implements Comparable<Long> {

    private final AtomicLong score;
    private final AtomicLong tmp;
    private final VisLabel lblScore;
    private boolean bool;
    private boolean valid;

    public Score() {
        super(true);
        super.setFillParent(true);

        this.valid = true; // default score value to 0
        // They default to 1
        this.tmp = new AtomicLong(0);
        this.score = new AtomicLong(0);
        // --

        this.lblScore = new VisLabel(score.toString(), Color.BLACK);
        this.lblScore.setFontScale(2); // FIXME: 18 Nov 2018
        super.align(Align.center).add(lblScore).row();

        // super.align(Align.center).add(new VisLabel("Test score", Color.BLACK));

        zeroScore();
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

    /**
     * @param delta change in score
     */
    public void updateScore(int delta) {
        // TODO: 18 Nov 2018
        // this.tmp.addAndGet(delta);
        // this.valid = false;
        // --
        this.score.addAndGet(delta);
    }

    /**
     * Reset the value of the score to zero. Former {@code reset()} method. Reset already in use by super class.
     */
    public void zeroScore() {
        this.tmp.set(0);
        this.score.set(0);
        this.lblScore.setText(0 + "");
    }

    /**
     * Compares two {@code long} values numerically.
     */
    @Override
    public int compareTo(final Long other) {
        final Long score = this.score.get();
        return (score < other) ? -1 : ((score == other) ? 0 : 1);
    }

    @Override
    public String toString() {
        return score.toString();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (!(other instanceof Score)) return false;

        Score score1 = (Score) other;

        return score.equals(score1.score);
    }

    @Override
    public int hashCode() {
        return score.hashCode();
    }
}
