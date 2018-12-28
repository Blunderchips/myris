package dot.empire.myris.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import dot.empire.myris.Myris;
import dot.empire.myris.Screen;
import dot.empire.myris.SequenceGenerator;
import dot.empire.myris.gfx.ScoreLabel;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import static dot.empire.myris.Defines.Messages.BACK_KEY_PRESSED;
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

    private int direction;
    private float alpha;

    private int[][] blocks;
    private int[][] last;
    private final SequenceGenerator seqn;
    private Sound sfxCollect;
    private Sound sfxDeath;
    private Sound sfxClick;
    private final AtomicInteger numCollected;
    private ScoreLabel score;

    public ScreenGame() {
        this.alpha = 1;

        this.numCollected = new AtomicInteger();
        this.seqn = new SequenceGenerator(COLOURS.length);
        this.blocks = new int[SCREEN_WIDTH / BLOCK_SIZE][SCREEN_HEIGHT / BLOCK_SIZE];
        this.last = new int[SCREEN_WIDTH / BLOCK_SIZE][SCREEN_HEIGHT / BLOCK_SIZE];

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

        super.show(mngr);
    }

    @Override
    public void update(float dt) {
        if (numCollected.get() != 0) {
            this.numCollected.decrementAndGet();
            if (numCollected.get() % 2 == 0) {
                play(sfxCollect);
            }
        }

        this.alpha = MathUtils.clamp(Interpolation.linear.apply(alpha + dt * 2), 0, 1);
        if (alpha == 1) {
            this.direction = 0;
        }

        if (alpha == 1) {
            for (int x = 0; x < blocks.length; x++) {
                System.arraycopy(blocks[x], 0, last[x], 0, blocks[x].length);
            }
        }
    }

    @Override
    public void render(ShapeRenderer renderer, SpriteBatch batch) {
        int[][] tmp = alpha == 1 ? blocks : blocks;
        for (int x = 0; x < tmp.length; x++) {
            for (int y = 0; y < tmp[x].length; y++) {
                if (tmp[x][y] != -1) {
                    int colour = tmp[x][y];

                    int[] next = findNext(colour, x, y, direction);
                    float xPos = (x * BLOCK_SIZE), yPos = (y * BLOCK_SIZE);

                    switch (direction) {
                        case 1: // up
                            yPos = MathUtils.clamp(yPos - SCREEN_HEIGHT * (1 - alpha), 0, SCREEN_HEIGHT);
                            break;
                        case 2: // down
                            yPos = MathUtils.clamp(yPos + SCREEN_HEIGHT * (1 - alpha), 0, SCREEN_HEIGHT);
                            break;
                        case 3: // left
                            xPos = MathUtils.clamp(xPos + SCREEN_WIDTH * (1 - alpha), 0, SCREEN_WIDTH);
                            break;
                        case 4: // right
                            xPos = MathUtils.clamp(xPos - SCREEN_WIDTH * (1 - alpha), 0, SCREEN_WIDTH);
                            break;
                    }

                    renderer.setColor(COLOURS[colour]);
                    renderer.rect(
                            xPos,
                            yPos,
                            BLOCK_SIZE, BLOCK_SIZE
                    );
                }
            }
        }
    }

    @Override
    public void onUp() {
        this.direction = 1;
        this.last = new int[blocks.length][blocks[0].length];
        for (int x = 0; x < blocks.length; x++) {
            System.arraycopy(blocks[x], 0, last[x], 0, blocks[x].length);
        }

        for (int x = 0; x < blocks.length; x++) {
            for (int y = blocks[x].length - 1; y >= 0; y--) {
                if (blocks[x][y] != -1) {
                    up(blocks[x][y], x, y);
                }
            }
        }
        check(last);
    }

    @Override
    public void onDown() {
        this.direction = 2;
        this.last = new int[blocks.length][blocks[0].length];
        for (int x = 0; x < blocks.length; x++) {
            System.arraycopy(blocks[x], 0, last[x], 0, blocks[x].length);
        }

        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                if (blocks[x][y] != -1) {
                    down(blocks[x][y], x, y);
                }
            }
        }
        check(last);
    }

    @Override
    public void onLeft() {
        this.direction = 3;
        this.last = new int[blocks.length][blocks[0].length];
        for (int x = 0; x < blocks.length; x++) {
            System.arraycopy(blocks[x], 0, last[x], 0, blocks[x].length);
        }

        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                if (blocks[x][y] != -1) {
                    left(blocks[x][y], x, y);
                }
            }
        }
        check(last);
    }

    @Override
    public void onRight() {
        this.direction = 4;
        this.last = new int[blocks.length][blocks[0].length];
        for (int x = 0; x < blocks.length; x++) {
            System.arraycopy(blocks[x], 0, last[x], 0, blocks[x].length);
        }

        for (int x = blocks.length - 1; x >= 0; x--) {
            for (int y = 0; y < blocks[x].length; y++) {
                if (blocks[x][y] != -1) {
                    right(blocks[x][y], x, y);
                }
            }
        }
        check(last);
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
        // if (isFull()) {
        //     return;
        // }
        int x, y;
        do {
            x = MathUtils.random(blocks.length - 1);
            y = MathUtils.random(blocks[0].length - 1);
        } while (blocks[x][y] != -1);
        this.blocks[x][y] = seqn.next() - 1;
    }

    private void check(int[][] last) {
        if (Arrays.deepEquals(last, blocks) | isFull()) {
            // play(sfxDeath);
            return;
        }
        getEngine().setAlpha(0);
        play(sfxClick);
        addBlock();
        this.alpha = 0;
    }


    private boolean isFull() {
        for (int[] row : blocks) {
            for (int y = 0; y < row.length; y++) {
                if (row[y] == -1) {
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
        // addBlock();
    }

    /**
     * Called when the board is fulled and the player has lost.
     */
    private void death() {
        // Gdx.app.log(Myris.TAG, "All men must die");
        Gdx.app.log(Myris.TAG, "GAME OVER!"); // why not
        play(sfxDeath);
        changeScreen(new ScreenScore(score, getEngine().getPreferences().getHighScore()));
        getEngine().getPreferences().setHighScore(score);
        // clearBoard();
    }

    // siD 2018-12-22:
    // Saves current score as a possible highscore.
    // Easier to test for me & allows quite states.
    @Override
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public boolean handleMessage(Telegram msg) {
        switch (msg.message) {
            case BACK_KEY_PRESSED:
                death();
                break;
        }
        return super.handleMessage(msg);
    }

    private int[] findNext(int colour, int xPos, int yPos, int direction) {
        if (direction == 0) {
            return new int[]{0, 0};
        }
        int[] rtn = new int[2];
        switch (direction) {
            case 1: // up
                if (yPos == blocks[0].length) {
                    return new int[]{0, 0};
                }
                for (int y = yPos + 1; y < blocks[0].length; y++) {
                    if (blocks[xPos][y] == colour) {
                        rtn[0] = xPos;
                        rtn[1] = y;
                    }
                }
                break;
            case 2: // down
                if (yPos == 0) {
                    return new int[]{0, 0};
                }
                for (int y = yPos - 1; y < 0; y--) {
                    if (blocks[xPos][y] == colour) {
                        rtn[0] = xPos;
                        rtn[1] = y;
                    }
                }
                break;
            case 3: // left
                if (xPos == 0) {
                    return new int[]{0, 0};
                }
                for (int x = xPos - 1; x < 0; x--) {
                    if (blocks[x][yPos] == colour) {
                        rtn[0] = x;
                        rtn[1] = yPos;
                    }
                }
                break;
            case 4: // right
                if (xPos == blocks.length) {
                    return new int[]{0, 0};
                }
                for (int x = xPos + 1; x < blocks.length; x++) {
                    if (blocks[x][yPos] == colour) {
                        rtn[0] = x;
                        rtn[1] = yPos;
                    }
                }
                break;
        }
        return rtn;
    }

    @Override
    public void dispose() {
        this.score.dispose();
        super.dispose();
    }
}

