package weibo.weibo.model.RetObject;

import weibo.weibo.model.News;
import weibo.weibo.model.User;
import weibo.weibo.model.ViewObject;
import weibo.weibo.model.VoObject;

import java.util.List;

public class NewsRet implements VoObject {
    private int id;

    private String image;

    private String title;

    private int likeCount;

    private List<ViewObject> commentList;

    private User user;

    public NewsRet(int likeCount, List<ViewObject> commentList, News news, User user){
        this.id=news.getId();
        this.likeCount=likeCount;
        this.commentList=commentList;
        this.image=news.getImage();
        this.title=news.getTitle();
        this.user=user;
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

        return newsRetVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
