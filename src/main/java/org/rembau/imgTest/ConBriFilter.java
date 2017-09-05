package org.rembau.imgTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConBriFilter {

    private static float contrast = 1.5f; // default value;
    private static float brightness = 0.1f; // default value;

    private static float _brightness = 0.3f; // default value;

    public ConBriFilter() {
        // do stuff here if you need......
    }

    public static void main(String args[]) {
        mark("C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\yzm2.jpg", "C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\yzm2_1.jpg", 40, 40, 40, 40);
    }
    public static void mark(String src, String dst, int x, int y, int width, int height) {

        try {
            BufferedImage source = ImageIO.read(new File(src));
            BufferedImage filter = filter(source, null, x, y, width, height);

            FileOutputStream output = null;
            output = new FileOutputStream(new File(dst));
            ImageIO.write(filter, "jpg", output);
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage filter(BufferedImage src, BufferedImage dest, int x, int y, int width, int height) {
        int imgWidth = src.getWidth();
        int imgHeight = src.getHeight();

        if ( dest == null )
            dest = new BufferedImage(src.getWidth(), src.getHeight(),
                    BufferedImage.TYPE_INT_RGB);

        int[] inPixels = new int[imgWidth*imgHeight];
        int[] outPixels = new int[imgWidth*imgHeight];
        src.getRGB( 0, 0, imgWidth, imgHeight, inPixels, 0, imgWidth);

        // calculate RED, GREEN, BLUE means of pixel
        int index = 0;
        int[] rgbmeans = new int[3];
        double redSum = 0, greenSum = 0, blueSum = 0;
        double total = imgHeight * imgWidth;
        for(int row=0; row<imgHeight; row++) {
            int ta = 0, tr = 0, tg = 0, tb = 0;
            for(int col=0; col<imgWidth; col++) {
                index = row * imgWidth + col;
                ta = (inPixels[index] >> 24) & 0xff;
                tr = (inPixels[index] >> 16) & 0xff;
                tg = (inPixels[index] >> 8) & 0xff;
                tb = inPixels[index] & 0xff;
                redSum += tr;
                greenSum += tg;
                blueSum +=tb;
            }
        }

        rgbmeans[0] = (int)(redSum / total);
        rgbmeans[1] = (int)(greenSum / total);
        rgbmeans[2] = (int)(blueSum / total);

        // adjust contrast and brightness algorithm, here
        for(int row=0; row<imgHeight; row++) {
            int ta = 0, tr = 0, tg = 0, tb = 0;
            for(int col=0; col<imgWidth; col++) {
                index = row * imgWidth + col;

                if (row >= y && row < y + height && col >= x && col < x+width) {
                    ta = (inPixels[index] >> 24) & 0xff;
                    tr = (inPixels[index] >> 16) & 0xff;
                    tg = (inPixels[index] >> 8) & 0xff;
                    tb = inPixels[index] & 0xff;

                    // remove means
                    tr -= rgbmeans[0];
                    tg -= rgbmeans[1];
                    tb -= rgbmeans[2];

                    // adjust contrast now !!!
                    tr = (int) (tr * getContrast());
                    tg = (int) (tg * getContrast());
                    tb = (int) (tb * getContrast());

                    // adjust brightness

//                    float brightness = getBrightness();
                    float brightness = getBrightness(x, y, width, height, col, row);
                    System.out.println(row + " " + " " + col + " " + brightness);
                    tr += (int) (rgbmeans[0] * brightness);
                    tg += (int) (rgbmeans[1] * brightness);
                    tb += (int) (rgbmeans[2] * brightness);

                    outPixels[index] = (ta << 24) | (clamp(tr) << 16) | (clamp(tg) << 8) | clamp(tb);
                } else {
                    outPixels[index] = inPixels[index];
                }
            }
        }
        dest.setRGB( 0, 0, imgWidth, imgHeight, outPixels, 0, imgWidth );
        return dest;
    }

    public static int clamp(int value) {
        return value > 255 ? 255 :(value < 0 ? 0 : value);
    }

    public float get_brightness() {
        return _brightness;
    }

    public static float getBrightness(int x, int y, int wight, int height, int currentX, int currentY) {
        int margin = 10;
        float distance = _brightness - brightness;

        float xBrightness = 0;

        int xDistance = currentX - x;
        if (xDistance > wight/2) {
            xDistance = wight - xDistance;
        }

        int yDistance = currentY - y;
        if (yDistance > height/2) {
            yDistance = height - yDistance;
        }

        if (xDistance > yDistance) {
            xDistance = yDistance;
        }

        if (xDistance > 5) {
            xBrightness = _brightness;
        } else {
            xBrightness = brightness + (distance/margin)*xDistance;
        }


        return xBrightness;
    }

    public static float getContrast() {
        return contrast;
    }

    public void setContrast(float contrast) {
        this.contrast = contrast;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

}  