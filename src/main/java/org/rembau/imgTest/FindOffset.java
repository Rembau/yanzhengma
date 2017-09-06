package org.rembau.imgTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class FindOffset {
    public static void main(String args[]) {
//        File file = new File("C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\yzm2_b.jpg");
        File file = new File("G:\\idea\\uproject\\UWeb\\src\\main\\webapp\\images\\yzm\\tem\\06CC044FA3A83ED8C82CEDD679667065_hei_87yzm2.jpg");
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
            int width = image.getWidth();
            int height = image.getHeight();
            BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
            for(int i= 0 ; i < width ; i++){
                for(int j = 0 ; j < height; j++){
                    int rgb = image.getRGB(i, j);
                    grayImage.setRGB(i, j, rgb);
                }
            }

            HashSet<Integer> set = new HashSet<Integer>();

            for (int row = 0;row < grayImage.getHeight();row++) {
                for (int col = 0;col < grayImage.getWidth();col++) {    //抛弃0
//                    System.out.println(image.getRGB(col, row));
                    if (row == 40 && col == 40) {
                        System.out.println(col);
                    }
                    if (judge(grayImage, col, row)) {
                        System.out.println("====================" + col + " " + row);
                        set.add(col);
                    }
                }
            }

            System.out.println(set);

            LinkedList<Integer> list = new LinkedList<>(set);
            Collections.sort(list);

            System.out.println(list);

            LinkedList<Integer> newList = new LinkedList<>();

            newList.add(list.get(0));
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i) - list.get(i-1) != 1) {
                    newList.add(list.get(i));
                }
            }

            newList.remove(Integer.valueOf(0));
            System.out.println("new:" + newList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean judge(BufferedImage image, int x, int y) {

        int yNum = 0;
        int xNum = 0;
        for (int i = 0;i < 40;i++) {

            if (y + i >= image.getHeight()) {
                break;
            }

//            System.out.println("x:"+x);
            if (x - 1 > 0) {
                int rgb = image.getRGB(x - 1, y + i);
                if (rgb == -16777216) {
                    xNum ++;
                }

            }

            if (image.getRGB(x, y + i) == -16777216) {
                yNum++;
            }
        }

        return yNum > 25 && xNum <10;
    }

    private static boolean isBlack(int rgb) {
        return rgb < -1;
    }
}
