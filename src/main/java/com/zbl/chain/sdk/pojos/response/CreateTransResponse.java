package com.zbl.chain.sdk.pojos.response;


import com.zbl.chain.sdk.pojos.response.data.CreateTransData;

public class CreateTransResponse extends BaseResponse {

  private CreateTransData data;

  public CreateTransData getData() {
    return data;
  }

  public void setData(CreateTransData data) {
    this.data = data;
  }
}
