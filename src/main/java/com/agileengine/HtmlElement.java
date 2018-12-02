package com.agileengine;

import org.jsoup.nodes.Element;

public class HtmlElement {
    private Element element;
    private String qualifier;

    public HtmlElement() {
    }

    public HtmlElement(Element element, String qualifier) {
        this.element = element;
        this.qualifier = qualifier;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }
}
