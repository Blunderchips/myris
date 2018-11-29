package dot.empire.myris;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

/**
 * General definitions and constants. All constant should be {@code public}, {@code static} and {@code final}.
 * Tag in {@code BaseEngine} class. Created 15/11/2018.
 *
 * @author Matthew 'siD' Van der Bijl
 */
public final class Defines {

    /**
     * Width of the screen in pixels. Constant for viewport and desktop launcher.
     */
    public static final int SCREEN_WIDTH = 480;
    /**
     * Height of the screen in pixels. Constant for viewport and desktop launcher.
     */
    public static final int SCREEN_HEIGHT = 800;

    /**
     * Image overlay to be rendered on top of main game screen.
     */
    @AnnotationAssetManager.Asset(Texture.class)
    public static final String GAME_OVERLAY = "gfx/game-overlay.png";
    /**
     * GIF played on loading screen.
     */
    public static final String LOADING_GIF = "gfx/cube-1.3s-200px.gif";

    /**
     * Main menu music.
     */
    @AnnotationAssetManager.Asset(Music.class)
    public static final String BG_MUSIC = "Kai_Engel_-_01_-_Brand_New_World.mp3";
    /**
     * Play {@link Button} image.
     */
    @AnnotationAssetManager.Asset(Texture.class)
    public static final String ICO_PLAY = "gfx/play-button.png";
    /**
     * High score {@link Button} image.
     */
    @AnnotationAssetManager.Asset(Texture.class)
    public static final String ICO_SCORE = "gfx/trophy-cup.png";
    /**
     * Settings menu {@link Button} image.
     */
    @AnnotationAssetManager.Asset(Texture.class)
    public static final String ICO_SETTINGS = "gfx/cog.png";
    /**
     * General info screen {@link Button} image.
     */
    @AnnotationAssetManager.Asset(Texture.class)
    public static final String ICO_INFO = "gfx/info.png";
    /**
     * Logo/Title image for main menu {@link Button} image.
     */
    @AnnotationAssetManager.Asset(Texture.class)
    public static final String IMG_TITLE = "gfx/title.png";
    /**
     * Return {@link Button} image.
     */
    @AnnotationAssetManager.Asset(Texture.class)
    public static final String ICO_BTN_BACK = "gfx/anticlockwise-rotation.png";

    /**
     * Logo image for splash screen.
     */
    @AnnotationAssetManager.Asset(Texture.class)
    public static final String IMG_LOGO = "gfx/logo.png";
    /**
     * Sound effect played on splash screen.
     */
    @AnnotationAssetManager.Asset(Music.class)
    public static final String SFX_INTRO = "sfx/intro.ogg";
    /**
     * Sound effect to be played when a block is collected.
     */
    @AnnotationAssetManager.Asset(Sound.class)
    public static final String SFX_COLLECT = "sfx/170147__timgormly__8-bit-coin.ogg";
    /**
     * Sound effect played on death.
     */
    @AnnotationAssetManager.Asset(Sound.class)
    public static final String SFX_DEATH = "sfx/death.mp3";
    /**
     * Sound effect played when a move is made.
     */
    @AnnotationAssetManager.Asset(Sound.class)
    public static final String SFX_CLICK = "sfx/275152__bird-man__click.ogg";

    /**
     * Message flags.
     *
     * @see com.badlogic.gdx.ai.msg.MessageManager
     */
    public interface Messages {

        int AD_SHOW = 0x0;
        int AD_HIDE = 0x1;
        int MSG_MUTE = 0x2;
    }

    /**
     * @param mngr {@code Myris#assetManager}
     */
    public static void loadAllAssets(AnnotationAssetManager mngr) {
        mngr.load(new Defines());
        for (int i = 0; i < 10; i++) {
            mngr.load("gfx/img_num_" + i + ".png", Texture.class);
        }
    }
}
