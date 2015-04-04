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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model;

import com.google.gson.annotations.SerializedName;

@Deprecated
public class TableColumnModel {

    @SerializedName("tableName")
    private String tableName;

    @SerializedName("oldColumnName")
    private String oldColumnName;

    @SerializedName("newColumnName")
    private String newColumnName;

    @SerializedName("idColumnName")
    private String idColumnName;

    public static TableColumnModel newTableColumnModel(String tableName, String oldColumnName, String newColumnName) {
        TableColumnModel model = new TableColumnModel();
        model.tableName = tableName;
        model.oldColumnName = oldColumnName;
        model.newColumnName = newColumnName;
        return model;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOldColumnName() {
        return oldColumnName;
    }

    public void setOldColumnName(String oldColumnName) {
        this.oldColumnName = oldColumnName;
    }

    public String getNewColumnName() {
        return newColumnName;
    }

    public void setNewColumnName(String newColumnName) {
        this.newColumnName = newColumnName;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

    public void setIdColumnName(String idColumnName) {
        this.idColumnName = idColumnName;
    }
}
