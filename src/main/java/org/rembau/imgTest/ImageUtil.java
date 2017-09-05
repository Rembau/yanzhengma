package org.rembau.imgTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Date;

/**
 * <p>Title: ImageUtil </p>
 * <p>Description: </p>
 * <p>Email: icerainsoft@hotmail.com </p>
 * @author Ares
 * @date 2014年10月28日 上午10:24:26
 */
public class ImageUtil {

    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    private final static String DEFAULT_THUMB_PREVFIX = "thumb_";
    private final static String DEFAULT_CUT_PREVFIX = "cut_";
    private final static Boolean DEFAULT_FORCE = false;


    /**
     * <p>Title: cutImage</p>
     * <p>Description:  根据原图与裁切size截取局部图片</p>
     * @param srcImg    源图片
     * @param output    图片输出流
     * @param rect        需要截取部分的坐标和大小
     */
    public static void cutImage(File srcImg, OutputStream output, java.awt.Rectangle rect){
        if(srcImg.exists()){
            java.io.FileInputStream fis = null;
            ImageInputStream iis = null;
            try {
                fis = new FileInputStream(srcImg);
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames()).replace("]", ",");
                String suffix = null;
                // 获取图片后缀
                if(srcImg.getName().contains(".")) {
                    suffix = srcImg.getName().substring(srcImg.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀全部小写，然后判断后缀是否合法
                if(suffix == null || !types.toLowerCase().contains(suffix.toLowerCase() + ",")){
                    logger.error("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
                    return ;
                }
                // 将FileInputStream 转换为ImageInputStream
                iis = ImageIO.createImageInputStream(fis);
                // 根据图片类型获取该种类型的ImageReader
                ImageReader reader = ImageIO.getImageReadersBySuffix(suffix).next();
                reader.setInput(iis,true);
                ImageReadParam param = reader.getDefaultReadParam();
                param.setSourceRegion(rect);
                BufferedImage bi = reader.read(0, param);
                ImageIO.write(bi, suffix, output);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(fis != null) fis.close();
                    if(iis != null) iis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            logger.warn("the src image is not exist.");
        }
    }

    public static void cutImage(File srcImg, OutputStream output, int x, int y, int width, int height){
        cutImage(srcImg, output, new java.awt.Rectangle(x, y, width, height));
    }

    public static void cutImage(File srcImg, String destImgPath, java.awt.Rectangle rect){
        File destImg = new File(destImgPath);
        if(destImg.exists()){
            String p = destImg.getPath();
            try {
                if(!destImg.isDirectory()) p = destImg.getParent();
                if(!p.endsWith(File.separator)) p = p + File.separator;
                String name = p + DEFAULT_CUT_PREVFIX + srcImg.getName();
                logger.info("目标文件位置：{}", name);
                cutImage(srcImg, new java.io.FileOutputStream(name), rect);
            } catch (FileNotFoundException e) {
                logger.warn("the dest image is not exist.");
            }
        }else logger.warn("the dest image folder is not exist.");
    }

    public static void cutImage(File srcImg, String destImg, int x, int y, int width, int height){
        cutImage(srcImg, destImg, new java.awt.Rectangle(x, y, width, height));
    }

    public static void cutImage(String srcImg, String destImg, int x, int y, int width, int height){
        cutImage(new File(srcImg), destImg, new java.awt.Rectangle(x, y, width, height));
    }
    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 根据图片路径生成缩略图 </p>
     * @param srcImg    原图片路径
     * @param w            缩略图宽
     * @param h            缩略图高
     * @param prevfix    生成缩略图的前缀
     * @param force        是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
     */
    public void thumbnailImage(File srcImg, OutputStream output, int w, int h, String prevfix, boolean force){
        if(srcImg.exists()){
            try {
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames()).replace("]", ",");
                String suffix = null;
                // 获取图片后缀
                if(srcImg.getName().contains(".")) {
                    suffix = srcImg.getName().substring(srcImg.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀全部小写，然后判断后缀是否合法
                if(suffix == null || !types.toLowerCase().contains(suffix.toLowerCase() + ",")){
                    logger.error("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
                    return ;
                }
                logger.debug("target image's size, width:{}, height:{}.",w,h);
                Image img = ImageIO.read(srcImg);
                // 根据原图与要求的缩略图比例，找到最合适的缩略图比例
                if(!force){
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    if((width*1.0)/w < (height*1.0)/h){
                        if(width > w){
                            h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w/(width*1.0)));
                            logger.debug("change image's height, width:{}, height:{}.",w,h);
                        }
                    } else {
                        if(height > h){
                            w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h/(height*1.0)));
                            logger.debug("change image's width, width:{}, height:{}.",w,h);
                        }
                    }
                }
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
                g.dispose();
                // 将图片保存在原目录并加上前缀
                ImageIO.write(bi, suffix, output);
                output.close();
            } catch (IOException e) {
                logger.error("generate thumbnail image failed.",e);
            }
        }else{
            logger.warn("the src image is not exist.");
        }
    }
    public void thumbnailImage(File srcImg, int w, int h, String prevfix, boolean force){
        String p = srcImg.getAbsolutePath();
        try {
            if(!srcImg.isDirectory()) p = srcImg.getParent();
            if(!p.endsWith(File.separator)) p = p + File.separator;
            thumbnailImage(srcImg, new java.io.FileOutputStream(p + prevfix +srcImg.getName()), w, h, prevfix, force);
        } catch (FileNotFoundException e) {
            logger.error("the dest image is not exist.",e);
        }
    }

    public void thumbnailImage(String imagePath, int w, int h, String prevfix, boolean force){
        File srcImg = new File(imagePath);
        thumbnailImage(srcImg, w, h, prevfix, force);
    }

    public void thumbnailImage(String imagePath, int w, int h, boolean force){
        thumbnailImage(imagePath, w, h, DEFAULT_THUMB_PREVFIX, DEFAULT_FORCE);
    }

    public void thumbnailImage(String imagePath, int w, int h){
        thumbnailImage(imagePath, w, h, DEFAULT_FORCE);
    }

    /**
     * 给图片添加水印
     * @param iconPath 水印图片路径
     * @param srcImgPath 源图片路径
     * @param targerPath 目标图片路径
     */
    public static void markImageByIcon(String iconPath, String srcImgPath,
                                       String targerPath) {
        markImageByIcon(iconPath, srcImgPath, targerPath, null);
    }

    /**
     * 给图片添加水印、可设置水印图片旋转角度
     * @param iconPath 水印图片路径
     * @param srcImgPath 源图片路径
     * @param targerPath 目标图片路径
     * @param degree 水印图片旋转角度
     */
    public static void markImageByIcon(String iconPath, String srcImgPath,
                                       String targerPath, Integer degree) {
        OutputStream os = null;
        try {
            Image srcImg = ImageIO.read(new File(srcImgPath));

            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // 得到画笔对象
            // Graphics g= buffImg.getGraphics();
            Graphics2D g = buffImg.createGraphics();

            // 设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg
                    .getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);

            if (null != degree) {
                // 设置水印旋转
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2, (double) buffImg
                                .getHeight() / 2);
            }

            // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度
            ImageIcon imgIcon = new ImageIcon(iconPath);

            // 得到Image对象。
            Image img = imgIcon.getImage();

            float alpha = 0.5f; // 透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));

            // 表示水印图片的位置
            g.drawImage(img, 150, 300, null);

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

            g.dispose();

            os = new FileOutputStream(targerPath);

            // 生成图片
            ImageIO.write(buffImg, "JPG", os);

            System.out.println("图片完成添加Icon印章。。。。。。");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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


    public static void main(String[] args) {
//        new ImageUtil().thumbnailImage("imgs/Tulips.jpg", 150, 100);
        long time = new Date().getTime();
        int x = 0;
        int y = 0;
        int width = 40;
        int height = 40;
        String srcImg = "C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\fuzhu1.jpg";
        cutImage(srcImg,"C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma", x, y, width, height);
        makeImageColorToBlackWhite(srcImg, "C:\\Users\\youyue\\Desktop\\tem\\htmltest\\yanzhengma\\", x, y, width, height);
        System.out.println(new Date().getTime() - time + "ms");
    }
}