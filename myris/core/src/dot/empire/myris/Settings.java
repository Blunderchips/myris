package dot.empire.myris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import dot.empire.myris.gfx.Score;

/**
 * General settings and preferences. Created 16/11/2018.
 *
 * @author Matthew 'siD' Van der Bijl
 * @see com.badlogic.gdx.Preferences
 */
public final class Settings {

    /**
     * Location of brightness setting value in preference map (float).
     */
    private static final String BRIGHTNESS = "brightness";
    /**
     * Location of brightness setting value in preference map (float).
     */
    private static final String CONTRAST = "contrast";
    /**
     * Location of brightness setting value in preference map (bool).
     */
    private static final String IS_MUTED = "muted";
    /**
     * Location of high score in preference map (long).
     */
    private static final String HIGH_SCORE = "high_score";

    /**
     * Preference map.
     */
    private final Preferences preferences;

    public Settings() {
        this.preferences = Gdx.app.getPreferences(Myris.TAG);
    }

    public float getBrightness() {
        return preferences.getFloat(BRIGHTNESS, 0);
    }

    public void setBrightness(float brightness) {
        this.preferences.putFloat(BRIGHTNESS, brightness);
        this.preferences.flush();
    }

    public float getContrast() {
        return preferences.getFloat(CONTRAST, 1);
    }

    public void setContrast(float contrast) {
        this.preferences.putFloat(CONTRAST, contrast);
        this.preferences.flush();
    }

    /**
     * @return whether all sounds should be muted or not
     */
    public boolean isMuted() {
        return preferences.getBoolean(IS_MUTED, false);
    }

    public void setIsMuted(boolean isMuted) {
        this.preferences.putBoolean(IS_MUTED, isMuted);
        this.preferences.flush();
    }

    public void setHighScore(Score score){

    }
}
