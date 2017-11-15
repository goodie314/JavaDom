package test.javadom;

import javadom.JavaDom;

import java.io.IOException;

public class JavaDomTest {

    public static void main(String[] args) throws IOException {
        String url = "http://google.com";

        JavaDom javaDom = new JavaDom();
        javaDom.getPage(url);
        System.out.println(javaDom.getHtml());
    }
}
