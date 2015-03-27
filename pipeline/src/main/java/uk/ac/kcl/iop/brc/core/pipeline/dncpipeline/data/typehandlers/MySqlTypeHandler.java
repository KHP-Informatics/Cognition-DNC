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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.typehandlers;

import uk.ac.kcl.iop.brc.core.pipeline.common.service.ConfigurationService;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.DbColumnTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MySqlTypeHandler extends TypeHandler {

    @Autowired
    public ConfigurationService configurationService;

    @Override
    public boolean canHandle(String databaseName) {
        return databaseName.equalsIgnoreCase("mysql");
    }

    @Override
    public DbColumnTypes getDbColumnTypeForString(String typeStr) {
        configurationService.setConfigFilePath("dbColumnTypes/mysql.properties");
        String normalisedTypeName = configurationService.getValueAsString(typeStr.replaceAll(" ", "_"));
        return DbColumnTypes.getFromNormalisedString(normalisedTypeName);
    }

}
