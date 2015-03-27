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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class DNCWorkCoordinate {

    @SerializedName("patientId")
    private Long patientId;

    @SerializedName("sourceTable")
    private String sourceTable;

    @SerializedName("sourceColumn")
    private String sourceColumn;

    @SerializedName("idInSourceTable")
    private Long idInSourceTable;

    @SerializedName("pkColumnName")
    private String pkColumnName;

    @SerializedName("type")
    private String type;

    @SerializedName("updatetime")
    private String updateTime;

    public DNCWorkCoordinate patientId(Long id) {
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

    public Long getIdInSourceTable() {
        return idInSourceTable;
    }

    public void setIdInSourceTable(Long idInSourceTable) {
        this.idInSourceTable = idInSourceTable;
    }

    public String getPkColumnName() {
        return pkColumnName;
    }

    public void setPkColumnName(String pkColumnName) {
        this.pkColumnName = pkColumnName;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
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

    public String getUniqueIdentifier() {
        if (idInSourceTable == null) {
            return "";
        }
        return String.valueOf(idInSourceTable);
    }

    @Override
    public String toString() {
        return String.format("Patient Id: %d, Source table: %s, Source column: %s, DocId: %d", patientId, sourceTable, sourceColumn, idInSourceTable);
    }
}
