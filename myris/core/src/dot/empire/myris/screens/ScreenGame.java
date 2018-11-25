package dot.empire.myris.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.kotcrab.vis.ui.widget.VisImage;
import dot.empire.myris.Myris;
import dot.empire.myris.Screen;
import dot.empire.myris.SequenceGenerator;
import dot.empire.myris.gfx.Score;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import static dot.empire.myris.Defines.*;

public final class ScreenGame extends Screen {

    /**
     * Default block size.
     */
    private static final int BLOCK_SIZE = 160;
    /**
     * Block colours.
     */
    private static final Color[] COLOURS = {
            Color.SKY, /*Color.CHARTREUSE,*/ Color.GOLD,
            /*Color.TAN,*/ Color.SCARLET, Color.VIOLET
    };

    private int[][] blocks;
    private SequenceGenerator seqn;
    private Sound sfxCollect;
    private Sound sfxDeath;
    private Sound sfxClick;
    private AtomicInteger numCollected;
    private Score score;

    public ScreenGame() {
        this.numCollected = new AtomicInteger();
        this.seqn = new SequenceGenerator(COLOURS.length);
        this.blocks = new int[Gdx.graphics.getWidth() / BLOCK_SIZE][Gdx.graphics.getHeight() / BLOCK_SIZE];

        Gdx.app.log(Myris.TAG, String.format(Locale.ENGLISH, "Blocks = %dx%d", blocks.length, blocks[0].length));
    }

    @Override
    public void show(AssetManager mngr) {
        Sprite overlay = new Sprite(mngr.get(GAME_OVERLAY, Texture.class));
        overlay.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        add(new VisImage(overlay));

        this.sfxCollect = mngr.get(SFX_COLLECT, Sound.class);
        this.sfxDeath = mngr.get(SFX_DEATH, Sound.class);
        this.sfxClick = mngr.get(SFX_CLICK, Sound.class);

        addActor(score = new Score());
        reset_();


//        blocks[1][1] = 1;
//        blocks[1][0] = 1;
//        blocks[0][0] = 2;
//        Gdx.app.log("" + children(1, 1, 1, LEFT), "");
    }

    @Override
    public void update(float dt) {
        if (numCollected.get() != 0) {
            this.numCollected.decrementAndGet();
            if (numCollected.get() % 2 == 0) {
                this.sfxCollect.play();
            }
        }
    }

    @Override
    public void render(ShapeRenderer renderer, SpriteBatch batch) {
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                if (blocks[x][y] != -1) {
                    renderer.setColor(COLOURS[blocks[x][y]]);
                    renderer.rect((x * BLOCK_SIZE), (y * BLOCK_SIZE), BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }

    @Override
    public void onUp() {
        for (int x = 0; x < blocks.length; x++) {
            for (int y = blocks[x].length - 1; y >= 0; y--) {
                if (blocks[x][y] != -1) {
                    up(blocks[x][y], x, y);
                }
            }
        }
        check();
    }

    @Override
    public void onDown() {
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                if (blocks[x][y] != -1) {
                    down(blocks[x][y], x, y);
                }
            }
        }
        check();
    }

    @Override
    public void onLeft() {
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                if (blocks[x][y] != -1) {
                    left(blocks[x][y], x, y);
                }
            }
        }
        check();
    }

    @Override
    public void onRight() {
        for (int x = blocks.length - 1; x >= 0; x--) {
            for (int y = 0; y < blocks[x].length; y++) {
                if (blocks[x][y] != -1) {
                    right(blocks[x][y], x, y);
                }
            }
        }
        check();
    }

    private void up(int colour, int x, int y) {
        this.blocks[x][y] = -1;
        if (y != blocks[0].length - 1 && blocks[x][y + 1] == colour) {
            if (blocks[x][y + 1] == colour) {
                collect();
            }
            return;
        } else if (y == blocks[0].length - 1 || blocks[x][y + 1] != -1) {
            this.blocks[x][y] = colour;
            return;
        }
        up(colour, x, y + 1);
    }

    private void down(int colour, int x, int y) {
        this.blocks[x][y] = -1;
        if (y != 0 && blocks[x][y - 1] == colour) {
            if (blocks[x][y - 1] == colour) {
                collect();
            }
            return;
        } else if (y == 0 || blocks[x][y - 1] != -1) {
            this.blocks[x][y] = colour;
            return;
        }
        down(colour, x, y - 1);
    }

    private void left(int colour, int x, int y) {
//        if (!children(colour, x, y, LEFT)) {
//            return;
//        }
        this.blocks[x][y] = -1;
        if (x != 0 && blocks[x - 1][y] == colour) {
            if (blocks[x - 1][y] == colour) {
                collect();
            }
            return;
        } else if (x == 0 || blocks[x - 1][y] != -1) {
            this.blocks[x][y] = colour;
            return;
        }
        left(colour, x - 1, y);
    }

    private void right(int colour, int x, int y) {
        this.blocks[x][y] = -1;
        if (x != blocks.length - 1 && blocks[x + 1][y] == colour) {
            if (blocks[x + 1][y] == colour) {
                collect();
            }
            return;
        } else if (x == blocks.length - 1 || blocks[x + 1][y] != -1) {
            this.blocks[x][y] = colour;
            return;
        }
        right(colour, x + 1, y);
    }

    /**
     * Add new block to the game world.
     */
    private void addBlock() {
        if (isFull()) {
            return;
        }
        int x, y;
        do {
            x = MathUtils.random(blocks.length - 1);
            y = MathUtils.random(blocks[0].length - 1);
        } while (blocks[x][y] != -1);
        this.blocks[x][y] = seqn.next() - 1;
    }

    private void check() {
        getEngine().setAlpha(0);
        this.sfxClick.play();
        addBlock();
    }


    private boolean isFull() {
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                if (blocks[x][y] == -1) {
                    return false;
                }
            }
        }

        getEngine().setAlpha(0);
        Gdx.app.log(Myris.TAG, "GAME OVER!");
        this.sfxDeath.play();

        reset_();

        return true;
    }

    @Override
    public void dispose() {
        // TODO: 11 Nov 2018 Check if needed
        this.sfxCollect.dispose();
        this.sfxDeath.dispose();
        this.sfxClick.dispose();
        super.dispose();
    }

    private void collect() {
        this.numCollected.addAndGet(2);
        this.score.updateScore(1);
    }

    private void reset_() {
        this.score.reset();
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                this.blocks[x][y] = -1;
            }
        }
        addBlock();
        addBlock();
    }

    // FIXME: 19 Nov 2018
    @SuppressWarnings({"UnnecessaryLocalVariable", "ConstantConditions", "UnusedReturnValue", "WeakerAccess"})
    public boolean children(int colour, final int x, final int y, Direction direction) {
        boolean rtn = true;
        this.blocks[x][y] = -1;
        Gdx.app.debug(Myris.TAG, String.format(Locale.ENGLISH, "Child %d (%d;%d)", colour, x, y));
        if (rtn) {
            try {
                int x_ = x + 1, y_ = y;
                rtn = canMove(colour, x_, y_, direction);
                if (blocks[x_][y_] == colour) {
                    if (rtn) {
                        children(colour, x_, y_, direction);
                    }
                }
            } catch (IndexOutOfBoundsException ignore) {
            }
        }
        if (rtn) {
            try {
                int x_ = x - 1, y_ = y;
                rtn = canMove(colour, x_, y_, direction);
                if (blocks[x_][y_] == colour) {
                    if (rtn) {
                        children(colour, x_, y_, direction);
                    }
                }
            } catch (IndexOutOfBoundsException ignore) {
            }
        }
        if (rtn) {
            try {
                int x_ = x, y_ = y - 1;
                rtn = canMove(colour, x_, y_, direction);
                if (blocks[x_][y_] == colour) {
                    if (rtn) {
                        children(colour, x_, y_, direction);
                    }
                }
            } catch (IndexOutOfBoundsException ignore) {
            }
        }
        if (rtn) {
            try {
                int x_ = x + 1, y_ = y;
                rtn = canMove(colour, x_, y_, direction);
                if (blocks[x_][y_] == colour) {
                    if (rtn) {
                        children(colour, x_, y_, direction);
                    }
                }
            } catch (IndexOutOfBoundsException ignore) {
            }
        }
        if (rtn) {
            try {
                int x_ = x, y_ = y + 1;
                rtn = canMove(colour, x_, y_, direction);
                if (blocks[x_][y_] == colour) {
                    if (rtn) {
                        children(colour, x_, y_, direction);
                    }
                }
            } catch (IndexOutOfBoundsException ignore) {
            }
        }
        this.blocks[x][y] = colour;
        return rtn;
    }

    private boolean canMove(int colour, int x, int y, Direction direction) {
        switch (direction) {
            case UP:
                y++;
                break;
            case DOWN:
                y--;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
        }
        try {
            return blocks[x][y] == colour || blocks[x][y] == -1;
        } catch (IndexOutOfBoundsException ignore) {
        }
        return false;
    }

    protected enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}

