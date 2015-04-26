package uk.ac.kcl.iop.brc.core.pipeline.common.service;

import net.sf.jmimemagic.*;
import org.springframework.stereotype.Service;

@Service
public class FileTypeService {

    public String getType(byte[] data) throws MagicParseException, MagicException, MagicMatchNotFoundException {
        MagicMatch match = Magic.getMagicMatch(data);
        return match.getMimeType();
    }

    public boolean isPDF(byte[] data) {
        return isDataTypeEqualTo(data, "application/pdf");
    }

    private boolean isDataTypeEqualTo(byte[] data, String type) {
        try {
            return getType(data).equalsIgnoreCase(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



}
