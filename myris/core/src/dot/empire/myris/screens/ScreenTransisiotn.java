package dot.empire.myris.screens;

import com.badlogic.gdx.Gdx;
import dot.empire.myris.BaseEngine;
import dot.empire.myris.Screen;

public final class ScreenTransisiotn extends Screen {

    private float elapsed;
    private Class<? extends Screen> target;

    public ScreenTransisiotn(Class<? extends Screen> target, BaseEngine engine) {
        super(engine);
        this.elapsed = 0;
        this.target = target;
    }

    @Override
    public void update(float dt) {
        this.elapsed = Math.min(elapsed + dt, 1);
        if (elapsed == 1) {
            changeScreen(target);
        }
    }

    @Override
    public void changeScreen(final Class<? extends Screen> next) {
        Gdx.app.postRunnable(new Runnable() {

            @Override
            public void run() {
                try {
                    getEngine().setScreen(next.getConstructor(BaseEngine.class).newInstance(getEngine()));
                } catch (Exception ex) { // TODO: 11 Nov 2018 Get specific exceptions
                    Gdx.app.error(BaseEngine.TAG, "Cannot change next", ex);
                }
            }
        });
    }
}
