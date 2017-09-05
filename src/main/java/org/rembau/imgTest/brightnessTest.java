package org.rembau.imgTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class brightnessTest {

    ShortLookupTable LUT;

    public void brightImage(int offset){
        short[] brighten = new short[256]; //查找表的数据数组
        short pixelValue;
        for (int i=0;i<256;i++){
            pixelValue = (short)(i+offset);//加亮源像素10 个单位值
            if (pixelValue>255) //如果源像素加亮后的值超过255，则设为255
                pixelValue = 255;
            else if (pixelValue <0) //如果源像素加亮后的值低于0，则设为0
                pixelValue =0;
            brighten[i] = pixelValue; //赋值
        }
        LUT = new ShortLookupTable(0,brighten); //创建新的查找表，该表加亮源图像
    }

    public void darkenLUT(int offset){
        short[] darken = new short[256]; //查找表中的数据数组
        short pixelValue;
        for (int i=0;i<256;i++){
            pixelValue = (short)(i-offset); //把源像素变暗10 个单位值
            if (pixelValue>255) //如果处理后的像素值大于255，则设为255
                pixelValue = 255;
            else if (pixelValue <0) //如果处理后的像素值小于0，则设为0
                pixelValue =0;
            darken[i] = pixelValue; //赋值
        }
        LUT = new ShortLookupTable(0,darken); //创建新的查找表，该表变暗源图像
    }

    public void loadImage(){

    }

    public void applyFilter(){
        try {
            BufferedImage source = ImageIO.read(new File("C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\yzm1.jpg"));

            BufferedImage bimage = new BufferedImage(source.getWidth(), source.getHeight(),
                    BufferedImage.TYPE_INT_RGB);

            brightImage(50);
            LookupOp lop = new LookupOp(LUT,null); //根据查找表，创建查找过滤器
            lop.filter(source, bimage);//过滤图像

            // 将图片保存在原目录并加上前缀
            FileOutputStream output = new FileOutputStream(new File("C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\yzm1_1.jpg"));
            ImageIO.write(bimage, "jpg", output);

            output.flush();
            output.close();

            darkenLUT(50);
            lop = new LookupOp(LUT,null); //根据查找表，创建查找过滤器
            lop.filter(source, bimage);//过滤图像

            // 将图片保存在原目录并加上前缀
            output = new FileOutputStream(new File("C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\yzm1_2.jpg"));
            ImageIO.write(bimage, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[][] getRGB(String file) {
        BufferedImage bufImg = null;
        try {
            bufImg = ImageIO.read(new File(file));
        } catch (IOException e) {
            e.printStackTrace();
            return new int[0][0];
        }
        int height = bufImg.getHeight();
        int width = bufImg.getWidth();
        int[][] result = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                result[i][j] = bufImg.getRGB(i, j) & 0xFFFFFF;
            }
        }
        return result;
    }

    public static void main(String args[]) {
//        new brightnessTest().applyFilter();

        int[][] rgb = getRGB("C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\yzm1.jpg");
        int[][] rgb1 = getRGB("C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\yzm1_1.jpg");

        for (int i = 0;i < rgb.length;i++) {
            for (int j = 0;j < rgb[0].length;j++) {
                System.out.println(rgb[i][j] + "            " + rgb1[i][j]);
                System.out.println(((rgb[i][j] & 0xff0000) >> 16) + "            " + ((rgb1[i][j] & 0xff0000) >> 16));
                System.out.println(((rgb[i][j] & 0xff00) >> 8) + "            " + ((rgb1[i][j] & 0xff00) >> 8));
                System.out.println((rgb[i][j] & 0xff) + "            " + (rgb1[i][j] & 0xff));
            }
        }
    }
}
