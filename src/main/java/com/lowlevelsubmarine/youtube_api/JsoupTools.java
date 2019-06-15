package com.lowlevelsubmarine.youtube_api;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import java.util.HashMap;

public class JsoupTools {

    public static HashMap<String, String> getMetaTags(Element element, String propertyName) {
        HashMap<String, String> metaTags = new HashMap<>();
        for (Element item : element.getElementsByTag("meta")) {
            String name = null;
            String content = null;
            for (Attribute attribute : item.attributes()) {
                if (attribute.getKey().equals(propertyName)) {
                    name = attribute.getValue();
                } else if (attribute.getKey().equals("content")) {
                    content = attribute.getValue();
                }
            }
            if (name != null && content != null) {
                metaTags.put(name, content);
            }
        }
        return metaTags;
    }

}
