package de.pm.mindcloud.web;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;

/**
 * Created by samuel on 18.06.15.
 */
public class XMLSource {
    public static XMLSource xml() {
        try {
            return new XMLSource();
        } catch (ParserConfigurationException e) {
            return null;
        }
    }

    private final Document source;
    private final Element mindcloud;

    private XMLSource() throws ParserConfigurationException {
        source = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        mindcloud = source.createElement("mindcloud");
        source.appendChild(mindcloud);
    }

    public XMLSource node(String tag, String value) {
        Element node = source.createElement(tag);
        node.setTextContent(value);
        mindcloud.appendChild(node);
        return this;
    }

    public XMLSource node(XMLSource value) {
        mindcloud.appendChild(value.mindcloud);
        return this;
    }

    public DOMSource build() {
        return new DOMSource(source);
    }
}