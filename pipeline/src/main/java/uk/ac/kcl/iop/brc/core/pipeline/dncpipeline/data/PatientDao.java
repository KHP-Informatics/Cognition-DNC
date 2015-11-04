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


package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import uk.ac.kcl.iop.brc.core.pipeline.common.data.BaseDao;
import uk.ac.kcl.iop.brc.core.pipeline.common.data.SessionWrapper;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.helper.ClobHelper;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.Patient;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.PatientAddress;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.PatientCarer;

import java.util.ArrayList;
import java.util.Date;
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
        SessionWrapper sessionWrapper = getCurrentSourceSession();
        try {
            Query getPatient = sessionWrapper.getNamedQuery("getPatient");
            getPatient.setParameter("patientId", id);
            logger.info("Loading patient " + id);
            Patient patient = (Patient) getPatient.
                    setFirstResult(0).setMaxResults(1).
                    setResultTransformer(Transformers.aliasToBean(Patient.class)).uniqueResult();

            setNames(patient);
            setPhoneNumbers(patient);
            setAddresses(patient);
            setCarers(patient);
            setNhsNumbers(patient);
            setDateOfBirths(patient);

            return patient;
        } finally {
            sessionWrapper.closeSession();
        }
    }

    private void setNhsNumbers(Patient patient) {
        SessionWrapper sessionWrapper = getCurrentSourceSession();
        try {
            Query getNhsNumbers = sessionWrapper.getNamedQuery("getNhsNumbers");
            getNhsNumbers.setParameter("patientId", patient.getId());
            List nhsNumbers = getNhsNumbers.list();
            nhsNumbers.forEach(object -> {
                String number;
                if (object instanceof String) {
                    number = (String) object;
                } else {
                    Object[] nhsNumberRow = (Object[]) object;
                    number = clobHelper.getStringFromExpectedClob(nhsNumberRow[0]);
                }
                if (! StringUtils.isBlank(number)) {
                    patient.addNhsNumber(number);
                }
            });
        } catch (Exception ex) {
            logger.warn("Error while loading NHS Numbers of patient " + patient.getId() + ". Does the specified table exist? " + ex.getMessage());
        } finally {
            sessionWrapper.closeSession();
        }
    }

    private void setDateOfBirths(Patient patient) {
        SessionWrapper sessionWrapper = getCurrentSourceSession();
        try {
            Query getDateOfBirths = sessionWrapper.getNamedQuery("getDateOfBirths");
            getDateOfBirths.setParameter("patientId", patient.getId());
            List dateOfBirths = getDateOfBirths.list();
            dateOfBirths.forEach(object -> {
                Date dateOfBirth;
                if (object instanceof Date) {
                    dateOfBirth = (Date) object;
                } else {
                    Object[] dateOfBirthRow = (Object[]) object;
                    dateOfBirth = (Date) dateOfBirthRow[0];
                }
                if (dateOfBirth != null) {
                    patient.addDateOfBirth(dateOfBirth);
                }
            });
        } catch (Exception ex) {
            logger.warn("Error while loading date of births of patient " + patient.getId() + ". Does the specified table exist? " + ex.getMessage());
        } finally {
            sessionWrapper.closeSession();
        }
    }

    private void setCarers(Patient patient) {
        SessionWrapper sessionWrapper = getCurrentSourceSession();
        try {
            Query getCarers = sessionWrapper.getNamedQuery("getCarers");
            getCarers.setParameter("patientId", patient.getId());
            List carerObjects = getCarers.list();
            carerObjects.forEach(object -> {
                Object[] carerRow = (Object[]) object;
                String name = clobHelper.getStringFromExpectedClob(carerRow[0]);
                String lastName = clobHelper.getStringFromExpectedClob(carerRow[1]);
                PatientCarer carer = new PatientCarer(name, lastName);
                patient.addCarer(carer);
            });
        } catch (Exception ex) {
            logger.warn("Error while loading carers of patient " + patient.getId() + ". Does the specified carer table exist? " + ex.getMessage());
        } finally {
            sessionWrapper.closeSession();
        }
    }

    private void setPhoneNumbers(Patient patient) {
        SessionWrapper sessionWrapper = getCurrentSourceSession();
        try {
            Query getPhoneNumbers = sessionWrapper.getNamedQuery("getPhoneNumbers");
            getPhoneNumbers.setParameter("patientId", patient.getId());
            List list = getPhoneNumbers.list();
            List<String> phoneNumbers = new ArrayList<>();
            list.forEach(object -> {
                String phone = clobHelper.getStringFromExpectedClob(object);
                if (!StringUtils.isBlank(phone)) {
                    phoneNumbers.add(phone);
                }
            });
            patient.setPhoneNumbers(phoneNumbers);
        } finally {
            sessionWrapper.closeSession();
        }
    }

    /**
     * Fetches the addresses of the patient from the DB and assigns them to the given patient.
     *
     * @param patient
     */
    private void setAddresses(Patient patient) {
        SessionWrapper sessionWrapper = getCurrentSourceSession();
        try {
            Query getAddresses = sessionWrapper.getNamedQuery("getAddresses");
            getAddresses.setParameter("patientId", patient.getId());
            List addressObjects = getAddresses.list();
            addressObjects.forEach(object -> {
                Object[] addressRow = (Object[]) object;
                String addressStr = clobHelper.getStringFromExpectedClob(addressRow[0]);
                String postCode = "";
                if (addressRow.length > 1) {
                    postCode = clobHelper.getStringFromExpectedClob(addressRow[1]);
                }
                PatientAddress address = PatientAddress.newAddressPostCode(addressStr, postCode);
                patient.addAddress(address);
            });
        } catch (Exception ex) {
            logger.warn("Error while loading addresses of patient " + patient.getId() + ". Does the specified address table exist? " + ex.getMessage());
        } finally {
            sessionWrapper.closeSession();
        }
    }

    /**
     * Fetches the names of the patient from the DB and assigns them to the given patient.
     *
     * @param patient
     */
    private void setNames(Patient patient) {
        SessionWrapper sessionWrapper = getCurrentSourceSession();
        try {
            Query getForeNames = sessionWrapper.getNamedQuery("getPatientNames");
            getForeNames.setParameter("patientId", patient.getId());
            List foreNames = getForeNames.list();
            if (CollectionUtils.isEmpty(foreNames)) {
                logger.warn("!! No name/surname was found for patient with id " + patient.getId());
                return;
            }
            for (Object dbObject : foreNames) {
                Object[] namePair = (Object[]) dbObject;
                String name = clobHelper.getStringFromExpectedClob(namePair[0]);
                String lastName = clobHelper.getStringFromExpectedClob(namePair[1]);
                if (! StringUtils.isBlank(name)) {
                    patient.addForeName(name);
                }
                if (! StringUtils.isBlank(lastName)) {
                    patient.addSurname(lastName);
                }
            }
        } catch (Exception ex) {
            logger.warn("Error while loading names of patient " + patient.getId() + ". Does the specified address table exist? " + ex.getMessage());
        } finally {
            sessionWrapper.closeSession();
        }
    }

}
