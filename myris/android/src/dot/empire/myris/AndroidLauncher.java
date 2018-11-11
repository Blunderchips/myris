package dot.empire.myris;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import org.jetbrains.annotations.Nullable;

/**
 * Android Launcher.
 *
 * @author Matthew 'siD' Van der Bijl
 */
public final class AndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AndroidApplicationConfiguration cfg
                = new AndroidApplicationConfiguration();

        cfg.numSamples = 8;
        cfg.r = 24;
        cfg.g = 24;
        cfg.b = 24;
        cfg.a = 24;

        super.initialize(new BaseEngine(), cfg);
    }
}
