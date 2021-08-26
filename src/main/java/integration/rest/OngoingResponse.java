package integration.rest;

public class OngoingResponse {

    private String ccdrid;
    private String globalCallID;
    private boolean error = false;
    private String errorReason;

    public String getCcdrid() {
        return ccdrid;
    }

    public void setCcdrid(String ccdrid) {
        this.ccdrid = ccdrid;
    }

    public String getGlobalCallID() {
        return globalCallID;
    }

    public void setGlobalCallID(String globalCallID) {
        this.globalCallID = globalCallID;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }
}
