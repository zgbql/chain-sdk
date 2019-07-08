package com.kbanquan.chain.sdk.pojos.payload;


public class CreateTransPayload {


  private String businessId;

  private String hash;

  public String getBusinessId() {
    return businessId;
  }

  public void setBusinessId(String businessId) {
    this.businessId = businessId;
  }

  public String getHash(){
    return hash;
  }

  public void setHash(String hash){
    this.hash = hash;
  }
}
