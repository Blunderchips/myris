package dot.empire.myris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 *
 */
// https://github.com/mattdesl/lwjgl-basics/wiki/LibGDX-Brightness-&-Contrast
public final class ShaderBatch extends SpriteBatch {

    private static final String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
            + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
            + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
            + "uniform mat4 u_projTrans;\n" //
            + "varying vec4 v_color;\n" //
            + "varying vec2 v_texCoords;\n" //
            + "\n" //
            + "void main()\n" //
            + "{\n" //
            + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
            + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
            + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
            + "}\n";

    private static final String fragmentShader = "#ifdef GL_ES\n" //
            + "#define LOWP lowp\n" //
            + "precision mediump float;\n" //
            + "#else\n" //
            + "#define LOWP \n" //
            + "#endif\n" //
            + "varying LOWP vec4 v_color;\n" //
            + "varying vec2 v_texCoords;\n" //
            + "uniform sampler2D u_texture;\n" //
            + "uniform float brightness;\n" //
            + "uniform float contrast;\n" //
            + "void main()\n"//
            + "{\n" //
            + "  vec4 color = v_color * texture2D(u_texture, v_texCoords);\n"
            + "  color.rgb /= color.a;\n" //ignore alpha
            + "  color.rgb = ((color.rgb - 0.5) * max(contrast, 0.0)) + 0.5;\n" //apply contrast
            + "  color.rgb += brightness;\n" //apply brightness
            + "  color.rgb *= color.a;\n" //return alpha
            + "  gl_FragColor = color;\n"
            + "}";

    public final boolean isCompiled;
    public float brightness;
    public float contrast;
    protected int brightnessLoc = -1, contrastLoc = -1;
    ShaderProgram shader;

    public ShaderBatch(int size) {
        super(size);

        this.contrast = 1f;
        this.brightness = 0;

        ShaderProgram.pedantic = false;

        this.shader = new ShaderProgram(vertexShader, fragmentShader);
        this.isCompiled = shader.isCompiled();

        if (isCompiled) {
            super.setShader(shader);
            {
                this.shader.begin();
                this.brightnessLoc = shader.getUniformLocation("brightness");
                this.contrastLoc = shader.getUniformLocation("contrast");
            }
            this.shader.end();
        }

        Gdx.app.debug("Shader batch", "Compiled = " + Boolean.toString(isCompiled));
        Gdx.app.debug("Shader batch", "Log = " + shader.getLog());
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

    @Override
    @Deprecated
    public void begin() {
        this.begin(true);
    }

    public float getBrightness() {
        return this.brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public float getContrast() {
        return this.contrast;
    }

    public void setContrast(float contrast) {
        this.contrast = contrast;
    }

//    @Override
//    public String toString() {
//        return MoreObjects.toStringHelper(this)
//                .add("isCompiled", isCompiled)
//                .add("log", log)
//                .add("brightness", brightness)
//                .add("contrast", contrast)
//                .add("brightnessLoc", brightnessLoc)
//                .add("contrastLoc", contrastLoc)
//                .add("shader", shader)
//                .toString();
//    }
}
