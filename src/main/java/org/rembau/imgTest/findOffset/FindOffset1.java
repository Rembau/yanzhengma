package org.rembau.imgTest.findOffset;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FindOffset1 {
    public static void main(String args[]) throws IOException {
        String srcPath = "C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\yzm1.jpg";
        String heiPath = "G:\\idea\\uproject\\UWeb\\src\\main\\webapp\\images\\yzm\\tem\\151_51D696E0B97F357C6B90A039D39FA11E_hei_yzm1.jpg";
        BufferedImage different = getDifferent(srcPath, heiPath);
        BufferedImage binaryImage = binaryImage(different);
        findOffset(binaryImage);
    }

    private static void findOffset(BufferedImage fileImg) throws IOException {
        long start = System.currentTimeMillis();
        if (fileImg == null) {
            File file = new File("C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\yzm1_findOffset1_b.jpg");
            fileImg = ImageIO.read(file);
        }

        int[] pixels = new int[fileImg.getWidth() * fileImg.getHeight()];
        fileImg.getRGB(0, 0, fileImg.getWidth(), fileImg.getHeight(), pixels, 0, fileImg.getWidth());

        List<Integer> list1530 = new LinkedList<>();
        List<Integer> list1550 = new LinkedList<>();
        List<Integer> list1570 = new LinkedList<>();
        List<Integer> list1590 = new LinkedList<>();
        for (int row = 0;row < fileImg.getHeight() - 40;row ++) {
            for (int col = 0; col < fileImg.getWidth() - 40; col++) {

//                int index = row * fileImg.getWidth() + col;
//                int srcA = (pixels[index] >> 24) & 0xff;
//                int srcR = (pixels[index] >> 16) & 0xff;
//                int srcG = (pixels[index] >> 8) & 0xff;
//                int srcB = pixels[index] & 0xff;

//                System.out.println(fileImg.getRGB(col, row) + " " + srcA + " " + srcR + " " + srcG + " " + srcB);
//                System.out.println(pixels[index]);
                int num = getWhiteNum(row, col, pixels, fileImg.getWidth(), fileImg);
//                1550 - 1570 - 1590
                if (num > 1530) {
                    list1530.add(col);
                }
                if (num > 1550) {
                    list1550.add(col);
                }
                if (num > 1570) {
                    list1570.add(col);
                }
                if (num > 1590) {
                    list1590.add(col);
                }
            }
        }

        System.out.println("1530:" + list1530 + "\n1550:" + list1550 + " \n1570:" + list1570 + "\n1590:" + list1590);

        System.out.println("耗时：" + (System.currentTimeMillis() - start));
    }

    public static BufferedImage binaryImage(BufferedImage image) throws IOException {

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);//重点，技巧在这个参数BufferedImage.TYPE_BYTE_BINARY
        for(int i= 0 ; i < width ; i++){
            for(int j = 0 ; j < height; j++){
                int rgb = image.getRGB(i, j);
                grayImage.setRGB(i, j, rgb);
            }
        }

        File newFile = new File("C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\findOffset1\\binaryImage.jpg");
        ImageIO.write(grayImage, "jpg", newFile);
        return grayImage;
    }

    private static int getWhiteNum(int row, int col, int[] pixels, int width, BufferedImage fileImg) {
        int num = 0;
        for (int i = row;i < row + 40;i++) {
            for (int j = col;j < col + 40;j++) {
                int index = i * width + j;
                int srcA = (pixels[index] >> 24) & 0xff;
                int srcR = (pixels[index] >> 16) & 0xff;
                int srcG = (pixels[index] >> 8) & 0xff;
                int srcB = pixels[index] & 0xff;

                if (srcR > 240 && srcG > 240 && srcB > 240) {
                    num ++;
                }
            }
        }
        return num;
    }

    private static BufferedImage getDifferent(String srcPath, String heiPath) throws IOException {
        if (srcPath == null) {
            srcPath = "C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\yzm2.jpg";
        }
        File fileSrc = new File(srcPath);
        if (heiPath == null) {
            heiPath = "G:\\idea\\uproject\\UWeb\\src\\main\\webapp\\images\\yzm\\tem\\216_B17040868B73F78F5EE6F2869567CAE7_hei_yzm2.jpg";
        }
        File fileHei = new File(heiPath);

        BufferedImage fileSrcImg = ImageIO.read(fileSrc);
        BufferedImage fileSrcHei = ImageIO.read(fileHei);
        BufferedImage dest = new BufferedImage(fileSrcImg.getWidth(), fileSrcImg.getHeight(),
                BufferedImage.TYPE_INT_RGB);


        for (int row = 0;row < fileSrcImg.getHeight();row ++) {
            for (int col = 0;col < fileSrcImg.getWidth();col ++) {
                int heiA = (fileSrcHei.getRGB(col, row) >> 24) & 0xff;
                int heiR = (fileSrcHei.getRGB(col, row) >> 16) & 0xff;
                int heiG = (fileSrcHei.getRGB(col, row) >> 8) & 0xff;
                int heiB = fileSrcHei.getRGB(col, row) & 0xff;

                int srcA = (fileSrcImg.getRGB(col, row) >> 24) & 0xff;
                int srcR = (fileSrcImg.getRGB(col, row) >> 16) & 0xff;
                int srcG = (fileSrcImg.getRGB(col, row) >> 8) & 0xff;
                int srcB = fileSrcImg.getRGB(col, row) & 0xff;

                int destR = heiR - srcR;
                int destG = heiG - srcG;
                int destB = heiB - srcB;

                dest.setRGB(col, row, (srcA << 24) | (destR << 16) | (destG << 8) | destB);

//                System.out.println("hei: " + heiA + " " + heiR + " " + heiG + " " + heiB);
//                System.out.println("src: " + srcA + " " + srcR + " " + srcG + " " + srcB);


            }
        }

        File findOffset1 = new File("C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\findOffset1\\getDifferent.jpg");
        ImageIO.write(dest, "jpg", findOffset1);
        return dest;
    }
}
