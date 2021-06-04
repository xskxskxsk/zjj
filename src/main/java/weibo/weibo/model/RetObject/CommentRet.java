package weibo.weibo.model.RetObject;

import weibo.weibo.model.Comment;
import weibo.weibo.model.User;


import java.text.SimpleDateFormat;
import java.util.Date;

public class CommentRet{

    private int commentId;

    private String content;

    private int entityId;

    private String createTime;

    private int status;

    private String userName;

    private String urlHead;

    public CommentRet(Comment comment,User user){
        this.commentId=comment.getId();
        this.content=comment.getContent();
        this.entityId=comment.getEntityId();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        this.createTime=sdf.format(comment.getCreateTime());
        this.status=comment.getStatus();
        this.userName=user.getName();
        this.urlHead=user.getHeadUrl();
    }

    public int getCommentId(){return commentId;}

    public void setCommentId(int commentId){this.commentId=commentId;}

    public String getContent(){return content;}

    public void setContent(String content){this.content=content;}

    public int getEntityId(){return entityId;}

    public void setEntityId(int entityId){this.entityId=entityId;}

    public String getCreateTime(){return createTime;}

    public void setCreateTime(String createTime){this.createTime=createTime;}

    public int getStatus(){return status;}

    public void setStatus(int status){this.status=status;}

    public String getUserName(){return userName;}

    public void setUserName(String userName){this.userName=userName;}

    public String getUrlHead(){return urlHead;}

    public void setUrlHead(String urlHead){this.urlHead=urlHead;}
}
