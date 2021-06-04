package weibo.weibo.model.RetObject;

import weibo.weibo.model.Message;
import weibo.weibo.model.VoObject;

public class MsgRet implements VoObject {
    private Message message;

    private String fromUserName;

    private String fromHeadUrl;

    private String toUserName;

    private String toHeadUrl;

    private int totalCount;

    public MsgRet(Message message,String fromHeadUrl,String fromUserName,String toHeadUrl,String toUserName,int totalCount){
        this.message=message;
        this.fromHeadUrl=fromHeadUrl;
        this.fromUserName=fromUserName;
        this.toHeadUrl=toHeadUrl;
        this.toUserName=toUserName;
        this.totalCount=totalCount;
    }

    @Override
    public Object createVo() {
        MsgRetVo msgRetVo=new MsgRetVo();
        msgRetVo.setMessage(message);
        msgRetVo.setFromHeadUrl(fromHeadUrl);
        msgRetVo.setFromUserName(fromUserName);
        msgRetVo.setToHeadUrl(toHeadUrl);
        msgRetVo.setToUserName(toUserName);
        msgRetVo.setTotalCount(totalCount);

        return msgRetVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
