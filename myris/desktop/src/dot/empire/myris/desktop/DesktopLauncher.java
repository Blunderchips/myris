package dot.empire.myris.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import dot.empire.myris.BaseEngine;

/**
 * For testing. Created 07/11/2018.
 *
 * @author Matthew 'siD' Van der Bijl
 */
public final class DesktopLauncher {

    /**
     * Prevent instatitation.
     */
    private DesktopLauncher() {
    }

    /**
     * @param args Arguments from the command line
     */
    public static void main(String[] args) {
        final LwjglApplicationConfiguration cfg
                = new LwjglApplicationConfiguration();

        cfg.title = "myris";

        cfg.samples = 16;
        cfg.resizable = false;

        cfg.width = 480;
        cfg.height = 720;

        cfg.r = 24;
        cfg.g = 24;
        cfg.b = 24;
        cfg.a = 24;

        new LwjglApplication(new BaseEngine(), cfg);
    }
}
