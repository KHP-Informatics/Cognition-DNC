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
        return Arrays.asList(split);
    }

    public List<String> getSeparatedSurnames() {
        if (StringUtils.isBlank(lastName)) {
            return new ArrayList<>();
        }
        String[] split = lastName.split(" ");
        return Arrays.asList(split);
    }
}
