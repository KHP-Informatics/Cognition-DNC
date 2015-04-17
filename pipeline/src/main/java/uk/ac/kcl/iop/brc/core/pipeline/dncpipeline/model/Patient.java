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

import javax.persistence.Entity;
import java.util.*;

@Entity
public class Patient {

    private long id;

    @SerializedName("foreNames")
    private List<String> foreNames = new ArrayList<>();

    @SerializedName("surnames")
    private List<String> surnames = new ArrayList<>();

    @SerializedName("NHSNumber")
    private String NHSNumber;

    @SerializedName("dateOfBirth")
    private Date dateOfBirth;

    @SerializedName("addresses")
    private List<PatientAddress> addresses = new ArrayList<>();

    @SerializedName("phoneNumbers")
    private List<String> phoneNumbers = new ArrayList();

    private List<PatientCarer> carers = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNHSNumber() {
        return NHSNumber;
    }

    public void setNHSNumber(String NHSNumber) {
        this.NHSNumber = NHSNumber;
    }

    public List<PatientAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<PatientAddress> addresses) {
        this.addresses = addresses;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<String> getForeNames() {
        return foreNames;
    }

    public void setForeNames(List<String> foreNames) {
        this.foreNames = foreNames;
    }

    public List<String> getSurnames() {
        return surnames;
    }

    public void setSurnames(List<String> surnames) {
        this.surnames = surnames;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void addPhoneNumber(String phoneNumber) {
        phoneNumbers.add(phoneNumber);
    }

    public void addAddress(PatientAddress patientAddress) {
        addresses.add(patientAddress);
    }

    public void addForeName(String name) {
        foreNames.add(name);
    }

    public void addSurname(String surname) {
        surnames.add(surname);
    }

    public List<String> getForeNamesInDescendingLengthOrder() {
        if (getForeNames() == null) {
            return new ArrayList<>();
        }

        List<String> foreNames = new ArrayList<>(getForeNames());
        Collections.sort(foreNames, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return -Integer.valueOf(s1.length()).compareTo(Integer.valueOf(s2.length()));
            }
        });

        return foreNames;
    }

    public List<String> getLastNamesInDescendingLengthOrder() {
        if (getSurnames() == null) {
            return new ArrayList<>();
        }

        List<String> surnames = new ArrayList<>(getSurnames());
        Collections.sort(surnames, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return -Integer.valueOf(s1.length()).compareTo(Integer.valueOf(s2.length()));
            }
        });

        return surnames;
    }

    public List<PatientCarer> getCarers() {
        return carers;
    }

    public void setCarers(List<PatientCarer> carers) {
        this.carers = carers;
    }

    public void addCarer(PatientCarer carer) {
        carers.add(carer);
    }
}
