package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.commandline;


public class CommandLineArgHolder {

    private boolean noPseudonym;

    private boolean instantOCR;

    public boolean isNoPseudonym() {
        return noPseudonym;
    }

    public void setNoPseudonym(boolean noPseudonym) {
        this.noPseudonym = noPseudonym;
    }

    public boolean isInstantOCR() {
        return instantOCR;
    }

    public void setInstantOCR(boolean instantOCR) {
        this.instantOCR = instantOCR;
    }

}
