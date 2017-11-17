package javadom;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by u6062536 on 11/16/2017.
 */
public class SearchUtils {

    interface SearchFunc {
        void search(HtmlElement e, String searchKey, List<HtmlElement> found);
    }

    public static void traverseDom(HtmlElement element, Consumer<HtmlElement> callBack) {
        callBack.accept(element);

        element.getChildren().forEach((child) -> traverseDom(child, callBack));
    }

    public static void traverseDom(HtmlElement element, String searchValue, BiConsumer<HtmlElement, String> callBack) {
        callBack.accept(element, searchValue);

        element.getChildren().forEach((child) -> traverseDom(child, searchValue, callBack));
    }

    private static void traverseDom(HtmlElement element, String searchValue, List<HtmlElement> found, SearchFunc callBack) {
        callBack.search(element, searchValue, found);

        element.getChildren().forEach((child) -> traverseDom(child, searchValue, found, callBack));
    }

    public static List<HtmlElement> tagSearch(String tag, boolean subSearch, List<HtmlElement> found, HtmlElement root) {
        List<HtmlElement> tagsFound = new ArrayList<>();

        SearchFunc func = (e, search, list) -> {
            if (search.equals("*")) {
                list.add(e);
            }
            else if (e.getTag().getTagName().equals(search)) {
                list.add(e);
            }
        };

        if (subSearch) {
            for (HtmlElement elem : found) {
                SearchUtils.traverseDom(elem, tag, tagsFound, func);
            }
        }
        else {
            SearchUtils.traverseDom(root, tag, tagsFound, func);
        }

        return tagsFound;
    }

    public static List<HtmlElement> classNameSubSearch(String className, List<HtmlElement> found) {
        List<HtmlElement> classesFound = new ArrayList<>();

        for (HtmlElement elem : found) {
            String classProp = elem.getTag().getProperty("class");
            if (className.equals("*")) {
                classesFound.add(elem);
            }
            else if (classProp != null && classProp.contains(className)) {
                classesFound.add(elem);
            }
        }

        return classesFound;
    }

    public static List<HtmlElement> classNameSearch(String className, HtmlElement root) {
        List<HtmlElement> classesFound = new ArrayList<>();

        SearchUtils.traverseDom(root, className, classesFound, (e, search, list) -> {
            String prop = e.getTag().getProperty("class");
            if (search.equals("*")) {
                list.add(e);
            }
            else if (prop != null) {
                String[] classes = prop.split("\\s+");
                for (String name : classes) {
                    name = name.trim();
                    if (name.equals(search)) {
                        list.add(e);
                    }
                }
            }
        });

        return classesFound;
    }
}
