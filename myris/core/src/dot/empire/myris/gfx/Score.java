package dot.empire.myris.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.concurrent.atomic.AtomicLong;

public class Score extends VisTable {

    private final AtomicLong score;
    private final VisLabel lblScore;
    private boolean bool;

    public Score() {
        super(true);
        super.setFillParent(true);

        this.score = new AtomicLong(0);

        this.lblScore = new VisLabel(score.toString(), Color.BLACK);
        super.align(Align.center).add(lblScore).row();

        super.align(Align.center).add(new VisLabel("Test score", Color.BLACK));

        super.setVisible(true); // TODO: 13 Nov 2018 Really needed?
    }

    /**
     * Update.
     *
     * @param dt Delta time
     */
    @Override
    public void act(float dt) {
        long tmp = Long.parseLong(lblScore.getText().toString());
        if (tmp != score.get()) {
            if (bool = !bool) {
                this.lblScore.setText(Long.toString(tmp + 1));
            }
        }
        super.act(dt);
    }

    public void updateScore(int delta) {
        this.score.addAndGet(delta);
    }

    public void reset() {
        this.score.set(0);
        this.lblScore.setText(0 + "");
    }
}
