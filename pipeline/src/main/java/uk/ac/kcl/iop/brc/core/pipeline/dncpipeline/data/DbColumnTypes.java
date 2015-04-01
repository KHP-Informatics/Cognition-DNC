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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data;

import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.typehandlers.TypeHandler;

public enum DbColumnTypes {

    BLOB("blob"),
    VARCHAR("varchar"),
    CLOB("clob"),
    NOT_FOUND("");

    private String normalisedString;

    DbColumnTypes(String normalisedString) {
        this.normalisedString = normalisedString;
    }

    public static DbColumnTypes getFromColumnTypeAndDb(String typeStr, TypeHandler dbTypeHandler) {
        return dbTypeHandler.getDbColumnTypeForString(typeStr);
    }

    public static DbColumnTypes getFromNormalisedString(String normalisedType) {
        for (DbColumnTypes type : values()) {
            if (type.getNormalisedString().equalsIgnoreCase(normalisedType)) {
                return type;
            }
        }
        return NOT_FOUND;
    }

    public String getNormalisedString() {
        return normalisedString;
    }
}
