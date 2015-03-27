/*
        The MIT License (MIT)
        Copyright (c) 2015 King's College London

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in
        all copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
        THE SOFTWARE.
*/

package uk.ac.kcl.iop.brc.core.pipeline.common.helper;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class IOPXMLHelper {

    private File xmlFile;
    private InputStream inputStream;

    public IOPXMLHelper(String filePath) {
        xmlFile = new File(filePath);
    }

    public IOPXMLHelper(URL resource) throws URISyntaxException {
        xmlFile = new File(resource.toURI());
    }

    public IOPXMLHelper(InputStream resourceAsStream) {
        this.inputStream = resourceAsStream;
    }

    public List<String> getAsStringListByXpath(String xpath) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document;
        if (xmlFile != null) {
            document = builder.parse(xmlFile);
        } else {
            document = builder.parse(inputStream);
        }
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath path = pathFactory.newXPath();
        XPathExpression expression = path.compile(xpath);
        NodeList nodeList = (NodeList) expression.evaluate(document, XPathConstants.NODESET);

        List<String> result = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            String nodeValue = node.getTextContent();
            result.add(nodeValue);
        }

        return result;
    }

}
