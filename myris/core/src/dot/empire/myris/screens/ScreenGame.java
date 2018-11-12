package dot.empire.myris.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import dot.empire.myris.BaseEngine;
import dot.empire.myris.Screen;
import dot.empire.myris.SequenceGenerator;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

public final class ScreenGame extends Screen {

    /**
     * Default block size.
     */
    public static final int BLOCK_SIZE = 80;
    /**
     * Block colours.
     */
    private static final Color[] COLOURS = {
            Color.SKY, Color.CHARTREUSE, Color.GOLD,
            /*Color.TAN,*/ Color.SCARLET, Color.VIOLET
    };
    /**
     * Sound effect to be played when a block is collected.
     */
    @AnnotationAssetManager.Asset(Sound.class)
    private static final String SFX_COLLECT = "sfx/170147__timgormly__8-bit-coin.ogg";
    @AnnotationAssetManager.Asset(Sound.class)
    private static final String SFX_DEATH = "sfx/death.mp3";


    private int[][] blocks;
    private int[][] _blocks;
    private SequenceGenerator seqn;
    private Sound sfxCollect;
    private Sound sfxDeath;


    public ScreenGame() {
        this.seqn = new SequenceGenerator(COLOURS.length);
        this.blocks = new int[Gdx.graphics.getWidth() / BLOCK_SIZE][Gdx.graphics.getHeight() / BLOCK_SIZE];
        this._blocks = new int[Gdx.graphics.getWidth() / BLOCK_SIZE][Gdx.graphics.getHeight() / BLOCK_SIZE];

        Gdx.app.log(BaseEngine.TAG, String.format("Blocks = %dx%d", blocks.length, blocks[0].length));

        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                this.blocks[x][y] = -1;
            }
        }

        add();
        add();
    }

    @Override
    public void show(AnnotationAssetManager mngr) {
        this.sfxCollect = mngr.get(SFX_COLLECT, Sound.class);
        this.sfxDeath = mngr.get(SFX_DEATH, Sound.class);
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
        // int current = getSize(blocks);
        // Gdx.app.debug("Last size", Integer.toString(last));
        // Gdx.app.debug("Current size", Integer.toString(getSize()));
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

    /**
     * @param blocks the array to be tested
     * @return the number of active blocks
     */
    private int getSize(int[][] blocks) {
        int size = 0;
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                if (blocks[x][y] != -1) {
                    size++;
                }
            }
        }
        return size;
    }

    @Override
    public void dispose() {
        this.sfxCollect.dispose(); // TODO: 11 Nov 2018 Check if needed
        this.sfxDeath.dispose();
    }

    private void collect() {
        this.sfxCollect.play();
    }
}

