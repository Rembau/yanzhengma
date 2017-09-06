package org.rembau.imgTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GrayTest {
    public void binaryImage() throws IOException {
        File file = new File("C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\5E9FE032887E5DBD7D4362044DDFE30C_hei_162yzm1.jpg");
        BufferedImage image = ImageIO.read(file);

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_BINARY
        for(int i= 0 ; i < width ; i++){
            for(int j = 0 ; j < height; j++){
                int rgb = image.getRGB(i, j);
                grayImage.setRGB(i, j, rgb);
            }
        }

        File newFile = new File("C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\5E9FE032887E5DBD7D4362044DDFE30C_hei_162yzm1_b.jpg");
        ImageIO.write(grayImage, "jpg", newFile);
    }

    public void grayImage() throws IOException{
        File file = new File("C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\yzm2_1.jpg");
        BufferedImage image = ImageIO.read(file);

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_GRAY
        for(int i= 0 ; i < width ; i++){
            for(int j = 0 ; j < height; j++){
                int rgb = image.getRGB(i, j);
                grayImage.setRGB(i, j, rgb);
            }
        }

        File newFile = new File("C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\yzm2_g.jpg");
        ImageIO.write(grayImage, "jpg", newFile);
    }

    public static void main(String[] args) throws IOException {
        GrayTest demo = new GrayTest();
        demo.binaryImage();
        //demo.grayImage();
    }
}
