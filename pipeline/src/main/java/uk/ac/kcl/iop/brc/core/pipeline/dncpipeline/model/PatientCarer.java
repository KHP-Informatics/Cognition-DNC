/*
        Cognition-DNC (Dynamic Name Concealer)
        Binary to text document converter and database pseudonymiser.

        Copyright (C) 2015 Ismail E. Kartoglu, Richard G. Jackson

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

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatientCarer {

    private String firstName;

    private String lastName;

    public PatientCarer() {}

    public PatientCarer(String name, String lastName) {
        this.firstName = name;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getSeparatedFirstNames() {
        if (StringUtils.isBlank(firstName)) {
            return new ArrayList<>();
        }
        String[] split = firstName.split(" ");
        List<String> names = new ArrayList<>();
        for (String name : split) {
            names.addAll(Arrays.asList(name.split("-")));
        }
        return names;
    }

    public List<String> getSeparatedSurnames() {
        if (StringUtils.isBlank(lastName)) {
            return new ArrayList<>();
        }
        String[] split = lastName.split(" ");
        List<String> names = new ArrayList<>();
        for (String name : split) {
            names.addAll(Arrays.asList(name.split("-")));
        }
        return names;
    }
}
