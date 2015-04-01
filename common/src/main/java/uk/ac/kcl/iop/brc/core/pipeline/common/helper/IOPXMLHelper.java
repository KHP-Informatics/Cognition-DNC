/*
        Copyright (c) 2015 King's College London

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
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
