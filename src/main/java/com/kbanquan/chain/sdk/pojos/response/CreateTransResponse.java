package com.kbanquan.chain.sdk.pojos.response;


import com.kbanquan.chain.sdk.pojos.response.data.CreateTransData;

public class CreateTransResponse extends BaseResponse {

  private CreateTransData data;

  public CreateTransData getData() {
    return data;
  }

  public void setData(CreateTransData data) {
    this.data = data;
  }
}
