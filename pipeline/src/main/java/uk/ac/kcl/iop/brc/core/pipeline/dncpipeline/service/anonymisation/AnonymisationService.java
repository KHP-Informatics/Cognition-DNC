/*
        Cognition-DNC (Dynamic Name Concealer)
        Binary to text document converter and database pseudonymiser.

        Copyright (C) 2015 Ismail E. Kartoglu, Richard G. Jackson

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
