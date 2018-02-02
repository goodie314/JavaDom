package test.javadom;


import javadom.JavaDom;
import javadom.http.HttpService;
import javadom.page.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JavaDomTest {

    public static void main(String[] args) throws Exception {
        String url = "http://www.google.com";
        System.out.println(HttpService.get(url));
//        JavaDom dom = new JavaDom();
        testSpeed(1000);
//        Document doc = JavaDom.getPage(url);
//        System.out.println(doc.getRoot().toJSON());
    }

    public static void testSpeed(int iterations) throws Exception {
        double start;
        double end;
        List<Double> times = new ArrayList<>();

        String html = HttpService.get("http://www.google.com").getResponseBody();
        for (int i = 0; i < iterations; i++) {
            System.out.println("Iteration: " + i);
            start = System.nanoTime();
            Document doc = JavaDom.parseHtml("http://www.google.com", html);
//            startTime = System.nanoTime();
//            doc.querySelector("a[href]");
            end = System.nanoTime();
            times.add(end - start);
        }
        double average = 0;
        times = times.stream()
                .sorted()
                .collect(Collectors.toList());
        for (Double time : times) {
            System.out.println("Time: " + time / 1e6 + "ms");
            average += time;
        }
        System.out.println("Average time: " + (average / times.size()) / 1e6 + "ms");
        System.out.println("Median time: " + times.get(times.size() / 2) / 1e6 + "ms");
        System.out.println("Min time: " + times.get(0) / 1e6 + "ms");
        System.out.println("Mode time: " + times.get(times.size() - 1) / 1e6 + "ms");
    }
}
