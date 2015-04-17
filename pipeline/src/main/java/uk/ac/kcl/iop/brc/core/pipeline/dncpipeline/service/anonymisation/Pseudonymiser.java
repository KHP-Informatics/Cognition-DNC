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

import uk.ac.kcl.iop.brc.core.pipeline.common.helper.JsonHelper;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class Pseudonymiser {

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

        List<PseudonymisationRule> pseudonymisationRules = jsonHelper.loadListFromString(jsonRules);

        for (PseudonymisationRule rule : pseudonymisationRules) {
            if (rule == null) {
                continue;
            }
            text = text.replaceAll(rule.getRegexp(), rule.getPlaceHolder());
        }

        return text;
    }

}
