package weibo.weibo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import weibo.weibo.async.EventModel;
import weibo.weibo.async.EventProducer;
import weibo.weibo.async.EventType;
import weibo.weibo.model.EntityType;
import weibo.weibo.model.News;
import weibo.weibo.model.UserHolder;
import weibo.weibo.service.LikeService;
import weibo.weibo.service.NewsService;
import weibo.weibo.util.WeiboUtil;

@Controller
public class LikeController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserHolder userHolder;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(int newId) {
        long likeCount = likeService.like(userHolder.getUser().getId(), newId, EntityType.ENTITY_NEWS);
        newsService.updateLikeCount(newId, (int) likeCount);
        News news = newsService.getNews(newId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(userHolder.getUser().getId())
                .setEntityId(newId)
                .setEntityOwnerId(news.getUserId()));
        return WeiboUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String disLike(int newId) {
        long likeCount = likeService.disLike(userHolder.getUser().getId(), newId, EntityType.ENTITY_NEWS);
        newsService.updateLikeCount(newId, (int) likeCount);
        return WeiboUtil.getJSONString(0, String.valueOf(likeCount));
    }

}
