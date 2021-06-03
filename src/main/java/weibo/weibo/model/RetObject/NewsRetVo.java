package weibo.weibo.model.RetObject;

import weibo.weibo.model.User;
import weibo.weibo.model.ViewObject;

import java.util.List;

public class NewsRetVo {
    private int id;

    private String image;

    private String title;

    private int likeCount;

    private List<ViewObject> commentList;

    private User user;

    public int getId(){return id;}

    public void setId(int id){this.id=id;}

    public String getImage(){return image;}

    public void setImage(String image){this.image=image;}

    public String getTitle(){return title;}

    public void setTitle(String title){this.title=title;}

    public List<ViewObject> getCommentList(){return commentList;}

    public void setCommentList(List<ViewObject> commentList){this.commentList=commentList;}

    public User getUser(){return user;}

    public void setUser(User user){this.user=user;}

    public int getLikeCount(){return likeCount;}

    public void setLikeCount(int likeCount){this.likeCount=likeCount;}
}
