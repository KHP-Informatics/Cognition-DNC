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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.anonymisation;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.Patient;

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

    public String pseudonymisePersonHTML(Patient patient, String text) {
        Document document = Jsoup.parse(text);
        traverseAndAnonymise(document, patient);
        return document.toString();
    }

    public String pseudonymisePersonPlainText(Patient patient, String text) {
        return pseudonymiseAll(patient, text);
    }

    private String pseudonymiseAll(Patient patient, String text) {
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
            textNode.text(pseudonymiseAll(patient, textNode.text()));
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
