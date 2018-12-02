package com.agileengine;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import java.util.Iterator;


public class HtmlElementQualifier {

    private static final String ATTRIBUTE_EQUAL     = "1";
    private static final String ATTRIBUTE_NOT_EQUAL = "0";

    public static String calculateQualifier(Element source, Element target) {
        StringBuilder resultBuilder = new StringBuilder();

        // Compare id, it has highest priority in comparison
        if (source.id().equals(target.id())) {
            resultBuilder.append(ATTRIBUTE_EQUAL);
        } else {
            resultBuilder.append(ATTRIBUTE_NOT_EQUAL);
        }

        // Compare other elements
        for (Iterator<Attribute> iterator = source.attributes().iterator(); iterator.hasNext(); ) {
            Attribute sourceAttribute = iterator.next();

            // Id already compared above, skipping
            if(sourceAttribute.getKey().equalsIgnoreCase("id")) {
                continue;
            }

            // No attribute ib target element
            if(!target.attributes().hasKey(sourceAttribute.getKey())) {
                resultBuilder.append(ATTRIBUTE_NOT_EQUAL);
                continue;
            }

            // Compare attribute value
            String targetValue = target.attributes().get(sourceAttribute.getKey());

            if(targetValue.equalsIgnoreCase(sourceAttribute.getValue())) {
                resultBuilder.append(ATTRIBUTE_EQUAL);
            } else {
                resultBuilder.append(ATTRIBUTE_NOT_EQUAL);
            }
        }

        // Compare text. it has lowest priority in comparison
        if (source.hasText()) {
            if (target.hasText() && source.text().equalsIgnoreCase(target.text())) {
                resultBuilder.append(ATTRIBUTE_EQUAL);
            } else {
                resultBuilder.append(ATTRIBUTE_NOT_EQUAL);
            }
        }

        return resultBuilder.toString();
    }
}
