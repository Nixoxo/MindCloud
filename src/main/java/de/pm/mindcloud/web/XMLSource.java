package de.pm.mindcloud.web;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by samuel on 18.06.15.
 */
public class XMLSource {
    public static final String VALUE = "xmlSource";

    public static XMLSource xml(HttpSession session) {
        try {
            return new XMLSource(session);
        } catch (ParserConfigurationException e) {
            return null;
        }
    }

    public static XMLSource xml() {
        return xml(null);
    }

    private final Document source;
    private final Element mindcloud;

    private XMLSource(HttpSession session) throws ParserConfigurationException {
        source = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        mindcloud = source.createElement("mindcloud");
        source.appendChild(mindcloud);
        if (session != null) {
            addSessionAttributes(session);
        }
    }

    private void addSessionAttributes(HttpSession session) {
        Element sessionElement = source.createElement("session");
        Enumeration<String> sessionNames = session.getAttributeNames();
        while (sessionNames.hasMoreElements()) {
            String key = sessionNames.nextElement();
            Element object = source.createElement(key);
            Object attribute = session.getAttribute(key);
            Field[] fields = attribute.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Element fieldElement = source.createElement(field.getName());
                    Object fieldValue = field.get(attribute);
                    if (fieldValue != null) {
                        fieldElement.setTextContent(fieldValue.toString());
                    }
                    object.appendChild(fieldElement);
                } catch (Exception e) {
                }
            }
            sessionElement.appendChild(object);
        }
        mindcloud.appendChild(sessionElement);
    }

    public XMLSource node(String tag, String value) {
        Element node = source.createElement(tag);
        node.setTextContent(value);
        mindcloud.appendChild(node);
        return this;
    }

    public XMLSource node(String tag, XMLSource value) {
        Element node = source.createElement(tag);
        NodeList children = value.mindcloud.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = source.importNode(children.item(i), true);
            node.appendChild(child);
        }
        mindcloud.appendChild(node);
        return this;
    }

    public DOMSource build() {
        return new DOMSource(source);
    }
}