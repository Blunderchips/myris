/* Copyright by Johannes Borchardt */
/* LibGdx conversion 2014 by Anton Persson */
/* Released under Apache 2.0 */
/* https://code.google.com/p/animated-gifs-in-android/ */
package dot.empire.myris.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

// FIXME: 11 Nov 2018 Clean up, get block comments from source, check all collections
public class GifDecoder {

    /**
     * File read status: No errors.
     */
    private static final int STATUS_OK = 0x0;
    /**
     * File read status: Error decoding file (may be partially decoded).
     */
    private static final int STATUS_FORMAT_ERROR = 0x1;
    /**
     * File read status: Unable to open source.
     */
    private static final int STATUS_OPEN_ERROR = 0x2;
    /**
     * max decoder pixel stack size.
     */
    private static final int MAX_STACK_SIZE = 4096;
    /**
     * Current data block.
     */
    private final byte[] block = new byte[256];
    private InputStream in;
    private int status;
    /**
     * Full image width.
     */
    private int width;
    /**
     * Full image height.
     */
    private int height;
    /**
     * Global colour table used.
     */
    private boolean gctFlag;
    /**
     * Size of global colour table.
     */
    private int gctSize;
    /**
     * Global colour table.
     */
    private int[] gct;
    /**
     * Local colour table.
     */
    private int[] lct;
    /**
     * Active colour table.
     */
    private int[] act;
    /**
     * Background colour index.
     */
    private int bgIndex;
    /**
     * Background colour.
     */
    private int bgColour;
    /**
     * Previous background colour.
     */
    private int lastBgColour;
    /**
     * Interlace flag.
     */
    private boolean interlace;
    private int ix;
    private int iy;
    private int iw;
    private int ih; // current image rectangle
    private int lrx;
    private int lry;
    private int lrw;
    private int lrh;
    /**
     * Current frame.
     */
    private DixieMap image;
    /**
     * Previous frame.
     */
    private DixieMap lastPixmap;
    /**
     * Block size last graphic control extension info.
     */
    private int blockSize = 0;
    private int dispose = 0; // 0=no action; 1=leave in place; 2=restore to bg; 3=restore to prev
    private int lastDispose = 0;
    private boolean transparency = false; // use transparent colour
    private int delay = 0; // delay in milliseconds
    private int transIndex; // transparent colour index
    // LZW decoder working arrays
    private short[] prefix;
    private byte[] suffix;
    private byte[] pixelStack;
    private byte[] pixels;
    /**
     * Frames read from current file.
     */
    private List<GifFrame> frames;
    /**
     * Frame count.
     */
    private int frameCnt;

    public static Animation<TextureRegion> loadGIFAnimation(Animation.PlayMode playMode, InputStream is) {
        GifDecoder gdec = new GifDecoder();
        gdec.read(is);
        return gdec.getAnimation(playMode);
    }

    /**
     * Initializes.
     */
    public GifDecoder() {
        this.status = STATUS_OK;
        this.frameCnt = 0;
        this.frames = new LinkedList<GifFrame>();
        this.gct = null;
        this.lct = null;
    }

    /**
     * Gets display duration for specified frame.
     *
     * @return delay in milliseconds
     */
    private int getDelay() {
        this.delay = -1;
        if ((0 < frameCnt)) {
            this.delay = frames.get(0).delay;
        }
        return delay;
    }

    /**
     * Gets the number of frames read from file.
     *
     * @return frame count
     */
    // FIXME: 16 Nov 2018 Remove
    private int getFrameCount() {
        return this.frameCnt;
    }

    /**
     * Creates new frame image from current data (and previous frames as specified by their disposition codes).
     */
    private void setPixels() {
        // expose destination image'display pixels as int array
        int[] dest = new int[width * height];
        // fill in starting image contents based on last image'display dispose code
        if (lastDispose > 0) {
            if (lastDispose == 3) {
                // use image before last
                int n = frameCnt - 2;
                if (n > 0) {
                    lastPixmap = getFrame(n - 1);
                } else {
                    lastPixmap = null;
                }
            }
            if (lastPixmap != null) {
                lastPixmap.getPixels(dest, 0, width, width, height);
                // copy pixels
                if (lastDispose == 2) {
                    // fill last image rect area with background colour
                    int c = 0;
                    if (!transparency) {
                        c = lastBgColour;
                    }
                    for (int i = 0; i < lrh; i++) {
                        int n1 = (lry + i) * width + lrx;
                        int n2 = n1 + lrw;
                        for (int k = n1; k < n2; k++) {
                            dest[k] = c;
                        }
                    }
                }
            }
        }
        // copy each source line to the appropriate place in the destination
        int pass = 1;
        int inc = 8;
        int iline = 0;
        for (int i = 0; i < ih; i++) {
            int line = i;
            if (interlace) {
                if (iline >= ih) {
                    pass++;
                    switch (pass) {
                        case 2:
                            iline = 4;
                            break;
                        case 3:
                            iline = 2;
                            inc = 4;
                            break;
                        case 4:
                            iline = 1;
                            inc = 2;
                            break;
                        default:
                            break;
                    }
                }
                line = iline;
                iline += inc;
            }
            line += iy;
            if (line < height) {
                int k = line * width;
                int dx = k + ix; // start of line in dest
                int dlim = dx + iw; // end of dest line
                if ((k + width) < dlim) {
                    dlim = k + width; // past dest edge
                }
                int sx = i * iw; // start of line in source
                while (dx < dlim) {
                    // map colour and insert in destination
                    int index = ((int) pixels[sx++]) & 0xff;
                    int c = act[index];
                    if (c != 0) {
                        dest[dx] = c;
                    }
                    dx++;
                }
            }
        }
        image = new DixieMap(dest, width, height);
        //Pixmap.createPixmap(dest, width, height, Config.ARGB_4444);
    }

    /**
     * Gets the image contents of frame n.
     *
     * @return BufferedPixmap representation of frame, or null if n is invalid.
     */
    private DixieMap getFrame(int n) {
        if (frameCnt <= 0)
            return null;
        n = n % frameCnt;
        return frames.get(n).image;
    }

    /**
     * Reads GIF image from stream
     *
     * @param is containing GIF file.
     */
    private void read(InputStream is) {
        if (is != null) {
            in = is;
            readHeader();
            if (!err()) {
                readContents();
                if (frameCnt < 0) {
                    status = STATUS_FORMAT_ERROR;
                }
            }
        } else {
            status = STATUS_OPEN_ERROR;
        }
        try {
            assert is != null;
            is.close();
        } catch (Exception ex) {
            Gdx.app.error("GifDecoder", "cannot read gif", ex);
        }
    }

    /**
     * Decodes LZW image data into pixel array. Adapted from John Cristy'display BitmapMagick.
     */
    private void decodeBitmapData() {
        int nullCode = -1;
        int npix = iw * ih;

        int available, clear, code_mask, code_size, end_of_information, in_code,
                old_code, bits, code, count, i, datum, data_size, first, top, bi, pi;

        if ((pixels == null) || (pixels.length < npix)) {
            this.pixels = new byte[npix]; // allocate new pixel array
        }
        if (prefix == null) {
            this.prefix = new short[MAX_STACK_SIZE];
        }
        if (suffix == null) {
            this.suffix = new byte[MAX_STACK_SIZE];
        }
        if (pixelStack == null) {
            this.pixelStack = new byte[MAX_STACK_SIZE + 1];
        }
        // Initialize GIF data stream decoder.
        data_size = read();
        clear = 1 << data_size;
        end_of_information = clear + 1;
        available = clear + 2;
        old_code = nullCode;
        code_size = data_size + 1;
        code_mask = (1 << code_size) - 1;
        for (code = 0; code < clear; code++) {
            prefix[code] = 0; // XXX ArrayIndexOutOfBoundsException
            suffix[code] = (byte) code;
        }
        // Decode GIF pixel stream.
        datum = bits = count = first = top = pi = bi = 0;
        for (i = 0; i < npix; ) {
            if (top == 0) {
                if (bits < code_size) {
                    // Load bytes until there are enough bits for a code.
                    if (count == 0) {
                        // Read a new data block.
                        count = readBlock();
                        if (count <= 0) {
                            break;
                        }
                        bi = 0;
                    }
                    datum += (((int) block[bi]) & 0xff) << bits;
                    bits += 8;
                    bi++;
                    count--;
                    continue;
                }
                // Get the next code.
                code = datum & code_mask;
                datum >>= code_size;
                bits -= code_size;
                // Interpret the code
                if ((code > available) || (code == end_of_information)) {
                    break;
                }
                if (code == clear) {
                    // Reset decoder.
                    code_size = data_size + 1;
                    code_mask = (1 << code_size) - 1;
                    available = clear + 2;
                    old_code = nullCode;
                    continue;
                }
                if (old_code == nullCode) {
                    pixelStack[top++] = suffix[code];
                    old_code = code;
                    first = code;
                    continue;
                }
                in_code = code;
                if (code == available) {
                    pixelStack[top++] = (byte) first;
                    code = old_code;
                }
                while (code > clear) {
                    pixelStack[top++] = suffix[code];
                    code = prefix[code];
                }
                first = ((int) suffix[code]) & 0xff;
                // Add a new string to the string table,
                if (available >= MAX_STACK_SIZE) {
                    break;
                }
                pixelStack[top++] = (byte) first;
                prefix[available] = (short) old_code;
                suffix[available] = (byte) first;
                available++;
                if (((available & code_mask) == 0) && (available < MAX_STACK_SIZE)) {
                    code_size++;
                    code_mask += available;
                }
                old_code = in_code;
            }
            // Pop a pixel off the pixel stack.
            top--;
            pixels[pi++] = pixelStack[top];
            i++;
        }
        for (i = pi; i < npix; i++) {
            pixels[i] = 0; // clear missing pixels
        }
    }

    /**
     * @return true if an error was encountered during reading/decoding
     */
    private boolean err() {
        return status != STATUS_OK;
    }

    /**
     * Reads a single byte from the input stream.
     */
    private int read() {
        int curByte = 0;
        try {
            curByte = in.read();
        } catch (Exception e) {
            status = STATUS_FORMAT_ERROR;
        }
        return curByte;
    }

    /**
     * Reads next variable length block from input.
     *
     * @return number of bytes stored in "buffer"
     */
    private int readBlock() {
        blockSize = read();
        int n = 0;
        if (blockSize > 0) {
            try {
                int count;
                while (n < blockSize) {
                    count = in.read(block, n, blockSize - n);
                    if (count == -1) {
                        break;
                    }
                    n += count;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (n < blockSize) {
                status = STATUS_FORMAT_ERROR;
            }
        }
        return n;
    }

    /**
     * Reads colour table as 256 RGB integer values
     *
     * @param ncolours number of colours to read (integer)
     * @return int array containing 256 colours (packed ARGB with full alpha)
     */
    private int[] readColourTable(int ncolours) {
        int nbytes = 3 * ncolours;
        int[] tab = null;
        byte[] c = new byte[nbytes];
        int n = 0;
        try {
            n = in.read(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (n < nbytes) {
            status = STATUS_FORMAT_ERROR;
        } else {
            tab = new int[256]; // max size to avoid bounds checks
            int i = 0;
            int j = 0;
            while (i < ncolours) {
                int r = ((int) c[j++]) & 0xff;
                int g = ((int) c[j++]) & 0xff;
                int b = ((int) c[j++]) & 0xff;
                tab[i++] = 0xff000000 | (r << 16) | (g << 8) | b;
            }
        }
        return tab;
    }

    /**
     * Main file parser. Reads GIF content blocks.
     */
    private void readContents() {
        // read GIF file content blocks
        boolean done = false;
        while (!(done || err())) {
            int code = read();
            switch (code) {
                case 0x2C: // image separator
                    readBitmap();
                    break;
                case 0x21: // extension
                    code = read();
                    switch (code) {
                        case 0xf9: // graphics control extension
                            readGraphicControlExt();
                            break;
                        case 0xff: // application extension
                            readBlock();
                            StringBuilder app = new StringBuilder();
                            for (int i = 0; i < 11; i++) {
                                app.append((char) block[i]);
                            }
                            if (app.toString().equals("NETSCAPE2.0")) {
                                readNetscapeExt();
                            } else {
                                skip(); // don't care
                            }
                            break;
                        case 0xfe:// comment extension
                            skip();
                            break;
                        case 0x01:// plain text extension
                            skip();
                            break;
                        default: // uninteresting extension
                            skip();
                    }
                    break;
                case 0x3b: // terminator
                    done = true;
                    break;
                case 0x00: // bad byte, but keep going and see what happens break;
                default:
                    status = STATUS_FORMAT_ERROR;
            }
        }
    }

    /**
     * Reads Graphics Control Extension values.
     */
    private void readGraphicControlExt() {
        read(); // block size
        int packed = read(); // packed fields
        dispose = (packed & 0x1c) >> 2; // disposal method
        if (dispose == 0) {
            dispose = 1; // elect to keep old image if discretionary
        }
        transparency = (packed & 1) != 0;
        delay = readShort() * 10; // delay in milliseconds
        transIndex = read(); // transparent colour index
        read(); // block terminator
    }

    /**
     * Reads GIF file header information.
     */
    private void readHeader() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            id.append((char) read());
        }
        if (!id.toString().startsWith("GIF")) {
            status = STATUS_FORMAT_ERROR;
            return;
        }
        readLSD();
        if (gctFlag && !err()) {
            gct = readColourTable(gctSize);
            bgColour = gct[bgIndex];
        }
    }

    /**
     * Reads next frame image.
     */
    private void readBitmap() {
        ix = readShort(); // (sub)image position & size
        iy = readShort();
        iw = readShort();
        ih = readShort();
        int packed = read();
        // local colour table flag
        boolean lctFlag = (packed & 0x80) != 0;
        // local colour table size
        int lctSize = (int) Math.pow(2, (packed & 0x07) + 1);
        // 3 - sort flag
        // 4-5 - reserved lctSize = 2 << (packed & 7); // 6-8 - local colour
        // table size
        interlace = (packed & 0x40) != 0;
        if (lctFlag) {
            lct = readColourTable(lctSize); // read table
            act = lct; // make local table active
        } else {
            act = gct; // make global table active
            if (bgIndex == transIndex) {
                bgColour = 0;
            }
        }
        int save = 0;
        if (transparency) {
            save = act[transIndex];
            act[transIndex] = 0; // set transparent colour if specified
        }
        if (act == null) {
            status = STATUS_FORMAT_ERROR; // no colour table defined
        }
        if (err()) {
            return;
        }
        decodeBitmapData(); // decode pixel data
        skip();
        if (err()) {
            return;
        }
        frameCnt++;
        // create new image to receive frame data
        image = new DixieMap(width, height);
        setPixels(); // transfer pixel data to image
        frames.add(new GifFrame(image, delay)); // add image to frame
        // list
        if (transparency) {
            act[transIndex] = save;
        }
        resetFrame();
    }

    /**
     * Reads Logical Screen Descriptor.
     */
    private void readLSD() {
        // logical screen size
        width = readShort();
        height = readShort();
        // packed fields
        int packed = read();
        gctFlag = (packed & 0x80) != 0; // 1 : global colour table flag
        // 2-4 : colour resolution
        // 5 : gct sort flag
        gctSize = 2 << (packed & 7); // 6-8 : gct size
        bgIndex = read(); // background colour index
        // pixel aspect ratio
        read();
    }

    /**
     * Reads Netscape extension to obtain iteration count.
     */
    private void readNetscapeExt() {
        do {
            readBlock();
        } while ((blockSize > 0) && !err());
    }

    /**
     * Reads next 16-bit value, LSB first.
     */
    private int readShort() {
        // read 16-bit value, LSB first
        return read() | (read() << 8);
    }

    /**
     * Resets frame state for reading next image.
     */
    private void resetFrame() {
        this.lastDispose = dispose;
        this.lrx = ix;
        this.lry = iy;
        this.lrw = iw;
        this.lrh = ih;
        this.lastPixmap = image;
        this.lastBgColour = bgColour;
        this.dispose = 0;
        this.transparency = false;
        this.delay = 0;
        this.lct = null;
    }

    /**
     * Skips variable length blocks up to and including next zero length block.
     */
    private void skip() {
        do {
            readBlock();
        } while ((blockSize > 0) && !err());
    }

    private Animation<TextureRegion> getAnimation(PlayMode playMode) {

        int nrFrames = getFrameCount();
        Pixmap frame = getFrame(0);
        assert frame != null;
        int width = frame.getWidth();
        int height = frame.getHeight();
        int vzones = (int) Math.sqrt((double) nrFrames);
        int hzones = vzones;

        while (vzones * hzones < nrFrames) vzones++;

        int v, h;

        Pixmap target = new Pixmap(width * hzones, height * vzones, Pixmap.Format.RGBA8888);

        for (h = 0; h < hzones; h++) {
            for (v = 0; v < vzones; v++) {
                int frameID = v + h * vzones;
                if (frameID < nrFrames) {
                    frame = getFrame(frameID);
                    assert frame != null;
                    target.drawPixmap(frame, h * width, v * height);
                }
            }
        }

        Texture texture = new Texture(target);
        Array<TextureRegion> texReg = new Array<TextureRegion>();

        for (h = 0; h < hzones; h++) {
            for (v = 0; v < vzones; v++) {
                int frameID = v + h * vzones;
                if (frameID < nrFrames) {
                    TextureRegion tr = new TextureRegion(texture, h * width, v * height, width, height);
                    texReg.add(tr);
                }
            }
        }
        float frameDuration = (float) getDelay();
        frameDuration /= 1000; // convert milliseconds into seconds

        return new Animation<TextureRegion>(frameDuration, texReg, playMode);
    }

    private static class DixieMap extends Pixmap {

        DixieMap(int w, int h) {
            super(w, h, Format.RGBA8888);
        }

        DixieMap(int[] data, int w, int h) {
            super(w, h, Format.RGBA8888);

            int x, y;

            for (y = 0; y < h; y++) {
                for (x = 0; x < w; x++) {
                    int pxl_ARGB8888 = data[x + y * w];
                    int pxl_RGBA8888 =
                            ((pxl_ARGB8888 >> 24) & 0x000000ff) | ((pxl_ARGB8888 << 8) & 0xffffff00);
                    // convert ARGB8888 > RGBA8888
                    drawPixel(x, y, pxl_RGBA8888);
                }
            }
        }

        void getPixels(int[] pixels, int offset, int stride, int width, int height) {
            java.nio.ByteBuffer bb = getPixels();

            int k, l;

            for (k = 0; k < height; k++) {
                int _offset = offset;
                for (l = 0; l < width; l++) {
                    int pxl = bb.getInt(4 * (l + k * width));

                    // convert RGBA8888 > ARGB8888
                    pixels[_offset++] = ((pxl >> 8) & 0x00ffffff) | ((pxl << 24) & 0xff000000);
                }
                offset += stride;
            }
        }
    }

    private static class GifFrame {

        final DixieMap image;
        final int delay;

        /**
         * @param img Image
         * @param del Delay
         */
        GifFrame(DixieMap img, int del) {
            this.image = img;
            this.delay = del;
        }
    }
}