package javadom.page;

import java.util.List;
import java.util.stream.Collectors;

public class SearchUtil {

    public static List<Node> getElementsByTagName(String tag, List<Node> searchList) {
        return searchList.stream()
                .filter(node -> tag.equalsIgnoreCase(node.getName()))
                .collect(Collectors.toList());
    }

    public static List<Node> getElementById(String id, List<Node> searchList) {
        return searchList.stream()
                .filter(node -> {
                    String nodeId = node.getAttribute("id");
                    return nodeId != null && nodeId.equals(id);
                })
                .collect(Collectors.toList());
    }

    public static List<Node> getElementsByClassName(String className, List<Node> searchList) {
        return searchList.stream()
                .filter(node -> {
                    String tagClass = node.getAttribute("class");
                    return tagClass != null && tagClass.equals(className);
                })
                .collect(Collectors.toList());
    }
}
