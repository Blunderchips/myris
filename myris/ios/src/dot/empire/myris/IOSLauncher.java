/*                       _
 *  _ __ ___  _   _ _ __(_)___
 * | '_ ` _ \| | | | '__| / __|
 * | | | | | | |_| | |  | \__ \
 * |_| |_| |_|\__, |_|  |_|___/
 *            |___/
 */
package dot.empire.myris;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

/**
 * Apple launcher. <strong>Not sure if this works.</strong> Created 13/11/2018.
 */
public final class IOSLauncher extends IOSApplication.Delegate {

    /**
     * @param args Arguments from the command line
     */
    public static void main(String[] args) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(args, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    protected IOSApplication createApplication() {
        final IOSApplicationConfiguration cfg
                = new IOSApplicationConfiguration();

        cfg.useCompass = false;
        cfg.useAccelerometer = false;
        // cfg.useGL30 = true;

        return new IOSApplication(new Myris(), cfg);
    }
}