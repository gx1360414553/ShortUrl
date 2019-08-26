package com.gx.shorturl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年08月20日 09:56:00
 */
public class ShortUrlHelper {

    public static CloseableHttpClient httpClient;

    static {
        httpClient = HttpClients.createDefault();
    }


    /**
     * 将长链接转为短链接(调用的新浪的短网址API)
     *
     * @param url
     *            需要转换的长链接url
     * @return 返回转换后的短链接
     */
    public static String convertSinaShortUrl(String url) {
        try {
            // 调用新浪API
            HttpPost post = new HttpPost("http://api.t.sina.com.cn/short_url/shorten.json");
            List<NameValuePair> params = new LinkedList<NameValuePair>();
            // 必要的url长链接参数
            params.add(new BasicNameValuePair("url_long", url));
            // 必要的新浪key
            params.add(new BasicNameValuePair("source", "3271760578"));
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(post);
            // 得到调用新浪API后成功返回的json字符串
            // url_short : 短链接地址 type：类型 url_long：原始长链接地址
            String json = EntityUtils.toString(response.getEntity(), "utf-8");
            JSONArray jsonArray = JSONArray.parseArray(json);
            JSONObject object = (JSONObject) jsonArray.get(0);
            return object.getString("url_short");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 将长链接转为短链接(调用的http://h5ip.cn/网址API)
     *
     * @param url
     *            需要转换的长链接url
     * @return 返回转换后的短链接
     */
    public static String convertBaiDuShortUrl(String url) {
        try {
            // 调用百度API
            HttpPost post = new HttpPost("http://h5ip.cn/Index/addAddress");
            List<NameValuePair> params = new LinkedList<>();
            // 必要的url长链接参数
            params.add(new BasicNameValuePair("source", url));
            params.add(new BasicNameValuePair("https", "1"));
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(post);
            // 得到调用百度API后成功返回的json字符串
            // tinyurl : 短链接地址 status：0 表示转换成功 非0表示转换失败 longurl：原始长链接地址 err_msg:错误信息
            String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");
            JSONObject object = JSON.parseObject(jsonStr);
            return object.getString("short_url");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 测试
     * @param args
     */
    public static void main(String []args){
        String tinyurl = convertBaiDuShortUrl("http://news.sina.com.cn/gov/xlxw/2018-09-05/doc-ihiixyeu3395739.shtml");
        System.out.println(tinyurl);
    }
}
