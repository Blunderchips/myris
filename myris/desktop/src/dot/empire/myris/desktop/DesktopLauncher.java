package dot.empire.myris.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import dot.empire.myris.BaseEngine;

public class DesktopLauncher {

    /**
     * @param args Arguments from the command line
     */
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg
                = new LwjglApplicationConfiguration();

        new LwjglApplication(new BaseEngine(), cfg);
    }
}
