package dot.empire.myris.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import dot.empire.myris.Myris;

/**
 * Brightness/Contrast {@link SpriteBatch}. Modify the brightness/contrast of sprites and images rendered in libGDX.
 *
 * @see <a href="https://github.com/mattdesl/lwjgl-basics/wiki/LibGDX-Brightness-&-Contrast">LibGDX Brightness & Contrast</a>
 */
// https://github.com/mattdesl/lwjgl-basics/wiki/LibGDX-Brightness-&-Contrast
public final class ShaderBatch extends SpriteBatch {

    private static final String VERTEX_SHADER = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n"
            + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n"
            + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
            + "uniform mat4 u_projTrans;\n"
            + "varying vec4 v_color;\n"
            + "varying vec2 v_texCoords;\n"
            + "\n"
            + "void main(void)\n"
            + "{\n"
            + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n"
            + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
            + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n"
            + "}";

    private static final String FRAGMENT_SHADER = "#ifdef GL_ES\n"
            + "#define LOWP lowp\n"
            + "precision mediump float;\n"
            + "#else\n"
            + "#define LOWP \n"
            + "#endif\n"
            + "varying LOWP vec4 v_color;\n"
            + "varying vec2 v_texCoords;\n"
            + "uniform sampler2D u_texture;\n"
            + "uniform float brightness;\n"
            + "uniform float contrast;\n"
            + "void main(void)\n"
            + "{\n"
            + "  vec4 color = v_color * texture2D(u_texture, v_texCoords);\n"
            + "  color.rgb /= color.a;\n"                                       // ignore alpha
            + "  color.rgb = ((color.rgb - 0.5) * max(contrast, 0.0)) + 0.5;\n" // apply contrast
            + "  color.rgb += brightness;\n"                                    // apply brightness
            + "  color.rgb *= color.a;\n"                                       // return alpha
            + "  gl_FragColor = color;\n"
            + "}";

    private final ShaderProgram shader;
    private float brightness;
    private float contrast;
    private int brightnessLoc = -1, contrastLoc = -1;

    public ShaderBatch(int size) {
        super(Math.min(size, 8191));

        this.contrast = 1f;
        this.brightness = 0;

        ShaderProgram.pedantic = false;

        this.shader = new ShaderProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        boolean isCompiled = shader.isCompiled();

        if (isCompiled) {
            super.setShader(shader);
            this.shader.begin();
            {
                this.brightnessLoc = shader.getUniformLocation("brightness");
                this.contrastLoc = shader.getUniformLocation("contrast");
            }
            this.shader.end();
        }

        Gdx.app.debug(Myris.TAG, "Shader batch Compiled = " + isCompiled);
        String log = shader.getLog().trim();
        if (!log.isEmpty()) {
            Gdx.app.debug(Myris.TAG, "Shader batch Log = " + log);
        }
    }


    public void begin(boolean useShader) {
        super.begin();
        if (useShader && shader != null) {
            if (brightnessLoc != -1) {
                this.shader.setUniformf(brightnessLoc, brightness);
            }
            if (contrastLoc != -1) {
                this.shader.setUniformf(contrastLoc, contrast);
            }
        }
    }

    /**
     * @see ShaderBatch#begin(boolean)
     * @deprecated Use overloaded method
     */
    @Override
    @Deprecated
    public void begin() {
        this.begin(true);
    }

    public float getBrightness() {
        return this.brightness;
    }

    /**
     * @param brightness clamped
     */
    public void setBrightness(float brightness) {
        this.brightness = MathUtils.clamp(brightness, -1, 1);
    }

    public float getContrast() {
        return this.contrast;
    }

    /**
     * @param contrast clamped
     */
    public void setContrast(float contrast) {
        this.contrast = Math.max(contrast, 0.1f);
    }

    @Override
    public String toString() {
        return "ShaderBatch{" + "brightness=" + brightness +
                ", contrast=" + contrast +
                '}';
    }
}
