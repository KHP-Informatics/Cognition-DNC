/*
        Cognition-DNC (Dynamic Name Concealer)
        Binary to text document converter and database pseudonymiser.

        Copyright (C) 2015 Ismail E. Kartoglu

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
