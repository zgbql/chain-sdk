package com.zbl.chain.sdk;


import com.zbl.chain.sdk.exceptions.ServerException;
import com.zbl.chain.sdk.pojos.payload.CreateTransPayload;
import com.zbl.chain.sdk.utils.FabricUtils;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class FabricClient {

  private static final FabricConfig config = FabricConfig.getInstance();


  private String channelID ;

  private String chaincodeName ;

  private String  version ;

  private String orgName;

  private HFClient hfClient;

  private Channel channel;

  private FabricOrg fabricOrg;

  public FabricClient() throws Exception{
    config.initOrgs();
  }
  public HFClient getHfClient() throws CryptoException, InvalidArgumentException{
    if(null==hfClient){
      hfClient= FabricUtils.getClient(getFabricOrg().getPeerAdmin());
    }
    return hfClient;
  }

  public void setHfClient(HFClient hfClient) {
    this.hfClient = hfClient;
  }

  public Channel getChannel() throws Exception  {
    if(null==channel){
      channel=FabricUtils.getExistingChannel(channelID, getFabricOrg(), getHfClient(), config);


    }
    return channel;
  }

  public void setChannel(Channel channel){
    this.channel = channel;
  }


  public void setChannelID(String channelID){
    this.channelID = channelID;
  }

  public String getChaincodeName(){
    return chaincodeName;
  }

  public void setChaincodeName(String chaincodeName){
    this.chaincodeName = chaincodeName;
  }

  public String getVersion(){
    return version;
  }

  public void setVersion(String version){
    this.version = version;
  }

  public static FabricConfig getConfig(){
    return config;
  }

  public String getOrgName(){
    return orgName;
  }

  public void setOrgName(String orgName){
    this.orgName = orgName;
  }

  public FabricOrg getFabricOrg(){
    if(null==fabricOrg){
      this.fabricOrg = config.getIntegrationSampleOrg(orgName);
    }
    return fabricOrg;
  }

  public void setFabricOrg(FabricOrg fabricOrg){
    this.fabricOrg = fabricOrg;
  }

  public String  sendTransaction(CreateTransPayload createTransPayload) throws Exception{
    Collection<ProposalResponse> successful = new LinkedList<>();
    Collection<ProposalResponse> transactionPropResp = null;

      transactionPropResp = FabricUtils.sendProposalToPeers(getChannel(), getHfClient(),getChaincodeName(),"invoke", new String[]{"put",createTransPayload.getBusinessId(),createTransPayload.getHash()}, config.getProposalWaitTime());

    for (ProposalResponse response : transactionPropResp) {
      if (response.getStatus() == ProposalResponse.Status.SUCCESS) {

        successful.add(response);
      } else {
          throw  new ServerException(response.getMessage(),System.currentTimeMillis());
      }
    }

    return sendTransactionToOrderer(getChannel(), successful);

  }

  public String sendQuery(String key) throws Exception{

    TransactionInfo transactionInfo = getChannel().queryTransactionByID(key);
    return transactionInfo.getTransactionID();




  }

   /**
   * 发送交易到orderer
   */
  protected String sendTransactionToOrderer(Channel channel, Collection<ProposalResponse> successful)
    throws Exception{
    return channel.sendTransaction(successful).thenApply(transactionEvent -> {
      try {
        return transactionEvent.getTransactionID();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }).exceptionally(e -> {
      e.printStackTrace();
      return null;
    }).get(config.getTransactionWaitTime(), TimeUnit.SECONDS);
  }

}
