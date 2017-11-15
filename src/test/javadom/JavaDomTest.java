package test.javadom;

import javadom.HtmlDocument;
import javadom.HtmlElement;
import javadom.JavaDom;

import java.io.IOException;

public class JavaDomTest {

    public static void main(String[] args) throws IOException {
        String url = "http://google.com";

        JavaDom javaDom = new JavaDom();
        HtmlDocument page = javaDom.getPage(url);
        HtmlElement root = page.getRoot();
    }
}
