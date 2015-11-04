/*
        Cognition-DNC (Dynamic Name Concealer)         Developed by Ismail Kartoglu (https://github.com/iemre)
        Binary to text document converter and database pseudonymiser.

        Copyright (C) 2015 Biomedical Research Centre for Mental Health

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
import uk.ac.kcl.iop.brc.core.pipeline.common.helper.JsonHelper;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class Pseudonymiser {

    private static Logger logger = Logger.getLogger(Pseudonymiser.class);

    @Autowired
    private TemplateFiller templateFiller;

    public abstract String getJsonRuleFilePath();

    public abstract boolean canIgnore(Patient patient);

    public String pseudonymise(Patient patient, String text) {
        if (canIgnore(patient)) {
            return text;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("patient", patient);
        map.put("sourceText", text);
        String jsonRules = templateFiller.getFilledTemplate(getJsonRuleFilePath(), map);

        JsonHelper<PseudonymisationRule> jsonHelper = new JsonHelper<>(PseudonymisationRule[].class);

        List<PseudonymisationRule> pseudonymisationRules = new ArrayList<>();
        try {
            pseudonymisationRules = jsonHelper.loadListFromString(jsonRules);
        } catch (Exception ex) {
            logger.error("Error while loading pseudonymisation rules for Patient " + patient.getId() + " from " + getJsonRuleFilePath());
            ex.printStackTrace();
        }

        for (PseudonymisationRule rule : pseudonymisationRules) {
            if (rule == null) {
                continue;
            }
            text = text.replaceAll(rule.getRegexp(), rule.getPlaceHolder());
        }

        return text;
    }

}
