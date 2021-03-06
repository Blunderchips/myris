/*                       _
 *  _ __ ___  _   _ _ __(_)___
 * | '_ ` _ \| | | | '__| / __|
 * | | | | | | |_| | |  | \__ \
 * |_| |_| |_|\__, |_|  |_|___/
 *            |___/
 */
package dot.empire.myris.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import dot.empire.myris.Myris;
import org.oxbow.swingbits.dialog.task.TaskDialogs;

import java.awt.*;

import static dot.empire.myris.Defines.SCREEN_HEIGHT;
import static dot.empire.myris.Defines.SCREEN_WIDTH;

/**
 * For testing. Window size from {@code dot.empire.myris.Defines}. Created 07/11/2018.
 *
 * @author Matthew 'siD' Van der Bijl
 * @see Myris
 */
public final class DesktopLauncher {

    /**
     * Prevent instatitation.
     */
    @Deprecated
    private DesktopLauncher() {
    }

    /**
     * @param args Arguments from the command line
     */
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static void main(String[] args) {
        final LwjglApplicationConfiguration cfg
                = new LwjglApplicationConfiguration();

        cfg.title = "myris";

        cfg.samples = 8;
        cfg.resizable = false;

        cfg.width = SCREEN_WIDTH;
        cfg.height = SCREEN_HEIGHT;

        cfg.r = 24;
        cfg.g = 24;
        cfg.b = 24;
        cfg.a = 24;

        cfg.useGL30 = true;

        try {
            new LwjglApplication(new Myris(), cfg);
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            Toolkit.getDefaultToolkit().beep();
            TaskDialogs.showException(t);
        }
    }
}
