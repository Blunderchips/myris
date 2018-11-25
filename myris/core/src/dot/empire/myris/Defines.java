package dot.empire.myris;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

/**
 * General definitions and constants. Created 15/11/2018.
 *
 * @author Matthew 'siD' Van der Bijl
 */
public final class Defines {

    @AnnotationAssetManager.Asset(Texture.class)
    public static final String GAME_OVERLAY = "gfx/game-overlay.png";

    public static final String LOADING_GIF = "gfx/cube-1.3s-200px.gif";

    @AnnotationAssetManager.Asset(Music.class)
    public static final String BG_MUSIC = "Kai_Engel_-_01_-_Brand_New_World.mp3";
    @AnnotationAssetManager.Asset(Texture.class)
    public static final String ICO_PLAY = "gfx/play-button.png";
    @AnnotationAssetManager.Asset(Texture.class)
    public static final String ICO_SCORE = "gfx/trophy-cup.png";
    @AnnotationAssetManager.Asset(Texture.class)
    public static final String ICO_SETTINGS = "gfx/cog.png";
    @AnnotationAssetManager.Asset(Texture.class)
    public static final String ICO_INFO = "gfx/info.png";
    @AnnotationAssetManager.Asset(Texture.class)
    public static final String IMG_TITLE = "gfx/title.png";
    @AnnotationAssetManager.Asset(Texture.class)
    public static final String ICO_BTN_BACK = "gfx/anticlockwise-rotation.png";

    @AnnotationAssetManager.Asset(Texture.class)
    public static final String IMG_LOGO = "gfx/logo.png";
    @AnnotationAssetManager.Asset(Music.class)
    public static final String SFX_INTRO = "sfx/intro.ogg";

    /**
     * @see com.badlogic.gdx.ai.msg.MessageManager
     */
    public interface Messages {

        int AD_SHOW = 0x0;
        int AD_HIDE = 0x1;
        int MSG_MUTE = 0x2;
    }

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
}
