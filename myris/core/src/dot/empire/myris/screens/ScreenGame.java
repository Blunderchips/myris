package dot.empire.myris.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dot.empire.myris.Screen;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

public final class ScreenGame extends Screen {

    public static final int BLOCK_SIZE = 40;
    private int[][] blocks;

    public ScreenGame() {
        this.blocks = new int[Gdx.graphics.getWidth() / BLOCK_SIZE][Gdx.graphics.getHeight() / BLOCK_SIZE];
        Gdx.app.log("Blocks", String.format("%dx%d", blocks.length, blocks[0].length));
        this.blocks[1][0] = 1;
    }

    @Override
    public void show(AnnotationAssetManager mngr) {
        super.show(mngr);
    }

    @Override
    public void render(ShapeRenderer renderer) {
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                if (blocks[x][y] != 0) {
                    renderer.rect(40 * x, 40 * y, 40, 40);
                }
            }
        }
    }
}
