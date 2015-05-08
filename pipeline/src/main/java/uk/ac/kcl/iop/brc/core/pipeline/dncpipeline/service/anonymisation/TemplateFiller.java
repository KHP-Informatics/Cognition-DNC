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

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.runtime.ParserPoolImpl;
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
        velocityEngine.setProperty("parser.pool.size", 200);
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
        objectMap.put("StringUtils", StringUtils.class);
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }
}
