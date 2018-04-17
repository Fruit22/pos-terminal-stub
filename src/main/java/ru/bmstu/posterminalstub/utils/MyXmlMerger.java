package ru.bmstu.posterminalstub.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

public class MyXmlMerger {
    public String merge(String xml1, String xml2) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = f.newDocumentBuilder();

        Document doc = builder.parse(new ByteArrayInputStream(xml1.getBytes("UTF-8")));
        Document doc2 = builder.parse(new ByteArrayInputStream(xml2.getBytes("UTF-8")));

        doc.getDocumentElement();
        doc2.getDocumentElement();

        List<Node> nodeListFirst = createClassicListFromDoc(doc);
        List<Node> nodeListSecond = createClassicListFromDoc(doc2);

        nodeListFirst.forEach(nodeFromFirstList -> {
            nodeListSecond.forEach(nodeFromSecondList -> {
            mergeModules(nodeFromFirstList, nodeFromSecondList);
            });
        });

        return convertDocumentToXmlString(doc);
    }

    private String convertDocumentToXmlString(Document document) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        StringWriter sw = new StringWriter();
        t.transform(new DOMSource(document), new StreamResult(sw));
        return sw.toString();
    }

    private List<Node> createClassicListFromDoc(Document document) {
        return Optional.ofNullable(document)
                .map(Document::getDocumentElement)
                .map(Element::getChildNodes)
                .map(nodeList -> {
                    List<Node> list = new ArrayList<>();
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        list.add(nodeList.item(i));
                    }
                    return list;
                })
                .orElse(Collections.emptyList());
    }

    private String getAttribute(Node node, String attribute) {
        return Optional.ofNullable(node)
                .map(Node :: getAttributes)
                .map(attr -> attr.getNamedItem(attribute))
                .map(Node :: getNodeValue)
                .orElse(null);
    }

    private void mergeModules(Node nodeOne, Node nodeTwo) {
        String nameOne = getAttribute(nodeOne, "name");
        String nameTwo = getAttribute(nodeTwo, "name");
        String versionOne = getAttribute(nodeOne, "version");
        String versionTwo = getAttribute(nodeTwo, "version");

        if (nameOne != null && nameTwo != null && nameOne.equals(nameTwo)) {
            if (VersionHelper.compare(versionOne, versionTwo) == -1 && nodeTwo.getTextContent() != null) {
                nodeOne.setTextContent(nodeTwo.getTextContent());
                nodeOne.getAttributes().getNamedItem("version").setNodeValue(versionTwo);
            }
        }
    }
}
