package weibo.weibo.model.RetObject;

import weibo.weibo.model.Message;

public class MsgRetVo {
    private Message message;

    private String fromUserName;

    private String fromHeadUrl;

    private String toUserName;

    private String toHeadUrl;

    private int totalCount;

    public Message getMessage(){return message;}

    public void setMessage(Message message){this.message=message;}

    public String getFromUserName(){return fromUserName;}

    public void setFromUserName(String fromUserName){this.fromUserName=fromUserName;}

    public String getFromHeadUrl(){return fromHeadUrl;}

    public void setFromHeadUrl(String fromHeadUrl){this.fromHeadUrl=fromHeadUrl;}

    public String getToUserName(){return toUserName;}

    public void setToUserName(String toUserName){this.toUserName=toUserName;}

    public String getToHeadUrl(){return toHeadUrl;}

    public void setToHeadUrl(String toHeadUrl){this.toHeadUrl=toHeadUrl;}

    public int getTotalCount(){return totalCount;}

    public void setTotalCount(int totalCount){this.totalCount=totalCount;}
}
