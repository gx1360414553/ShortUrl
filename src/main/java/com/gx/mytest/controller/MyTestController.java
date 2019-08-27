package com.gx.mytest.controller;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.gx.qrcode.QRCodeUtil;
import com.gx.qrcode.ZXingBackGroundUtils;
import com.gx.shorturl.ShortNetAddressUtil;
import com.gx.shorturl.ShortUrl;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年08月26日 09:09:00
 */
@Controller
@RequestMapping("/qrcode")
public class MyTestController {

    @RequestMapping(value = "/generateqrcode", method = RequestMethod.GET)
    @ResponseBody
    public void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) {
        String longUrl;
        try {
            longUrl = "https://www.jianshu.com/u/c0aa31157ba5";
            // 转换成短url
            String shortUrl = ShortNetAddressUtil.getShortURL(longUrl);
//            String shortUrl = ShortUrl.shortUrl(longUrl);
            // 生成二维码
            BitMatrix qRcodeImg = QRCodeUtil.generateQRCodeStream(shortUrl, response);
            // 将二维码输出到页面中
            MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/logqrcode", method = RequestMethod.GET)
    @ResponseBody
    public void logqrcode(HttpServletResponse response) {
        try {
        String serverpath= ResourceUtils.getURL("classpath:static").getPath().replace("%20"," ").replace('/', '\\');
        String path=serverpath.substring(1);//从路径字符串中取出工程路径

        //头像logo
        File logoFile = new File(path + "\\th.jpg");
        //生成二维码地址
//        String qrCodePath = ClassUtils.getDefaultClassLoader().getResource("/").getPath();
//        File QrCodeFile = new File(qrCodePath + "\\test.jpg");
        String url = "https://www.baidu.com/";

            ZXingBackGroundUtils.drawLogoQRCode(logoFile, response.getOutputStream(), url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/hello/{abc}")
    public String helloHtml(@PathVariable("abc") String abc) {
        return abc;
    }

}
