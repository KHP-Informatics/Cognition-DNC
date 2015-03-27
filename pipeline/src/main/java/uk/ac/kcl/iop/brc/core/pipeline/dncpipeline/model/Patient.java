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

}
