package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.exception;

public class CanNotApplyOCRToNonPDFFilesException extends RuntimeException {

    private String message;

    public CanNotApplyOCRToNonPDFFilesException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
