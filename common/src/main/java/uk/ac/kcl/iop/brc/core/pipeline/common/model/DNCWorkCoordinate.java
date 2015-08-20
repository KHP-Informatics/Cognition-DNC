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



package uk.ac.kcl.iop.brc.core.pipeline.common.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.math.RandomUtils;

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

    public static DNCWorkCoordinate createEmptyCoordinate() {
        DNCWorkCoordinate coordinate = new DNCWorkCoordinate();
        coordinate.setPkColumnName("");
        coordinate.setPatientId(-1);
        coordinate.setSourceColumn("");
        coordinate.setIdInSourceTable(-1);
        return coordinate;
    }

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

    public String getFileName() {
        if (0 == idInSourceTable || -1 == idInSourceTable) {
            return String.format("%s_%d", sourceTable, RandomUtils.nextLong());
        }
        return String.format("%s_%d", sourceTable, idInSourceTable);
    }

    public boolean isMarkedAsOCR() {
        return markedAsOCR;
    }

    public void setMarkedAsOCR(boolean markedAsOCR) {
        this.markedAsOCR = markedAsOCR;
    }
}
