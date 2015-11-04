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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.StringUtils;

public class PatientAddress {

    @SerializedName("address")
    private String address;

    @SerializedName("postCode")
    private String postCode;

    public static PatientAddress newAddressPostCode(String addressStr, String postCode) {
        PatientAddress address = new PatientAddress();
        address.setAddress(addressStr);
        address.setPostCode(postCode);
        return address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public boolean isNotTooShort() {
        return !StringUtils.isBlank(address) && address.length() > 3;

    }

    public boolean isNotEmpty() {
        return ! StringUtils.isBlank(address) && ! StringUtils.isBlank(postCode);
    }
}
