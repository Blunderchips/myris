package dot.empire.myris.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.concurrent.atomic.AtomicLong;

import static dot.empire.myris.Defines.*;

/**
 * In-game Score Label. Former {@code Score} class.
 */
public final class ScoreLabel extends VisTable implements Comparable<Long>, Disposable {

    private final AtomicLong score;
    private final AtomicLong tmp;
    private final VisLabel lblScore;
    private boolean bool;
    private boolean valid;

    private float elasped;
    private Animation<TextureRegion> tutGif;

    public ScoreLabel() {
        super(true);
        super.setFillParent(true);

        this.valid = true; // default score value to 0
        // They default to 1
        this.tmp = new AtomicLong(0);
        this.score = new AtomicLong(0);
        // --

        this.tutGif = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP,
                Gdx.files.internal(TUTORIAL_GIF).read());

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
        // FIXME: 28 Dec 2018
        this.elasped += dt / 2f;
        if (elasped > 1000) {
            this.elasped = 0;
        }
        // --

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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (score.get() == 0) {
            TextureRegion tex = tutGif.getKeyFrame(elasped, true);
            batch.draw(tex,
                    (SCREEN_WIDTH - tex.getRegionWidth()) / 2f,
                    (SCREEN_HEIGHT - tex.getRegionHeight()) / 2f
            );
            return;
        }
        super.draw(batch, parentAlpha);
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
        return (score < other) ? -1 : ((score.equals(other)) ? 0 : 1);
    }

    @Override
    public String toString() {
        return score.toString();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (!(other instanceof ScoreLabel)) return false;

        ScoreLabel score1 = (ScoreLabel) other;

        return score.equals(score1.score);
    }

    @Override
    public int hashCode() {
        return score.hashCode();
    }

    public long getScore() {
        return this.score.get();
    }

    @Override
    public void dispose() {
//        for (TextureRegion texture : tutGif.getKeyFrames()) {
//            texture.getTexture().dispose();
//        }
    }
}
