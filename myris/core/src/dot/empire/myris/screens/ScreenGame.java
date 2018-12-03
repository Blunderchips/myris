package dot.empire.myris.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import dot.empire.myris.Myris;
import dot.empire.myris.Screen;
import dot.empire.myris.SequenceGenerator;
import dot.empire.myris.gfx.ScoreLabel;

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
    private ScoreLabel score;

    public ScreenGame() {
        this.numCollected = new AtomicInteger();
        this.seqn = new SequenceGenerator(COLOURS.length);
        this.blocks = new int[SCREEN_WIDTH / BLOCK_SIZE][SCREEN_HEIGHT / BLOCK_SIZE];

        Gdx.app.log(Myris.TAG, String.format(Locale.ENGLISH, "Blocks = %dx%d", blocks.length, blocks[0].length));
    }

    @Override
    public void show(AssetManager mngr) {
        // Sprite overlay = new Sprite(mngr.get(GAME_OVERLAY, Texture.class));
        // overlay.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        // add(new VisImage(overlay));

        this.sfxCollect = mngr.get(SFX_COLLECT, Sound.class);
        this.sfxDeath = mngr.get(SFX_DEATH, Sound.class);
        this.sfxClick = mngr.get(SFX_CLICK, Sound.class);

        addActor(score = new ScoreLabel());
        clearBoard();

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
        death();
        return true;
    }

    private void collect() {
        this.numCollected.addAndGet(2);
        this.score.updateScore(1);
    }

    /**
     * Reset playing field.
     */
    private void clearBoard() {
        this.score.zeroScore();
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                this.blocks[x][y] = -1;
            }
        }
        addBlock();
        addBlock();
    }

    /**
     * Called when the board is fulled and the player has lost.
     */
    private void death() {
        Gdx.app.log(Myris.TAG, "GAME OVER!"); // why not
        this.sfxDeath.play();
        changeScreen(new ScreenDeath(score, getEngine().getPreferences().getHighScore()));
        getEngine().getPreferences().setHighScore(score);
        // clearBoard();
    }
}

