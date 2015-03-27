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

import uk.ac.kcl.iop.brc.core.pipeline.common.utils.TimeUtil;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.StringWriter;
import java.util.Map;
import uk.ac.kcl.iop.brc.core.pipeline.common.utils.StringTools;

@Service
public class TemplateFiller {

    @Autowired
    private VelocityEngine velocityEngine;

    @PostConstruct
    public void init() {
        velocityEngine.init();
    }

    public String getFilledTemplate(String path, Map<String, Object> objectMap) {
        Template template = velocityEngine.getTemplate(path);
        VelocityContext context = new VelocityContext();

        StringWriter stringWriter = new StringWriter();
        addCommonToolsToMap(objectMap);
        objectMap.forEach((key, value) -> context.put(key, value));

        template.merge(context, stringWriter);

        return stringWriter.toString();
    }

    private void addCommonToolsToMap(Map<String, Object> objectMap) {
        objectMap.put("StringTools", StringTools.class);
        objectMap.put("TimeUtil", TimeUtil.class);
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
}
