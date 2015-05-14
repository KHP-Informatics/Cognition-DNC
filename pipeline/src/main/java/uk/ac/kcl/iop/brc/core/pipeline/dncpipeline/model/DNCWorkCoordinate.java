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

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class DNCWorkCoordinate {

    @SerializedName("patientId")
    private long patientId;

    @SerializedName("sourceTable")
    private String sourceTable;

    @SerializedName("sourceColumn")
    private String sourceColumn;

    @SerializedName("idInSourceTable")
    private long idInSourceTable;

    @SerializedName("pkColumnName")
    private String pkColumnName;

    @SerializedName("type")
    private String type;

    @SerializedName("updatetime")
    private String updateTime;

    private boolean markedAsOCR = false;

    public DNCWorkCoordinate patientId(long id) {
        this.patientId = id;
        return this;
    }

    public DNCWorkCoordinate sourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
        return this;
    }

    public DNCWorkCoordinate sourceColumn(String sourceColumn){
        this.sourceColumn = sourceColumn;
        return this;
    }

    public DNCWorkCoordinate idInSourceTable(Long idInSourceTable) {
        this.idInSourceTable = idInSourceTable;
        return this;
    }

    public DNCWorkCoordinate type(String type) {
        this.type = type;
        return this;
    }

    public DNCWorkCoordinate pkColumnName(String pkColumnName) {
        this.pkColumnName = pkColumnName;
        return this;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getSourceColumn() {
        return sourceColumn;
    }

    public void setSourceColumn(String sourceColumn) {
        this.sourceColumn = sourceColumn;
    }

    public long getIdInSourceTable() {
        return idInSourceTable;
    }

    public void setIdInSourceTable(long idInSourceTable) {
        this.idInSourceTable = idInSourceTable;
    }

    public String getPkColumnName() {
        return pkColumnName;
    }

    public void setPkColumnName(String pkColumnName) {
        this.pkColumnName = pkColumnName;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdateTime() {
        if (updateTime == null ) {
            return "";
        }
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public boolean isBinary() {
        return "binary".equalsIgnoreCase(type);
    }


    @Override
    public String toString() {
        return String.format("Patient Id: %d, Source table: %s, Source column: %s, DocId: %d", patientId, sourceTable, sourceColumn, idInSourceTable);
    }

    public boolean isMarkedAsOCR() {
        return markedAsOCR;
    }

    public void setMarkedAsOCR(boolean markedAsOCR) {
        this.markedAsOCR = markedAsOCR;
    }
}
