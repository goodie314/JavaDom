package test.javadom;


import javadom.JavaDom;
import javadom.http.HttpService;
import javadom.page.Document;

public class JavaDomTest {

    public static void main(String[] args) throws Exception {
        String url = "http://www.google.com";
        System.out.println(HttpService.get(url));
        JavaDom dom = new JavaDom();
        Document doc = dom.getPage(url);
//        System.out.println(doc.querySelector("script"));
    }
}
