package org.jarvis.http;

public class Test {
    static HttpSite httpSite = HttpSiteFactory.create();
    static String host1 = "http://localhost:8817";
    static String host2 = "http://10.24.3.194:8990";

    public static void main(String[] args) throws Exception {
        test1();
        listTest();
        orderStatusNumTest();
        orderDetailTest();
        findOrderModelTest();
        queryOrderStatus();
        orderPropsTest();
        findOrderModelWithProgramTest();
        findOrderInfoTest();
        findOrderInfoByTradeNoTest();
        queryOrderTest();
        findOrderBaoInfoTest();
        saveOrderEsTest();
    }

    public static void test1() {
        String url = "/order/getConfig";
        FormEntity formEntity = new FormEntity();
        formEntity.addParam("orderType", "206430");
        String data1 = httpSite.post(host1 + url, formEntity, String.class).getData();
        String data2 = httpSite.post(host2 + url, formEntity, String.class).getData();
        System.out.println(data1.equals(data2));
    }


    public static void listTest() throws Exception {
        String url = "/order/list";
        FormEntity formEntity = new FormEntity();
        formEntity.addParam("userId", "567496019");
        formEntity.addParam("tabStatus", "5");
        formEntity.addParam("appKey", "MMMuPs");
        formEntity.addParam("pageStart", "0");
        formEntity.addParam("pageSize", "10");
        String data1 = httpSite.post(host1 + url, formEntity, String.class).getData();
        String data2 = httpSite.post(host2 + url, formEntity, String.class).getData();
        System.out.println(data1.equals(data2));
    }


    public static void orderStatusNumTest() throws Exception {

        String url = "/order/orderStatusNum";
        FormEntity formEntity = new FormEntity();
        formEntity.addParam("userId", "984086082");
        formEntity.addParam("appKey", "My3jZg");
        String data1 = httpSite.post(host1 + url, formEntity, String.class).getData();
        String data2 = httpSite.post(host2 + url, formEntity, String.class).getData();
        System.out.println(data1.equals(data2));


    }


    public static void orderDetailTest() throws Exception {

        String url = "/order/orderDetail";
        FormEntity formEntity = new FormEntity();
        formEntity.addParam("orderId", "1681280747252");
        formEntity.addParam("appKey", "My3jZg");
        String data1 = httpSite.post(host1 + url, formEntity, String.class).getData();
        String data2 = httpSite.post(host2 + url, formEntity, String.class).getData();
        System.out.println(data1.equals(data2));
    }


    public static void findOrderModelTest() throws Exception {
        String url = "/order/findOrderModel";
        FormEntity formEntity = new FormEntity();
        formEntity.addParam("userId", "3271000937252");
        formEntity.addParam("orderId", "1681280747252");
        String data1 = httpSite.post(host1 + url, formEntity, String.class).getData();
        String data2 = httpSite.post(host2 + url, formEntity, String.class).getData();
        System.out.println(data1.equals(data2));
    }

    /**
     * 查询订单状态
     *
     * @throws Exception
     */

    public static void queryOrderStatus() throws Exception {
        String url = "/order/queryOrderStatus";
        FormEntity formEntity = new FormEntity();
        formEntity.addParam("userId", "984086082");
        formEntity.addParam("orderId", "1686090466082");
        formEntity.addParam("sync", "1");
        String data1 = httpSite.post(host1 + url, formEntity, String.class).getData();
        String data2 = httpSite.post(host2 + url, formEntity, String.class).getData();
        System.out.println(data1.equals(data2));
    }


    public static void orderPropsTest() throws Exception {
        String url = "/order/orderProps";
        FormEntity formEntity = new FormEntity();
        formEntity.addParam("orderId", "1692065251566");
        String data1 = httpSite.post(host1 + url, formEntity, String.class).getData();
        String data2 = httpSite.post(host2 + url, formEntity, String.class).getData();
        System.out.println(data1.equals(data2));
    }


    public static void findOrderModelWithProgramTest() throws Exception {
        String url = "/order/findOrderModelWithProgram";
        FormEntity formEntity = new FormEntity();
        formEntity.addParam("orderId", "1689617737995");
        formEntity.addParam("userId", "3271000917995");
        formEntity.addParam("sign", "2399462290dff65346302d3d57946a23");
        String data1 = httpSite.post(host1 + url, formEntity, String.class).getData();
        String data2 = httpSite.post(host2 + url, formEntity, String.class).getData();
        System.out.println(data1.equals(data2));

    }


    public static void findOrderInfoTest() throws Exception {
        String url = "/order/findOrderInfo";
        FormEntity formEntity = new FormEntity();
        formEntity.addParam("appKey", "MMMzaA");
        formEntity.addParam("dealId", "3724997469");
        formEntity.addParam("type", "2");
        formEntity.addParam("showRefundDetail", "true");
        formEntity.addParam("tradeNo", "7710110003849743");
        formEntity.addParam("rsaSign", "f2KyhOPDb1qWjRYRipnolwbOrkbU1Hen25b9IKL2Au+94CNu+HiyzkUzR1i1ynWvsld8b9Z5MqYlqBINQpwaQA==");
        String data1 = httpSite.post(host1 + url, formEntity, String.class).getData();
        String data2 = httpSite.post(host2 + url, formEntity, String.class).getData();
        System.out.println(data1.equals(data2));

    }


    public static void findOrderInfoByTradeNoTest() throws Exception {
        String url = "/order/findOrderInfoByTradeNo";
        FormEntity formEntity = new FormEntity();
        formEntity.addParam("tradeNo", "7701200004446964");
        String data1 = httpSite.post(host1 + url, formEntity, String.class).getData();
        String data2 = httpSite.post(host2 + url, formEntity, String.class).getData();
        System.out.println(data1.equals(data2));

    }


    public static void queryOrderTest() throws Exception {
        String url = "/order/queryOrder";
        FormEntity formEntity = new FormEntity();
        formEntity.addParam("orderId", "40005283445735");
        formEntity.addParam("userId", "1881195735");
        String data1 = httpSite.post(host1 + url, formEntity, String.class).getData();
        String data2 = httpSite.post(host2 + url, formEntity, String.class).getData();
        System.out.println(data1.equals(data2));
    }


    public static void findOrderBaoInfoTest() throws Exception {
        String url = "/order/findOrderBaoInfo";
        FormEntity formEntity = new FormEntity();
        formEntity.addParam("orderId", "40005283445735");
        formEntity.addParam("ak", "biaaSZRW1idLvUJGeOCbvqSUnSmZ21fG");
        formEntity.addParam("timestamp", "1647440008");
        formEntity.addParam("sign", "62ae9857d5107554f4ea26d632eeef75");
        String data1 = httpSite.post(host1 + url, formEntity, String.class).getData();
        String data2 = httpSite.post(host2 + url, formEntity, String.class).getData();
        System.out.println(data1.equals(data2));

    }


    public static void saveOrderEsTest() throws Exception {

        String url = "/order/syncOrderEs";
        FormEntity formEntity = new FormEntity();
        formEntity.addParam("orderId", "1750330089096");
        String data1 = httpSite.post(host1 + url, formEntity, String.class).getData();
        String data2 = httpSite.post(host2 + url, formEntity, String.class).getData();
        System.out.println(data1.equals(data2));
    }

}
