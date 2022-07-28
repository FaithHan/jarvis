package org.jarvis.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jarvis.codec.HexUtils;
import org.jarvis.io.StreamCopyUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class HttpUtilsTest {

    private static HttpSite httpSite;

    @BeforeAll
    static void beforeAll() {
        httpSite = HttpSiteFactory.custom().chromeMode().build();
    }

    @BeforeEach
    void startHttpServer() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8080);
                Socket accept = serverSocket.accept();
                InputStream inputStream = accept.getInputStream();
                StreamCopyUtils.copy(inputStream, System.out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Test
    void download_test() throws IOException {
        byte[] data = httpSite.get("https://img0.baidu.com/it/u=4006423645,1371782873&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=375", byte[].class).getData();
        System.out.println(HexUtils.toHexString(data));
        StreamCopyUtils.copy(data, new FileOutputStream("/Users/hanfei08/IdeaProjects/jarvis/a.JPEG"));
    }

    @Test
    void test() {
        HttpSite httpSite = HttpSiteFactory.custom()
                .timeout(10)
                .proxy("localhost", 6152)
                .chromeMode()
                .build();

        Response<String> response = httpSite.get("https://www.baidu.com/s?wd=ip", String.class);
        System.out.println(response.getHttpStatusCode());
        System.out.println(response.getHeader());
        System.out.println(response.getData());
    }

    @Test
    void get() {
        QueryParams queryParams = new QueryParams();
        queryParams.addParam("name", "faith");
        queryParams.addParam("name", "测试");
        Headers headers = new Headers();
        headers.addHeader("nihao", "hehe");
        headers.addHeader("nihao", "哈哈");
        headers.addHeader("nihao", "xixi");
        Response<String> s = httpSite.post("http://localhost:8080/index?beijing=baidu&beijing=百度", headers, queryParams, String.class);
        System.out.println(s.getData());
    }

    @Test
    void post() {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 22);
        Response<Map> response = httpSite.post("http://localhost:8080/post?beijing=baidu&beijing=百度", map, Map.class);
        System.out.println(response.getHttpStatusCode());
        System.out.println(response.getData());
    }

    @Test
    void post_multipart() throws URISyntaxException, IOException {
        MultipartEntity multipartEntity = MultipartEntity.create()
                .textBody("name", "世界")
                .textBody("name", "hello")
                .binaryBody("file", new File("/Users/hanfei08/Desktop/131628246419_.pic_thumb.jpg"));

        Request<MultipartEntity> request = Request.post("http://localhost:8080/file?beijing=baidu&beijing=百度")
                .header("head1", "hello")
                .header("head1", "world")
                .build(multipartEntity);

        Response<String> response = httpSite.execute(request, String.class);
        System.out.println(response.getHttpStatusCode());
        System.out.println(response.getData());
    }

    @Test
    void post_upload_file() {
        MultipartEntity multipartEntity = MultipartEntity.create()
                .textBody("name", "世界")
                .textBody("name", "hello")
                .binaryBody("file", new File("/Users/hanfei08/Desktop/131628246419_.pic_thumb.jpg"));

        Request<MultipartEntity> request = Request.post("http://localhost:8080/file?beijing=baidu&beijing=百度")
                .header("head1", "hello")
                .header("head1", "world")
                .build(multipartEntity);

        Headers headers = new Headers();
        headers.addHeader("head1", "hello");
        headers.addHeader("head1", "世界");
        String uri = "http://localhost:8080/file";
        Response<String> response = httpSite.post(uri, headers, multipartEntity, String.class);
        System.out.println(response.getHttpStatusCode());
        System.out.println(response.getData());
    }


    @Test
    void name() throws IOException {
        HttpPost httpGet = new HttpPost("http://localhost:8080/file");
        httpGet.addHeader("head2", "nihao, 世界");

        httpGet.addHeader("head1", "nihao");
        httpGet.addHeader("head1", "shijie");
        CloseableHttpResponse execute = HttpClientBuilder.create().build().execute(httpGet);
    }

    @Test
    void name2() throws IOException {
        Arrays.asList(
                "/order/getConfig",
                "/order/list",
                "/order/orderStatusNum",
                "/order/orderDetail",
                "/order/findOrderModel",
                "/order/queryOrderStatus",
                "/order/orderProps",
                "/order/findOrderModelWithProgram",
                "/order/findOrderInfo",
                "/order/findOrderInfoByTradeNo",
                "/order/queryOrder",
                "/order/findOrderBaoInfo",
                "/order/syncOrderEs"
        );
    }

    public static List<Integer> spiralOrder(int[][] matrix) {
        int top = 0;
        int buttom = matrix.length-1;
        int left = 0;
        int right = matrix[0].length - 1;
        int size = matrix.length * matrix[0].length;
        List<Integer> list = new ArrayList<>();
        while(list.size() != size) {
            for(int i = left; i <= right; i++){
                list.add(matrix[top][i]);
            }
            top++;
            for(int i = top; i<= buttom;i++){
                list.add(matrix[i][right]);
            }
            right--;
            for(int i = right; i>=left; i--){
                list.add(matrix[buttom][i]);
            }
            buttom--;
            for(int i = buttom; i>=top; i--){
                list.add(matrix[i][left]);
            }
            left++;
        }
        return list;
    }

    public static void main(String[] args) {
        int[][] ints =  new int[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12}};
        spiralOrder(ints);
    }
}