package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PatientCarerTest {

    @Test
    public void shouldGetSeparatedForeNames() {
        PatientCarer patientCarer = new PatientCarer();
        patientCarer.setFirstName("Name Surname");
        patientCarer.setLastName("Last Name");
        List<String> names = patientCarer.getSeparatedFirstNames();
        List<String> surnames = patientCarer.getSeparatedSurnames();

        assertThat(names.get(0), equalTo("Name"));
        assertThat(names.get(1), equalTo("Surname"));

        assertThat(surnames.get(0), equalTo("Last"));
        assertThat(surnames.get(1), equalTo("Name"));
    }

    @Test
    public void shouldGetEmptyListWhenForeNameIsNull() {
        PatientCarer patientCarer = new PatientCarer();
        patientCarer.setFirstName(null);
        patientCarer.setLastName(null);

        List<String> names = patientCarer.getSeparatedFirstNames();
        List<String> surnames = patientCarer.getSeparatedSurnames();

        assertThat(names.size(), equalTo(0));
        assertThat(surnames.size(), equalTo(0));
    }

}