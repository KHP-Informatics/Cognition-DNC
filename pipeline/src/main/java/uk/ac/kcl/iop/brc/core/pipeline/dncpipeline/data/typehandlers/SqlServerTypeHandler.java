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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.typehandlers;

import uk.ac.kcl.iop.brc.core.pipeline.common.service.ConfigurationService;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.DbColumnTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SqlServerTypeHandler extends TypeHandler {

    @Autowired
    public ConfigurationService configurationService;

    @Override
    public boolean canHandle(String databaseName) {
        return databaseName.equalsIgnoreCase("sqlserver");
    }

    @Override
    public DbColumnTypes getDbColumnTypeForString(String typeStr) {
        configurationService.setConfigFilePath("dbColumnTypes/sqlServer.properties");
        String normalisedTypeName = configurationService.getValueAsString(typeStr.replaceAll(" ", "_"));
        return DbColumnTypes.getFromNormalisedString(normalisedTypeName);
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

}
