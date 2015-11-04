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



package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.anonymisation;


import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.model.Patient;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
public class NamePseudonymiser extends Pseudonymiser {

    @Override
    public String getJsonRuleFilePath() {
        return "anonymisation/nameRules.vm";
    }

    @Override
    public boolean canIgnore(Patient patient) {
        return CollectionUtils.isEmpty(patient.getForeNames()) && CollectionUtils.isEmpty(patient.getSurnames());
    }

}
