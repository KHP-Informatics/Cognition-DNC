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

package uk.ac.kcl.iop.brc.core.pipeline.common.utils;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class TimeUtilTest {

    @Test
    public void shouldGetDateFromString() throws ParseException {
        Date dateFromString = TimeUtil.getDateFromString("09/05/1990", "dd/MM/yyyy");

        assertThat(TimeUtil.getFormattedDate(dateFromString, "dd/MM/yyyy"), equalTo("09/05/1990"));
    }

	@Test
	public void shouldAllowRegexInDateFormat() throws ParseException {
		Date dateFromString = TimeUtil.getDateFromString("09/05/1990", "dd/MM/yyyy");

		assertThat(TimeUtil.getFormattedDate(dateFromString, "dd'(th|rd|st)?' MMMM"), equalTo("09(th|rd|st)? May"));
		assertThat(TimeUtil.getFormattedDate(dateFromString, "d"), equalTo("9"));
	}

}
