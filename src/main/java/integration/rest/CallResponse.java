package integration.rest;

public class CallResponse {

  private String ccdrId;
  private String callerName;
  private String calledName;
  private String globalCallID;
  private String callerNumber;
  private String calledNumber;


  public String getCcdrId() {
    return ccdrId;
  }

  public void setCcdrId(String ccdrId) {
    this.ccdrId = ccdrId;
  }

  public String getCallerName() {
    return callerName;
  }

  public void setCallerName(String callerName) {
    this.callerName = callerName;
  }

  public String getCalledName() {
    return calledName;
  }

  public void setCalledName(String calledName) {
    this.calledName = calledName;
  }

  public String getGlobalCallID() {
    return globalCallID;
  }

  public void setGlobalCallID(String globalCallID) {
    this.globalCallID = globalCallID;
  }

  public String getCallerNumber() {
    return callerNumber;
  }

  public void setCallerNumber(String callerNumber) {
    this.callerNumber = callerNumber;
  }

  public String getCalledNumber() {
    return calledNumber;
  }

  public void setCalledNumber(String calledNumber) {
    this.calledNumber = calledNumber;
  }

}
