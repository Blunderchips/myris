package dot.empire.myris;

import android.os.Bundle;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import static dot.empire.myris.BaseEngine.AD_HIDE;
import static dot.empire.myris.BaseEngine.AD_SHOW;

/**
 * Android Launcher. Should be the main {@link android.app.Activity}.
 *
 * @author Matthew 'siD' Van der Bijl
 * @see dot.empire.myris.BaseEngine
 */
public final class AndroidLauncher extends AndroidApplication implements Telegraph {

    private boolean isShowing;

    public AndroidLauncher() {
        this.isShowing = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MessageManager.getInstance().addListener(this, AD_SHOW);
        MessageManager.getInstance().addListener(this, AD_HIDE);

        final AndroidApplicationConfiguration cfg
                = new AndroidApplicationConfiguration();

        cfg.numSamples = 8;
        // cfg.r = 24;
        // cfg.g = 24;
        // cfg.b = 24;
        // cfg.a = 24;
        cfg.useImmersiveMode = true;
        cfg.useCompass = false;
        cfg.useAccelerometer = false;

        super.initialize(new BaseEngine(), cfg);
    }

    @Override
    public boolean handleMessage(Telegram telegram) {
        switch (telegram.message) {
            case AD_HIDE:
                // TODO: 14 Nov 2018  
                return true;
            case AD_SHOW:
                // TODO: 14 Nov 2018
                return true;
        }
        return false;
    }
}
