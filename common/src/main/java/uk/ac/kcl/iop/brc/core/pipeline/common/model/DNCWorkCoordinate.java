/*
        Cognition-DNC (Dynamic Name Concealer)         Developed by Ismail Kartoglu (https://github.com/iemre)
        Binary to text document converter and database pseudonymiser.

        Copyright (C) 2015 Biomedical Research Centre for Mental Health

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
    private long PATIENTID;

    @SerializedName("sourceTable")
    private String SOURCETABLE;

    @SerializedName("sourceColumn")
    private String SOURCECOLUMN;

    @SerializedName("idInSourceTable")
    private long IDINSOURCETABLE;

    @SerializedName("pkColumnName")
    private String PKCOLUMNNAME;

    @SerializedName("type")
    private String TYPE;

    @SerializedName("updatetime")
    private String UPDATETIME;

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
        this.PATIENTID = id;
        return this;
    }

    public DNCWorkCoordinate sourceTable(String sourceTable) {
        this.SOURCETABLE = sourceTable;
        return this;
    }

    public DNCWorkCoordinate sourceColumn(String sourceColumn){
        this.SOURCECOLUMN = sourceColumn;
        return this;
    }

    public DNCWorkCoordinate idInSourceTable(Long idInSourceTable) {
        this.IDINSOURCETABLE = idInSourceTable;
        return this;
    }

    public DNCWorkCoordinate type(String type) {
        this.TYPE = type;
        return this;
    }

    public DNCWorkCoordinate pkColumnName(String pkColumnName) {
        this.PKCOLUMNNAME = pkColumnName;
        return this;
    }

    public String getSourceTable() {
        return SOURCETABLE;
    }

    public void setSourceTable(String sourceTable) {
        this.SOURCETABLE = sourceTable;
    }

    public String getSourceColumn() {
        return SOURCECOLUMN;
    }

    public void setSourceColumn(String sourceColumn) {
        this.SOURCECOLUMN = sourceColumn;
    }

    public long getIdInSourceTable() {
        return IDINSOURCETABLE;
    }

    public void setIdInSourceTable(long idInSourceTable) {
        this.IDINSOURCETABLE = idInSourceTable;
    }

    public String getPkColumnName() {
        return PKCOLUMNNAME;
    }

    public void setPkColumnName(String pkColumnName) {
        this.PKCOLUMNNAME = pkColumnName;
    }

    public long getPatientId() {
        return PATIENTID;
    }

    public void setPatientId(long patientId) {
        this.PATIENTID = patientId;
    }

    public String getType() {
        return TYPE;
    }

    public void setType(String type) {
        this.TYPE = type;
    }

    public String getUpdateTime() {
        if (UPDATETIME == null ) {
            return "";
        }
        return UPDATETIME;
    }

    public void setUpdateTime(String updateTime) {
        this.UPDATETIME = updateTime;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public boolean isBinary() {
        return "binary".equalsIgnoreCase(TYPE);
    }

    @Override
    public String toString() {
        return String.format("Patient Id: %d, Source table: %s, Source column: %s, DocId: %d", PATIENTID, SOURCETABLE, SOURCECOLUMN, IDINSOURCETABLE);
    }

    public String getFileName() {
        if (0 == IDINSOURCETABLE || -1 == IDINSOURCETABLE) {
            return String.format("%s_%d", SOURCETABLE, RandomUtils.nextLong());
        }
        return String.format("%s_%d", SOURCETABLE, IDINSOURCETABLE);
    }

    public boolean isMarkedAsOCR() {
        return markedAsOCR;
    }

    public void setMarkedAsOCR(boolean markedAsOCR) {
        this.markedAsOCR = markedAsOCR;
    }
}
