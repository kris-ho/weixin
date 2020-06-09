package com.hks.weixin.utils;

import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public class WxUtil {

    public static String sha1(String str) {

        MessageDigest md = null;
        try {
            //获取一个加密对象
            md = MessageDigest.getInstance("sha1");
            //加密
            byte[] digest = md.digest(str.getBytes());

            char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            StringBuilder sb = new StringBuilder();
            //处理加密结果
            for (byte b : digest) {
                sb.append(chars[(b >> 4) & 15]);
                sb.append(chars[b & 15]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向指定的地址发送一个post请求，带着data数据
     * @param url
     * @param data
     * @return
     */
    public static String post(String url, String data) {
        OutputStream os = null;
        InputStream is = null;

        try {
            URL urlObj = new URL(url);
            URLConnection connection = urlObj.openConnection();
            // 要发送数据出去，必须要设置为可发送数据状态
            connection.setDoOutput(true);
            // 获取输出流
            os = connection.getOutputStream();
            // 写出数据
            os.write(data.getBytes());
            // 获取输入流
            is = connection.getInputStream();
            byte[] b = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = is.read(b)) != -1) {
                sb.append(new String(b, 0, len));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 向指定的地址发送get请求
     * @param url
     */
    public static String get(String url) {
        try {
            URL urlObj = new URL(url);
            // 开连接
            URLConnection connection = urlObj.openConnection();
            InputStream is = connection.getInputStream();
            byte[] b = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = is.read(b)) != -1) {
                sb.append(new String(b, 0, len));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 将map型转为请求参数型
    public static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 调用图灵机器人
     * @param msg
     * @return
     * @throws IOException
     */
    public static String getAnswer(String msg, String APIKEY, String APIURL, String USERID) throws IOException {
        //实现方式1
        return getResultMes(tulinPost(APIURL, getReqMes(msg, APIKEY, USERID)));
        //实现方式2
//        String INFO = URLEncoder.encode(msg, "utf-8");
//        String getURL = "http://www.tuling123.com/openapi/api?key=" + APIKEY + "&info=" + INFO;
//        URL getUrl = new URL(getURL);
//        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
//        connection.connect();
//
//        // 取得输入流，并使用Reader读取
//        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
//        StringBuilder sb = new StringBuilder();
//        String len = "";
//        while ((len = reader.readLine()) != null) {
//            sb.append(len);
//        }
//        reader.close();
//        // 断开连接
//        connection.disconnect();
//        String[] ss = new String[10];
//        String s = sb.toString();
//        String answer;
//        ss = s.split(":");
//        answer = ss[ss.length - 1];
//        answer = answer.substring(1, answer.length() - 2);
//        return answer;
    }

    /**
     * 构建传输的正确的json格式的请求字符串
     * @param msg 输入内容
     * @return
     */
    private static String getReqMes(String msg, String APIKEY, String USERID) {
        // 请求json，里面包含reqType，perception，userInfo
        JSONObject reqJson = new JSONObject();
        // 输入类型:0-文本(默认)、1-图片、2-音频
        int reqType = 0;
        reqJson.put("reqType", reqType);

        // 输入信息,里面包含inputText，inputImage，selfInfo
        JSONObject perception = new JSONObject();
        // 输入的文本信息
        JSONObject inputText = new JSONObject();
        inputText.put("text", msg);
        perception.put("inputText", inputText);
//        // 输入的图片信息
//        JSONObject inputImage = new JSONObject();
//        inputImage.put("url","");
//        perception.put("inputImage",inputImage);
//        // 个人信息，里面包含location
//        JSONObject selfInfo = new JSONObject();
//        // 包含city，province，street
//        JSONObject location = new JSONObject();
//        location.put("city","");
//        location.put("province","");
//        location.put("street","");
//        selfInfo.put("location",location);
//        perception.put("selfInfo",selfInfo);
        // 用户信息
        JSONObject userInfo = new JSONObject();
        userInfo.put("apiKey", APIKEY);
        userInfo.put("userId", USERID);

        reqJson.put("perception", perception);
        reqJson.put("userInfo", userInfo);
        return reqJson.toString();
    }

    /**
     * 发送post请求到图灵服务器
     * @param url
     * @param reqMes
     * @return
     */
    private static String tulinPost(String url, String reqMes) {

//        String status = "";
        StringBuilder responseStr = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) conn;
            // 设置请求属性
            httpUrlConnection.setRequestProperty("Content-Type", "application/json");
            httpUrlConnection.setRequestProperty("x-adviewrtb-version", "2.1");
            httpUrlConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpUrlConnection.setRequestProperty("contentType", "utf-8");
            // 发送POST请求必须设置如下两行
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(httpUrlConnection.getOutputStream());
            // 发送请求参数
            out.write(reqMes);
            // flush输出流的缓冲
            out.flush();
            httpUrlConnection.connect();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
            responseStr = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                responseStr.append(line);
            }
//            status = new Integer(httpUrlConnection.getResponseCode()).toString();
//            System.out.println("status=============="+status);
//            System.out.println("response=============="+responseStr);
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return responseStr.toString();
    }

    /**
     * 从tulinPostStr返回的请求中取出结果
     * @param tulinPostStr
     * @return
     */
    public static String getResultMes(String tulinPostStr) {

        JSONObject thesultStr = JSONObject.fromObject(tulinPostStr);

        List<Object> results = (List<Object>) thesultStr.get("results");

        JSONObject resultObj = JSONObject.fromObject(results.get(0));

        JSONObject values = JSONObject.fromObject(resultObj.get("values"));

        return values.get("text").toString();
    }
}
