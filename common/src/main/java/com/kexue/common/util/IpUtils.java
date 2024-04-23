package com.kexue.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * IP工具类
 */
public class IpUtils {

//    public static final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//    
//    public static AtomicLong counter = new AtomicLong(0);
    
    /**
     * 获取访问者的ip地址
     * 注：要外网访问才能获取到外网地址，如果你在局域网甚至本机上访问，获得的是内网或者本机的ip
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            //X-Forwarded-For：Squid 服务代理
            String ipAddresses = request.getHeader("X-Forwarded-For");

            if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
                //Proxy-Client-IP：apache 服务代理
                ipAddresses = request.getHeader("Proxy-Client-IP");
            }

            if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
                //WL-Proxy-Client-IP：weblogic 服务代理
                ipAddresses = request.getHeader("WL-Proxy-Client-IP");
            }

            if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
                //HTTP_CLIENT_IP：有些代理服务器
                ipAddresses = request.getHeader("HTTP_CLIENT_IP");
            }

            if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
                //X-Real-IP：nginx服务代理
                ipAddresses = request.getHeader("X-Real-IP");
            }

            //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
            if (ipAddresses != null && ipAddresses.length() != 0) {
                ipAddress = ipAddresses.split(",")[0];
            }

            //还是不能获取到，最后再通过request.getRemoteAddr();获取
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
                ipAddress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

//    /**
//     * 调用太平洋网络IP地址查询Web接口（http://whois.pconline.com.cn/），返回ip、地理位置
//     */
//    public static IpInfo getIpVo(String ip) {
////        //查本机
////        String url = "http://whois.pconline.com.cn/ipJson.jsp?json=true";
//        String result = getIpInfo(ip);
//        System.out.println("+++++++++++++"+result);
//        return JSON.parseObject(result, IpInfo.class);
//    }

//    /**
//     * 查询ip具体信息
//     *
//     * @param ip
//     * @return
//     */
//    private static String getIpInfo(String ip) {
//        long count = counter.getAndAdd(1);
//        String result = "";
//        if (count % 2 == 0) {
//            HttpGet get = new HttpGet("http://whois.pconline.com.cn/ipJson.jsp?json=true&ip=" + ip);
//            try (CloseableHttpResponse response = httpClient.execute(get)) {
//                HttpEntity entity = response.getEntity();
//                result = EntityUtils.toString(entity);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            
//            return result.trim();
//        } else if (count % 2 == 1) {
//            HttpPost post = new HttpPost("https://tool.lu/ip/ajax.html");
//            post.setHeader("Referer", "https://tool.lu/ip/index.html");
//            post.setHeader("Origin", "https://tool.lu");
//            HashMap<String, Object> param = new HashMap<>();
//            param.put("ip","111.248.154.121");
//            String json = JSON.toJSONString(param);
//            try {
//                post.setEntity(new StringEntity(json));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            try (CloseableHttpResponse response = httpClient.execute(post)) {
//                result = EntityUtils.toString(response.getEntity());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return result;
//        }
//        return "";
//    }
//
//    /**
//     * 直接根据访问者的Request，返回ip、地理位置
//     */
//    public static IpInfo getIpVoByRequest(HttpServletRequest request) {
//        return IpUtils.getIpVo(IpUtils.getIpAddr(request));
//    }

    /*
        终极大法：java获取不了，就用js来获取
        <!-- js获取客户ip -->
        <script src="http://whois.pconline.com.cn/ipJson.jsp"></script>
     */

}
