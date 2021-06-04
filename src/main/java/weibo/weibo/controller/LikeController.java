package weibo.weibo.controller;


import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import weibo.weibo.annotation.Audit;
import weibo.weibo.annotation.LoginUser;
import weibo.weibo.async.EventModel;
import weibo.weibo.async.EventProducer;
import weibo.weibo.async.EventType;
import weibo.weibo.async.MqProducer;
import weibo.weibo.model.EntityType;
import weibo.weibo.model.News;
import weibo.weibo.model.UserHolder;
import weibo.weibo.service.LikeService;
import weibo.weibo.service.NewsService;
import weibo.weibo.util.WeiboUtil;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LikeController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserHolder userHolder;

    @Autowired
    private EventProducer eventProducer;
    @Autowired
    private MqProducer mqProducer;
    @ApiOperation(value = "点赞", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="newsId", required = true, dataType="Integer")
    })
    @Audit
    @PostMapping("/like")
    public String like(@LoginUser @ApiIgnore @RequestParam(required = false) Long userId,@RequestParam(required =false ) int newsId) {
        long likeCount = likeService.like(Math.toIntExact(userId), newsId, EntityType.ENTITY_NEWS);
        //newsService.updateLikeCount(newsId, (int) likeCount);//需要异步写回
        //boolean mqResult = mqProducer.asyncLike(newsId, (int) likeCount);
        News news = newsService.getNews(newsId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(Math.toIntExact(userId))
                .setEntityId(newsId)
                .setEntityOwnerId(news.getUserId()));
        return WeiboUtil.getJSONString(0, String.valueOf(likeCount));
    }

//    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
//    @ResponseBody
//    public String disLike(int newId) {
//       // long likeCount = likeService.disLike(userHolder.getUser().getId(), newId, EntityType.ENTITY_NEWS);
//        newsService.updateLikeCount(newId, (int) likeCount);
//        return WeiboUtil.getJSONString(0, String.valueOf(likeCount));
//    }

}
