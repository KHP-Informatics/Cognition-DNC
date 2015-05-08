package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PatientAddressTest {

    @Test
    public void shouldReturnFalseIfAddressIsNotTooShort() {
        PatientAddress address = new PatientAddress();

        assertThat(address.isNotTooShort(), equalTo(false));

        address.setAddress("add");

        assertThat(address.isNotTooShort(), equalTo(false));
    }

}