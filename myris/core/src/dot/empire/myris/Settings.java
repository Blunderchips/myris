package dot.empire.myris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Preferences. Created 16/11/2018.
 *
 * @author Matthew 'siD' Van der Bijl
 * @see com.badlogic.gdx.Preferences
 */
public final class Settings {

    private static String BRIGHTNESS = "brightness";
    private static String CONTRAST = "contrast";
    private static String IS_MUTED = "muted";

    private final Preferences preferences;

    public Settings() {
        this.preferences = Gdx.app.getPreferences(BaseEngine.TAG);
    }

    public float getBrightness() {
        return preferences.getFloat(BRIGHTNESS, 0);
    }

    public void setBrightness(float brightness) {
        this.preferences.putFloat(BRIGHTNESS, brightness);
        flush();
    }

    public float getContrast() {
        return preferences.getFloat(CONTRAST, 1);
    }

    public void setContrast(float contrast) {
        this.preferences.putFloat(CONTRAST, contrast);
        flush();
    }

    private void flush() {
        // Gdx.app.debug(BaseEngine.TAG, "saving preferences");
        this.preferences.flush();
    }

    public boolean isMuted() {
        return preferences.getBoolean(IS_MUTED, false);
    }
}
