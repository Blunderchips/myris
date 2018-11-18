package dot.empire.myris;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

/**
 * Apple launcher. Created 13/11/2018.
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

        return new IOSApplication(new Myris(), cfg);
    }
}