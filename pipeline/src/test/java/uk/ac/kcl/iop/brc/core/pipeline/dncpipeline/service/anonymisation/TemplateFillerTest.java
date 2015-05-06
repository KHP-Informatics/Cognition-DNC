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

import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.Patient;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TemplateFillerTest {

    @Test
    public void shouldFillVelocityTemplate() {
        TemplateFiller templateFiller = new TemplateFiller();
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.addProperty("resource.loader", "class");
        velocityEngine.addProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        templateFiller.setVelocityEngine(velocityEngine);
        templateFiller.init();

        Map<String, Object> map = new HashMap<>();
        Patient value = new Patient();
        value.addNhsNumber("NHS Number");
        map.put("test", value);
        String filledTemplate = templateFiller.getFilledTemplate("velocityTemplateExample.json", map);

        assertThat(filledTemplate, equalTo("[\n  {\"test\": \"${someStr.substring(0,2)} ${someStr.substring(2, 4)}\"}\n]"));
    }

    @Test
    public void shouldStringSplittingWork() {
        TemplateFiller templateFiller = new TemplateFiller();
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.addProperty("resource.loader", "class");
        velocityEngine.addProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        templateFiller.setVelocityEngine(velocityEngine);
        templateFiller.init();

        Map<String, Object> map = new HashMap<>();
        map.put("someStr", "some string");
        String filledTemplate = templateFiller.getFilledTemplate("velocityTemplateExample.json", map);

        assertThat(filledTemplate, equalTo("[\n" +
                "  {\"test\": \"so me\"}\n" +
                "]"));
    }

}
