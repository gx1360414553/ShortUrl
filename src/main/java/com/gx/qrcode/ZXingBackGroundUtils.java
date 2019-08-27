package com.gx.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description 合成图片 ---- 生成二维码和嵌套背景图片
 * @createTime 2019年08月27日 09:43:00
 */
public class ZXingBackGroundUtils {

    private static final int QRCOLOR = 0xFF000000; // 默认是黑色
    private static final int BGWHITE = 0xFFFFFFFF; // 背景颜色

    private static final int WIDTH = 200; // 二维码宽
    private static final int HEIGHT = 200; // 二维码高

    // 用于设置QR二维码参数
    private static Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>() {
        private static final long serialVersionUID = 1L;

        {
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 设置QR二维码的纠错级别（H为最高级别）具体级别信息
            put(EncodeHintType.CHARACTER_SET, "utf-8");// 设置编码方式
            put(EncodeHintType.MARGIN, 0);
        }
    };


    // 生成带logo的二维码图片
    /***
     *@param logoFile  logo图地址
     * @param codeFile  二维码生成地址
     * @param qrUrl 扫描二维码方位地址
     * */
    public static void drawLogoQRCode(File logoFile, OutputStream os, String qrUrl) {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            BitMatrix bm = multiFormatWriter.encode(qrUrl, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
            BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

            // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
                }
            }

            int width = image.getWidth();
            int height = image.getHeight();
            if (Objects.nonNull(logoFile) && logoFile.exists()) {
                // 构建绘图对象
                Graphics2D g = image.createGraphics();
                // 读取Logo图片
                BufferedImage logo = ImageIO.read(logoFile);
                // 开始绘制logo图片
                g.drawImage(logo, width * 2 / 5, height * 2 / 5, width * 2 / 10, height * 2 / 10, null);
                g.dispose();
                logo.flush();
            }

            image.flush();

//            ImageIO.write(image, "png", codeFile); // TODO
            ImageIO.write(image, "png", os); // TODO
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws  Exception {
        //头像logo
        File logoFile = new File("E:\\workspace\\my_test\\src\\main\\resources\\static\\th.jpg");
        //生成二维码地址
        File QrCodeFile = new File("E:\\workspace\\my_test\\src\\main\\resources\\static\\test.jpg");
        String url = "https://www.baidu.com/";
//        drawLogoQRCode(logoFile, QrCodeFile, url);

//        mergeImage("E:\\workspace\\my_test\\src\\main\\resources\\static\\abc.jpg", "E:\\workspace\\my_test\\src\\main\\resources\\static\\test.jpg", "E:\\workspace\\my_test\\src\\main\\resources\\static\\test2.jpg","100", "40");
    }



    /***
     * 二维码嵌套背景图的方法
     *@param bigPath 背景图 - 可传网络地址
     *@param smallPath 二维码地址 - 可传网络地址
     *@param newFilePath 生成新图片的地址
     * @param  x 二维码x坐标
     *  @param  y 二维码y坐标
     * */
    public static void mergeImage(String bigPath, String smallPath, OutputStream os,String x, String y) throws IOException {

        try {
            BufferedImage small;
            BufferedImage big;
            if (bigPath.contains("http://") || bigPath.contains("https://")) {
                URL url = new URL(bigPath);
                big = ImageIO.read(url);
            } else {
                big = ImageIO.read(new File(bigPath));
            }


            if (smallPath.contains("http://") || smallPath.contains("https://")) {

                URL url = new URL(smallPath);
                small = ImageIO.read(url);
            } else {
                small = ImageIO.read(new File(smallPath));
            }

            Graphics2D g = big.createGraphics();

            float fx = Float.parseFloat(x);
            float fy = Float.parseFloat(y);
            int x_i = (int) fx;
            int y_i = (int) fy;
            g.drawImage(small, x_i, y_i, small.getWidth(), small.getHeight(), null);
            g.dispose();
            ImageIO.write(big, "png", os);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

