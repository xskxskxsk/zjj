package weibo.weibo.model.RetObject;

import weibo.weibo.model.News;
import weibo.weibo.model.User;
import weibo.weibo.model.VoObject;

import java.util.Date;
import java.util.List;

public class NewsRet implements VoObject {
    private int id;

    private String image;

    private String title;

    private int likeCount;

    private int CommentCount;

    private List<CommentRet> commentList;

    private User user;

    private Date createTime;

    public NewsRet(int likeCount, List<CommentRet> commentList, News news, User user,int commentCount){
        this.id=news.getId();
        this.likeCount=likeCount;
        this.commentList=commentList;
        this.image=news.getImage();
        this.title=news.getTitle();
        this.user=user;
        this.createTime=news.getCreatedTime();
        this.CommentCount=commentCount;
    }

    @Override
    public Object createVo() {
        NewsRetVo newsRetVo=new NewsRetVo();
        newsRetVo.setId(id);
        newsRetVo.setImage(image);
        newsRetVo.setLikeCount(likeCount);
        newsRetVo.setTitle(title);
        newsRetVo.setCommentList(commentList);
        newsRetVo.setUser(user);
        newsRetVo.setCommentCount(CommentCount);
        newsRetVo.setCreateTime(createTime);

        return newsRetVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
