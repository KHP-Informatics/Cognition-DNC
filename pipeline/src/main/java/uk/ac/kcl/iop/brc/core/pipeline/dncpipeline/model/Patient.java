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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Entity;
import java.util.*;

@Entity
public class Patient {

    private long id;

    @SerializedName("foreNames")
    private List<String> foreNames = new ArrayList<>();

    @SerializedName("surnames")
    private List<String> surnames = new ArrayList<>();

    @SerializedName("nhsNumbers")
    private List<String> nhsNumbers = new ArrayList<>();

    @SerializedName("dateOfBirth")
    private List<Date> dateOfBirths = new ArrayList<>();

    @SerializedName("addresses")
    private List<PatientAddress> addresses = new ArrayList<>();

    @SerializedName("phoneNumbers")
    private List<String> phoneNumbers = new ArrayList<>();

    private List<PatientCarer> carers = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void addPhoneNumber(String phoneNumber) {
        phoneNumbers.add(phoneNumber);
    }

    public void addAddress(PatientAddress patientAddress) {
        if (patientAddress.isNotEmpty()) {
            addresses.add(patientAddress);
        }
    }

    public void addForeName(String name) {
        foreNames.add(name);
    }

    public void addSurname(String surname) {
        surnames.add(surname);
    }

    public List<String> getForeNamesInDescendingLengthOrder() {
        if (CollectionUtils.isEmpty(foreNames)) {
            return new ArrayList<>();
        }

        List<String> foreNames = new ArrayList<>();
        for (String name : getForeNames()) {
            if (! StringUtils.isBlank(name)) {
                foreNames.add(name);
                addToListBySplitting(foreNames, name);
            }
        }
        Collections.sort(foreNames, (s1, s2) -> {
            if (s1 == null || s2 == null) {
                return -1;
            }
            return -Integer.valueOf(s1.length()).compareTo(Integer.valueOf(s2.length()));
        });

        return foreNames;
    }

    public List<String> getLastNamesInDescendingLengthOrder() {
        if (CollectionUtils.isEmpty(surnames)) {
            return new ArrayList<>();
        }

        List<String> surnames = new ArrayList<>();

        for (String name : getSurnames()) {
            if (! StringUtils.isBlank(name)) {
                surnames.add(name);
                addToListBySplitting(surnames, name);
            }
        }
        Collections.sort(surnames, (s1, s2) -> {
            if (s1 == null || s2 == null) {
                return -1;
            }
            return -Integer.valueOf(s1.length()).compareTo(Integer.valueOf(s2.length()));
        });

        return surnames;
    }

    private void addToListBySplitting(List<String> surnames, String name) {
        String[] splitBySpace = name.split(" ");
        String[] splitByDash = name.split("-");
        for (String nameSplit : splitBySpace) {
            if (nameSplit.length() > 3) {
                surnames.add(nameSplit);
            }
        }
        for (String nameSplit : splitByDash) {
            if (nameSplit.length() > 3) {
                surnames.add(nameSplit);
            }
        }
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

    public List<String> getCarerForeNames() {
        List<String> names = new ArrayList<>();
        carers.forEach(carer -> names.addAll(carer.getSeparatedFirstNames()));

        return names;
    }
    public List<String> getCarerLastNames() {
        List<String> lastNames = new ArrayList<>();
        carers.forEach(carer -> lastNames.addAll(carer.getSeparatedSurnames()));

        return lastNames;
    }

    public List<String> getSeparatedForeNames() {
        if (getForeNames() == null) {
            return new ArrayList<>();
        }
        List<String> names = new ArrayList<>();
        for (String name : getForeNames()) {
            if (StringUtils.isBlank(name)) {
                continue;
            }
            String[] nameSplit = name.split(" ");
            Collections.addAll(names, nameSplit);

        }
        return names;
    }

    public List<String> getSeparatedSurnames() {
        if (getSurnames() == null) {
            return new ArrayList<>();
        }
        List<String> names = new ArrayList<>();
        for (String name : getSurnames()) {
            if (StringUtils.isBlank(name)) {
                continue;
            }
            String[] nameSplit = name.split(" ");
            Collections.addAll(names, nameSplit);

        }
        return names;
    }

    public List<String> getNhsNumbers() {
        return nhsNumbers;
    }

    public void setNhsNumbers(List<String> nhsNumbers) {
        this.nhsNumbers = nhsNumbers;
    }

    public List<Date> getDateOfBirths() {
        return dateOfBirths;
    }

    public void setDateOfBirths(List<Date> dateOfBirths) {
        this.dateOfBirths = dateOfBirths;
    }

    public void addNhsNumber(String number) {
        nhsNumbers.add(number);
    }

    public void addDateOfBirth(Date dateOfBirth) {
        dateOfBirths.add(dateOfBirth);
    }
}
