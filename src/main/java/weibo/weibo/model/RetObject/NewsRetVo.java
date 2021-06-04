package weibo.weibo.model.RetObject;

import weibo.weibo.model.User;

import java.util.Date;
import java.util.List;

public class NewsRetVo {
    private int id;

    private String image;

    private String title;

    private int likeCount;

    private int commentCount;

    private List<CommentRet> commentList;

    private User user;

    private String createTime;

    public int getId(){return id;}

    public void setId(int id){this.id=id;}

    public String getImage(){return image;}

    public void setImage(String image){this.image=image;}

    public String getTitle(){return title;}

    public void setTitle(String title){this.title=title;}

    public List<CommentRet> getCommentList(){return commentList;}

    public void setCommentList(List<CommentRet> commentList){this.commentList=commentList;}

    public User getUser(){return user;}

    public void setUser(User user){this.user=user;}

    public int getLikeCount(){return likeCount;}

    public void setLikeCount(int likeCount){this.likeCount=likeCount;}

    public int getCommentCount(){return commentCount;}

    public void setCommentCount(int commentCount){this.commentCount=commentCount;}

    public String getCreateTime(){return createTime;}

    public void setCreateTime(String createTime){this.createTime=createTime;}
}
