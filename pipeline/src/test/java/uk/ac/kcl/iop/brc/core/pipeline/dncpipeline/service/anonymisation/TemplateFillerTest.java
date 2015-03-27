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
        value.setNHSNumber("NHS Number");
        map.put("test", value);
        String filledTemplate = templateFiller.getFilledTemplate("velocityTemplateExample.json", map);

        assertThat(filledTemplate, equalTo("[\n  {\"test\":\"NHS Number\"},\n  {\"test\": \"${someStr.substring(0,2)} ${someStr.substring(2, 4)}\"}\n]"));
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
                "  {\"test\":\"${test.getNHSNumber()}\"},\n" +
                "  {\"test\": \"so me\"}\n" +
                "]"));
    }

}