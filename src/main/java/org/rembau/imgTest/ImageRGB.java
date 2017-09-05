package org.rembau.imgTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;


public class ImageRGB {


    public static void main(String[] args) {
        long time = new Date().getTime();
        makeImageColorToBlackWhite("G:\\idea\\uproject\\UWeb\\src\\main\\webapp\\html\\yanzhengma\\fuzhu.jpg",
                "G:\\idea\\uproject\\UWeb\\src\\main\\webapp\\html\\yanzhengma\\fuzhu_hei.jpg", 20, 20, 40, 40);
        System.out.println(new Date().getTime() - time + "ms");
    }


    /**
     * 将彩色图片变为灰色的图片
     *
     * @param imagePath
     */
    public static void makeImageColorToBlackWhite(String imagePath, String targetPath, int x, int y, int width, int height) {
        int[][] result = getImageGRB(imagePath);
        int[] rgb = new int[3];
        BufferedImage bi = new BufferedImage(result.length, result[0].length, BufferedImage.TYPE_INT_RGB);
        System.out.println(result.length + " " + result[0].length);;
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                if (i >= x && i < x+width && j >= y && j < y+height) {
                    rgb[0] = (result[i][j] & 0xff0000) >> 16;
                    rgb[1] = (result[i][j] & 0xff00) >> 8;
                    rgb[2] = (result[i][j] & 0xff);
                    int color = (int) (rgb[0] * 0.3 + rgb[1] * 0.59 + rgb[2] * 0.11);
//color = color > 128 ? 255 : 0;
                    //bi.setRGB(i, j, (color << 16) | (color << 8) | color);
                } else {
                    bi.setRGB(i, j, result[i][j]);
                }
            }
        }
        try {
            File outputPath = new File(targetPath);
            if (!outputPath.isDirectory()) {
                return;
            }
            targetPath += "hei_" + new File(imagePath).getName();
            ImageIO.write(bi, "JPEG", new File(targetPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取图片的像素点
     *
     * @param filePath
     * @return
     */
    public static int[][] getImageGRB(String filePath) {
        File file = new File(filePath);
        int[][] result = null;
        if (!file.exists()) {
            System.out.println("图片不存在");
            return result;
        }
        try {
            BufferedImage bufImg = ImageIO.read(file);
            int height = bufImg.getHeight();
            int width = bufImg.getWidth();
            result = new int[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    result[i][j] = bufImg.getRGB(i, j) & 0xFFFFFF;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }


}