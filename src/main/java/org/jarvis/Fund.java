package org.jarvis;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jarvis.clock.TimeUtils;
import org.jarvis.http.HttpSite;
import org.jarvis.http.HttpSiteFactory;
import org.jarvis.json.JsonUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Fund {

    static HttpSite httpSite = HttpSiteFactory.custom()
            .chromeMode()
            .proxy(8234)
            .build();

    public static void main(String[] args) throws FileNotFoundException {
        Pattern pattern = Pattern.compile("基金规模</a>：([\\d\\\\.]+.?元)");
        String body = httpSite.get("http://fund.eastmoney.com/js/fundcode_search.js", String.class).getData();
        List<List<String>> fundList = JsonUtils.toObj(body.substring(9), new TypeReference<List<List<String>>>() {
        });

        List<List<String>> result = fundList.stream()
                .filter(fund -> fund.get(3).contains("债券型"))
                .collect(Collectors.toList());
        System.out.println(result.size());

        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream("/Users/hanfei08/IdeaProjects/jarvis/fund.csv"), StandardCharsets.UTF_8), true);
//         <td><a href="http://fundf10.eastmoney.com/gmbd_004366.html">基金规模</a>：2.99亿元（2021-12-31）</td>
        MutableInt mutableInt = new MutableInt(0);
        LinkedList<List<String>> lists = new LinkedList<>(result);

        while (!lists.isEmpty()) {

            mutableInt.add(1);
            TimeUtils.sleep(500, TimeUnit.MILLISECONDS);


            List<String> fund = lists.poll();
            String fundCode = fund.get(0);
            String fundName = fund.get(2);
            String fundType = fund.get(3);

            String html = httpSite.get(String.format("http://fund.eastmoney.com/%s.html", fundCode), String.class).getData();
            String money = getMoney(pattern, html);
            if (money == null) {
                lists.add(fund);
                continue;
            }

            StringWriter stringWriter = new StringWriter();
            stringWriter.append(fundName);
            stringWriter.append(",");
            stringWriter.append(fundType);
            stringWriter.append(",");
            stringWriter.append(money);
            stringWriter.append("\n");
            System.out.print(stringWriter);
            printWriter.append(stringWriter.toString());
            printWriter.flush();
        }
//        result.stream()
//                .forEach(fund -> {
//                    mutableInt.add(1);
//                    if (mutableInt.getValue() % 10 == 0) {
//                        TimeUtils.sleep(5);
//                    }
//                    String fundCode = fund.get(0);
//                    String fundName = fund.get(2);
//                    String fundType = fund.get(3);
//
//                    String html = httpSite.get(String.format("http://fund.eastmoney.com/%s.html", fundCode), String.class).getData();
//                    String money = getMoney(pattern, html);
//                    while (money == null) {
//                        TimeUtils.sleep(5);
//                        money = getMoney(pattern, html);
//                    }
//
//                    StringWriter stringWriter = new StringWriter();
//                    stringWriter.append(fundName);
//                    stringWriter.append(",");
//                    stringWriter.append(fundType);
//                    stringWriter.append(",");
//                    stringWriter.append(money);
//                    stringWriter.append("\n");
//                    System.out.print(stringWriter);
//                    printWriter.append(stringWriter.toString());
//                    printWriter.flush();
//                });
    }

    private static String getMoney(Pattern pattern, String html) {
        String money = null;
        try {
            Matcher matcher = pattern.matcher(html);
            matcher.find();
            money = matcher.group(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(html);
        }
        return money;
    }
}
