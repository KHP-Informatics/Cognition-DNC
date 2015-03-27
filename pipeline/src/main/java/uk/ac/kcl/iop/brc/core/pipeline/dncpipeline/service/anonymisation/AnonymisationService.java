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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.anonymisation;

import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.Patient;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.PythonService;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.MissingResourceException;

@Service
public class AnonymisationService {

    private static Logger logger = Logger.getLogger(AnonymisationService.class);

    @Autowired
    private NamePseudonymiser namePseudonymiser;

    @Autowired
    private PhonePseudonymiser phonePseudonymiser;

    @Autowired
    private DateOfBirthPseudonymiser dateOfBirthPseudonymiser;

    @Autowired
    private NHSNumberPseudonymiser nhsNumberPseudonymiser;

    @Autowired
    private AddressPseudonymiser addressPseudonymiser;

    public String anonymisePatientHTML(Patient patient, String text) {
        Document document = Jsoup.parse(text);
        traverseAndAnonymise(document, patient);
        return document.toString();
    }

    public String anonymisePatientPlainText(Patient patient, String text) {
        text = namePseudonymiser.pseudonymise(patient, text);
        text = phonePseudonymiser.pseudonymise(patient, text);
        text = nhsNumberPseudonymiser.pseudonymise(patient, text);
        text = dateOfBirthPseudonymiser.pseudonymise(patient, text);
        text = addressPseudonymiser.pseudonymise(patient, text);
        return text;
    }

    private void traverseAndAnonymise(Node node, Patient patient) {
        if (node == null) {
            return;
        }

        if (node instanceof TextNode) {
            TextNode textNode = (TextNode) node;
            textNode.text(namePseudonymiser.pseudonymise(patient, textNode.text()));
            textNode.text(phonePseudonymiser.pseudonymise(patient, textNode.text()));
            textNode.text(nhsNumberPseudonymiser.pseudonymise(patient, textNode.text()));
            textNode.text(dateOfBirthPseudonymiser.pseudonymise(patient, textNode.text()));
            textNode.text(addressPseudonymiser.pseudonymise(patient, textNode.text()));
        }

        if (CollectionUtils.isEmpty(node.childNodes())) {
            return;
        }

        for (Node child : node.childNodes()) {
            traverseAndAnonymise(child, patient);
        }
    }

    public void setNamePseudonymiser(NamePseudonymiser namePseudonymiser) {
        this.namePseudonymiser = namePseudonymiser;
    }

    public void setPhonePseudonymiser(PhonePseudonymiser phonePseudonymiser) {
        this.phonePseudonymiser = phonePseudonymiser;
    }

    public void setDateOfBirthPseudonymiser(DateOfBirthPseudonymiser dateOfBirthPseudonymiser) {
        this.dateOfBirthPseudonymiser = dateOfBirthPseudonymiser;
    }

    public void setNhsNumberPseudonymiser(NHSNumberPseudonymiser nhsNumberPseudonymiser) {
        this.nhsNumberPseudonymiser = nhsNumberPseudonymiser;
    }

    public void setAddressPseudonymiser(AddressPseudonymiser addressPseudonymiser) {
        this.addressPseudonymiser = addressPseudonymiser;
    }
}
