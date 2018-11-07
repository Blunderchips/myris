package dot.empire.myris.screens;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dot.empire.myris.Screen;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

public final class ScreenLoading extends Screen {

    private final Screen parent;

    public ScreenLoading(Screen parent) {
        this.parent = parent;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
    }

    @Override
    public void render(ShapeRenderer renderer) {
        super.render(renderer);
    }

    @Override
    public boolean load(AnnotationAssetManager mngr) {
        return false;
    }

    @Override
    public String getName() {
        return String.format("Loading => %s", parent.getName());
    }
}
