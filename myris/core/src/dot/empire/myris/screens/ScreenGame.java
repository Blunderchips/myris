package dot.empire.myris.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import dot.empire.myris.BaseEngine;
import dot.empire.myris.Screen;
import dot.empire.myris.SequenceGenerator;
import dot.empire.myris.gui.Score;
import net.dermetfan.gdx.assets.AnnotationAssetManager;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public final class ScreenGame extends Screen {

    /**
     * Default block size.
     */
    private static final int BLOCK_SIZE = 80;
    /**
     * Block colours.
     */
    private static final Color[] COLOURS = {
            Color.SKY, Color.CHARTREUSE, Color.GOLD,
            Color.TAN, Color.SCARLET, Color.VIOLET
    };
    /**
     * Sound effect to be played when a block is collected.
     */
    @AnnotationAssetManager.Asset(Sound.class)
    private static final String SFX_COLLECT = "sfx/170147__timgormly__8-bit-coin.ogg";
    @AnnotationAssetManager.Asset(Sound.class)
    private static final String SFX_DEATH = "sfx/death.mp3";
    @AnnotationAssetManager.Asset(Sound.class)
    private static final String SFX_CLICK = "sfx/275152__bird-man__click.ogg";

    private int[][] blocks;
    private int[][] _blocks;
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
        this._blocks = new int[Gdx.graphics.getWidth() / BLOCK_SIZE][Gdx.graphics.getHeight() / BLOCK_SIZE];

        Gdx.app.log(BaseEngine.TAG, String.format(Locale.ENGLISH, "Blocks = %dx%d", blocks.length, blocks[0].length));

        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                this.blocks[x][y] = -1;
            }
        }

        add();
        add();
    }

    @Override
    public void show(@NotNull AssetManager mngr) {
        this.sfxCollect = mngr.get(SFX_COLLECT, Sound.class);
        this.sfxDeath = mngr.get(SFX_DEATH, Sound.class);
        this.sfxClick = mngr.get(SFX_CLICK, Sound.class);

        getEngine().getUILayer().addActor(score = new Score());
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
        System.arraycopy(blocks, 0, _blocks, 0, blocks.length);
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
        System.arraycopy(blocks, 0, _blocks, 0, blocks.length);
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
        System.arraycopy(blocks, 0, _blocks, 0, blocks.length);
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
        System.arraycopy(blocks, 0, _blocks, 0, blocks.length);
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
    private void add() {
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
        add();
    }


    private boolean isFull() {
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                if (blocks[x][y] == -1) {
                    return false;
                }
            }
        }

        Gdx.app.log(BaseEngine.TAG, "GAME OVER!");
        this.sfxDeath.play();

        return true;
    }

    @Override
    public void dispose() {
        // TODO: 11 Nov 2018 Check if needed
        this.sfxCollect.dispose();
        this.sfxDeath.dispose();
        this.sfxClick.dispose();
    }

    private void collect() {
        this.numCollected.addAndGet(2);
        this.score.updateScore(1);
    }
}

