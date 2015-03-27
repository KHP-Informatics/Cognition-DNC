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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data;

import uk.ac.kcl.iop.brc.core.pipeline.common.data.BaseDao;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.helper.ClobHelper;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.Patient;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.PatientAddress;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PatientDao extends BaseDao {

    @Autowired
    private ClobHelper clobHelper;

    private static Logger logger = Logger.getLogger(PatientDao.class);

    /**
     * Returns the patient with the given id.
     * Returns the patient from the cache if it's been fetched once.
     *
     * @param id
     * @return a patient object of the patient with the given id.
     */
    @Cacheable(value = "patients", key = "#id")
    public Patient getPatient(Long id) {
        Query getPatient = getCurrentSourceSession().getNamedQuery("getPatient");
        getPatient.setParameter("patientId", id);

        Patient patient = (Patient) getPatient.
                setFirstResult(0).setMaxResults(1).
                setResultTransformer(Transformers.aliasToBean(Patient.class)).uniqueResult();

        setNames(patient);
        setPhoneNumbers(patient);
        setAddresses(patient);

        return patient;
    }

    private void setPhoneNumbers(Patient patient) {
        Query getPhoneNumbers = getCurrentSourceSession().getNamedQuery("getPhoneNumbers");
        getPhoneNumbers.setParameter("patientId", patient.getId());
        List list = getPhoneNumbers.list();
        List<String> phoneNumbers = new ArrayList<>();
        list.forEach(object -> {
            String phone = clobHelper.getStringFromExpectedClob(object);
            phoneNumbers.add(phone);
        });
        patient.setPhoneNumbers(phoneNumbers);
    }

    /**
     * Fetches the addresses of the patient from the DB and assigns them to the given patient.
     *
     * @param patient
     */
    private void setAddresses(Patient patient) {
        Query getAddresses = getCurrentSourceSession().getNamedQuery("getAddresses");
        getAddresses.setParameter("patientId", patient.getId());
        List addressObjects = getAddresses.list();
        addressObjects.forEach(object -> {
            Object[] addressRow = (Object[]) object;
            String addressStr = clobHelper.getStringFromExpectedClob(addressRow[0]);
            String postCode = clobHelper.getStringFromExpectedClob(addressRow[1]);
            PatientAddress address = PatientAddress.newAddressPostCode(addressStr, postCode);
            patient.addAddress(address);
        });
    }

    /**
     * Fetches the names of the patient from the DB and assigns them to the given patient.
     *
     * @param patient
     */
    private void setNames(Patient patient) {
        Query getForeNames = getCurrentSourceSession().getNamedQuery("getPatientNames");
        getForeNames.setParameter("patientId", patient.getId());
        List foreNames = getForeNames.list();
        for (Object dbObject : foreNames) {
            Object[] namePair = (Object[]) dbObject;
            String name = clobHelper.getStringFromExpectedClob(namePair[0]);
            String lastName = clobHelper.getStringFromExpectedClob(namePair[1]);
            patient.addForeName(name);
            patient.addSurname(lastName);
        }
    }

    /**
     * Return patients between row start and end.
     *
     * @param start
     * @param end
     * @return List of patients
     */
    public List<Patient> getPatientsWithBetween(int start, int end) {
        Query getAllPatients = getCurrentSourceSession().getNamedQuery("getAllPatients");
        List<Patient> patients = getAllPatients.
                setFirstResult(start).setMaxResults(end).
                setResultTransformer(Transformers.aliasToBean(Patient.class)).list();
        return patients;
    }

}