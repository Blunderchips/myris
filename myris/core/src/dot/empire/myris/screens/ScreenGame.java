package dot.empire.myris.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import dot.empire.myris.Screen;
import dot.empire.myris.SequenceGenerator;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

public final class ScreenGame extends Screen {

    public static final int BLOCK_SIZE = 80;

    private static final Color[] COLOURS = {
            Color.SKY, Color.CHARTREUSE, Color.GOLD,
            Color.TAN, Color.SCARLET, Color.VIOLET
    };

    private int[][] blocks;
    private SequenceGenerator seqn;

    public ScreenGame() {
        this.seqn = new SequenceGenerator(COLOURS.length);
        this.blocks = new int[Gdx.graphics.getWidth() / BLOCK_SIZE][Gdx.graphics.getHeight() / BLOCK_SIZE];
        Gdx.app.log("Blocks", String.format("%dx%d", blocks.length, blocks[0].length));

        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                this.blocks[x][y] = -1;
            }
        }

        add();
    }

    @Override
    public void show(AnnotationAssetManager mngr) {
        super.show(mngr);
    }

    @Override
    public void render(ShapeRenderer renderer) {
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                if (blocks[x][y] != -1) {
                    renderer.setColor(COLOURS[blocks[x][y]]);
                    renderer.rect(BLOCK_SIZE * x, BLOCK_SIZE * y, BLOCK_SIZE, BLOCK_SIZE);
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
        add();
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
        add();
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
        add();
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
        add();
    }

    private void up(int colour, int x, int y) {
        if (y == blocks[0].length - 1 || blocks[x][y + 1] != -1) {
            this.blocks[x][y] = colour;
            return;
        }
        this.blocks[x][y] = -1;
        up(colour, x, y + 1);
    }

    private void down(int colour, int x, int y) {
        if (y == 0 || blocks[x][y - 1] != -1) {
            this.blocks[x][y] = colour;
            return;
        }
        this.blocks[x][y] = -1;
        down(colour, x, y - 1);
    }

    private void left(int colour, int x, int y) {
        if (x == 0 || blocks[x - 1][y] != -1) {
            this.blocks[x][y] = colour;
            return;
        }
        this.blocks[x][y] = -1;
        left(colour, x - 1, y);
    }

    private void right(int colour, int x, int y) {
        if (x == blocks.length - 1 || blocks[x + 1][y] != -1) {
            this.blocks[x][y] = colour;
            return;
        }
        this.blocks[x][y] = -1;
        right(colour, x + 1, y);
    }

    private void add() {
        int x, y;
        do {
            x = MathUtils.random(blocks.length - 1);
            y = MathUtils.random(blocks[0].length - 1);
        } while (blocks[x][y] != -1);
        this.blocks[x][y] = seqn.next() - 1;
    }

    private void check() {

    }

    boolean check(int x, int y) {

        return false;
    }
}

