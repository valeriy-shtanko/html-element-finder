package com.agileengine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import static com.agileengine.HtmlElementQualifier.calculateQualifier;

public class HtmlElementFinder {

    private static Logger LOGGER = LoggerFactory.getLogger(HtmlElementFinder.class);


    public static void main(String[] args) throws Exception {

        checkArguments(args);

        // Read source element id (if defined)
        String sourceElementId = "make-everything-ok-button";
        int fileArgumentIndex = 0;

        if (!isPath(args[0])) {
            fileArgumentIndex = 1;
            sourceElementId = args[0];
        }

        // Read source element from original file
        Element sourceElement = getDocument(args[fileArgumentIndex]).getElementById(sourceElementId);

        if (sourceElement == null) {
            LOGGER.error("Source element with id '{}' not found.", sourceElementId);
            System.exit(-1);
        }

        // Seek for elements similar to source element in provided files
        for(int i = fileArgumentIndex + 1; i < args.length; i++) {
            try {
                processDocument(sourceElement, getDocument(args[i]));
            }
            catch(IOException e) {
                LOGGER.error("Error reading [{}] file", args[i], e);
            }
        }
    }


    // --------------------------------------------------------------------------------------------
    // Private methods
    //
    private static Document getDocument(String filePath) throws IOException {
        File htmlFile = new File(filePath);

        return Jsoup.parse( htmlFile,"utf8", htmlFile.getAbsolutePath());
    }


    private static void processDocument(Element sourceElement, Document document) {

        List<HtmlElement> htmlElements = new ArrayList<>();

        for (Element element : document.body().children()) {
            processElement(sourceElement, element, htmlElements);
        }

        // Find top similar element
        Collections.sort(
                htmlElements,
                (element1, element2) -> (-1) * element1.getQualifier().compareTo(element2.getQualifier()));

        Element result = htmlElements.get(0).getElement();

        // Print result
        System.out.printf("File: %s\n", document.location());
        printElementPath(result);
        System.out.println();
    }


    private static void processElement(Element sourceElement,
                                       Element targetElement,
                                       List<HtmlElement> htmlElements) {

        HtmlElement htmlElement = new HtmlElement(targetElement, calculateQualifier(sourceElement, targetElement));

        htmlElements.add(htmlElement);

        for (Element element : targetElement.children()) {
            processElement(sourceElement, element, htmlElements);
        }
    }


    private static void printElementPath(Element element) {

        StringBuilder stringBuilder = new StringBuilder();
        ListIterator<Element> it = element.parents().listIterator(element.parents().size());

        while (it.hasPrevious()) {
            Element el = it.previous();

            stringBuilder.append(String.format("%s", el.tag()) );

            if (!el.className().isEmpty()) {
                stringBuilder.append(String.format("(%s)", el.className()) );
            }

            if (it.hasPrevious()) {
                stringBuilder.append(" > ");
            }
        }

        System.out.println(stringBuilder.toString());
    }


    private static boolean isPath(String path) {
        File file = new File(path);

        return file.exists() && file.isFile();
    }


    private static void checkArguments(String[] args) {
        if(args.length == 0) {
            LOGGER.error("Required arguments not provided.");
            System.exit(-1);
        }

        int minArgsLength = isPath(args[0]) ? 1 : 2;

        if(args.length < minArgsLength) {
            LOGGER.error("Source file not defined.");
            System.exit(-1);
        }
    }
}
