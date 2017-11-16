package test.javadom;

import javadom.Document;
import javadom.HtmlElement;
import javadom.JavaDom;

import java.io.IOException;
import java.util.List;

public class JavaDomTest {

    public static void main(String[] args) throws IOException {
        String url = "http://google.com";

        JavaDom javaDom = new JavaDom();
        Document page = javaDom.getPage(url);
        HtmlElement root = page.getRoot();

        List<HtmlElement> elems = page.find(".gbmt");
        elems.forEach((e) -> System.out.println(e.getTag()));
    }
}
