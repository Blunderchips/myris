/*                       _
 *  _ __ ___  _   _ _ __(_)___
 * | '_ ` _ \| | | | '__| / __|
 * | | | | | | |_| | |  | \__ \
 * |_| |_| |_|\__, |_|  |_|___/
 *            |___/
 */
package dot.empire.myris;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import static dot.empire.myris.Defines.Messages.*;

/**
 * Android Launcher. Should be the main {@code android.app.Activity}.
 *
 * @author Matthew 'siD' Van der Bijl
 * @see dot.empire.myris.Myris
 */
// https://github.com/libgdx/libgdx/wiki/Admob-in-libgdx
// https://github.com/Blunderchips/dot_Juggle/blob/master/juggle-libgdx/android/src/dot/empire/juggle/AndroidLauncher.java
public final class AndroidLauncher extends AndroidApplication implements Telegraph {

    /**
     * Link to GitHub repository.
     */
    private static final String GITHUB_REPO_URL = "https://github.com/Blunderchips/myris/";

    private boolean isMuted;

    protected AdView adView;
    private boolean isShowing;


    public AndroidLauncher() {
        this.isShowing = false;
    }

    /**
     * Called when the activity is starting.  This is where most initialization
     * should go: calling {@code setContentView(int)} to inflate the
     * activity's UI, using {@code findViewById} to programmatically interact
     * with widgets in the UI, calling
     * {@code #managedQuery(android.net.Uri, String[], String, String[], String)} to retrieve
     * cursors for data being displayed, etc.
     *
     * <p>You can call {@code finish} from within this function, in
     * which case {@code onDestroy()} will be immediately called without any of the rest
     * of the activity lifecycle ({@code onStart}, {@code onResume},
     * {@code onPause}, etc) executing.
     *
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@code onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MessageManager.getInstance().addListener(this, AD_SHOW);
        MessageManager.getInstance().addListener(this, AD_HIDE);
        MessageManager.getInstance().addListener(this, OPEN_ABOUT);

        final AndroidApplicationConfiguration cfg
                = new AndroidApplicationConfiguration();

        cfg.numSamples = 16;
        //cfg.r = 24;
        //cfg.g = 24;
        //cfg.b = 24;
        //cfg.a = 24;
        cfg.useImmersiveMode = true;
        cfg.useCompass = false;
        cfg.useAccelerometer = false;

        RelativeLayout layout = new RelativeLayout(this);

        View gameView = super.initializeForView(new Myris(), cfg);
        layout.addView(gameView);

        this.adView = new AdView(this);
        this.adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                int visiblity = adView.getVisibility();
                adView.setVisibility(AdView.GONE);
                adView.setVisibility(visiblity);
            }
        });
        this.adView.setAdSize(AdSize.SMART_BANNER);
        // http://www.google.com/admob
        this.adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111"); // https://developers.google.com/admob/android/test-ads

        AdRequest.Builder builder = new AdRequest.Builder();
        //run once before uncommenting the following line. Get TEST device ID from the logcat logs.
//        builder.addTestDevice("INSERT TEST DEVICE ID HERE");
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        layout.addView(adView, adParams);
        this.adView.loadAd(builder.build());
        this.adView.setVisibility(View.GONE);

        setContentView(layout);
    }

    /**
     * @param telegram the incoming message
     * @return whether the message has been handled or not
     * @see dot.empire.myris.Defines.Messages
     */
    @Override
    public boolean handleMessage(Telegram telegram) {
        switch (telegram.message) {
            case AD_HIDE:
                Gdx.app.log(Myris.TAG, "Hide ad");
                if (isShowing) {
                    this.isShowing = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adView.setVisibility(View.GONE);
                        }
                    });
                }
                return true;
            case AD_SHOW:
                Gdx.app.log(Myris.TAG, "Show ad");
                if (!isShowing) {
                    this.isShowing = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adView.setVisibility(View.VISIBLE);
                        }
                    });
                }
                return true;
            case OPEN_ABOUT:
                openAbout();
                return true;
        }
        return false;
    }

    /**
     * Called to process key events.  You can override this to intercept all
     * key events before they are dispatched to the window.  Be sure to call
     * this implementation for key events that should be handled normally.
     *
     * @param evt The key event
     * @return boolean Return true if this event was consumed.
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                MessageManager.getInstance().dispatchMessage(UNMUTE);
                if (isMuted) {
                    this.isMuted = false;
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                MessageManager.getInstance().dispatchMessage(MUTE);
                if (!isMuted) {
                    this.isMuted = true;
                    return true;
                }
                break;
        }
        return super.dispatchKeyEvent(evt);
    }

    /**
     * Called when a key was pressed down and not handled by any of the views
     * inside of the activity. So, for example, key presses while the cursor
     * is inside a TextView will not trigger the event (unless it is a navigation
     * to another object) because TextView handles its own key presses.
     *
     * <p>If the focused view didn't want this event, this method is called.
     *
     * <p>The default implementation takes care of {@code KeyEvent#KEYCODE_BACK}
     * by calling {@code onBackPressed()}, though the behavior varies based
     * on the application compatibility mode: for
     * {@code android.os.Build.VERSION_CODES#ECLAIR} or later applications,
     * it will set up the dispatch to call {@code onKeyUp} where the action
     * will be performed; for earlier applications, it will perform the
     * action immediately in on-down, as those versions of the platform
     * behaved.
     *
     * <p>Other additional default key handling may be performed
     * if configured with {@code setDefaultKeyMode}.
     *
     * @return <code>true</code> to prevent this event from being propagated
     * further, or <code>false</code> to indicate that you have not handled
     * this event and it should continue to be propagated.
     */
    // https://stackoverflow.com/questions/11182703/check-if-back-key-was-pressed-in-android
    @Override
    public boolean onKeyDown(int key, KeyEvent evt) {
        if (key == KeyEvent.KEYCODE_BACK) {
            MessageManager.getInstance().dispatchMessage(BACK_KEY_PRESSED);
            return true;
        }
        return super.onKeyDown(key, evt);
    }

    /**
     * Call on back button pressed.
     */
    @Override
    public void onBackPressed() {
        MessageManager.getInstance().dispatchMessage(BACK_KEY_PRESSED);
    }

    /**
     * Opens git repo in native web browser.
     */
    private void openAbout() {
        super.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_REPO_URL)));
    }
}
