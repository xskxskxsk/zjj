package weibo.weibo.controller;

import io.swagger.annotations.*;
import springfox.documentation.annotations.ApiIgnore;
import weibo.weibo.annotation.Audit;
import weibo.weibo.annotation.LoginUser;
import weibo.weibo.model.*;
import weibo.weibo.model.RetObject.CommentRet;
import weibo.weibo.model.RetObject.NewsRet;
import weibo.weibo.service.*;
import weibo.weibo.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;

    @Autowired
    LikeService likeService;

    @Autowired
    UserService userService;

    @Autowired
    UserHolder userHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    AliService aliService;

    @Autowired
    private JedisUtil jedisUtil;
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization",value = "Token",required = true)
    })
    @Audit
    @ResponseBody
    @GetMapping("news")
    public Object newsAll(@LoginUser @ApiIgnore @RequestParam(required = false) Long userId) {
        List<News> newsList = newsService.getAllNews();
        List<NewsRet> newsRetList=new ArrayList<>();
        if (newsList != null) {
            for(News news:newsList) {
                int likeCount = likeService.getLikeStatus(userId.intValue(), news.getId(), EntityType.ENTITY_NEWS);

                List<CommentRet> commentVOs = new ArrayList<>();
                List<Comment> comments = commentService.getCommentByEntity(news.getId(), EntityType.ENTITY_COMMENT);

                for (Comment comment : comments) {
                    CommentRet vo = new CommentRet(comment,userService.getUser(comment.getUserId()));
                    commentVOs.add(vo);
                }

                User user=userService.getUser(news.getUserId());
                NewsRet newsRet=new NewsRet(likeCount,commentVOs,news,user,comments.size());
                newsRetList.add(newsRet);
            }
        }

        ReturnObject returnObject=new ReturnObject(newsRetList);
        return Common.getListRetObject(returnObject);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization",value = "Token",required = true)
    })
    @Audit
    @ResponseBody
    @GetMapping("user/news")
    public Object myNews(@LoginUser @ApiIgnore @RequestParam(required = false) Long userId) {
        List<News> newsList = newsService.getMyNews(userId.intValue());
        List<NewsRet> newsRetList=new ArrayList<>();
        if (newsList != null) {
            for(News news:newsList) {
                int likeCount = likeService.getLikeStatus(userId.intValue(), news.getId(), EntityType.ENTITY_NEWS);

                List<CommentRet> commentVOs = new ArrayList<>();
                List<Comment> comments = commentService.getCommentByEntity(news.getId(), EntityType.ENTITY_COMMENT);

                for (Comment comment : comments) {
                    CommentRet vo = new CommentRet(comment,userService.getUser(comment.getUserId()));
                    commentVOs.add(vo);
                }

                User user=userService.getUser(news.getUserId());
                NewsRet newsRet=new NewsRet(likeCount,commentVOs,news,user,comments.size());
                newsRetList.add(newsRet);
            }
        }

        ReturnObject returnObject=new ReturnObject(newsRetList);
        return Common.getListRetObject(returnObject);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization",value = "Token",required = true),
            @ApiImplicitParam(paramType = "path",dataType = "int",name = "newsId",value = "资讯id",required = true)
    })
    @Audit
    @ResponseBody
    @GetMapping("news/{newsId}")
    public Object newsDetail(@PathVariable("newsId") int newsId,@LoginUser @ApiIgnore @RequestParam(required = false) Long userId) {
        News news = newsService.getNews(newsId);
        int likeCount=0;
        List<CommentRet> commentVOs = new ArrayList<>();
        if (news != null) {
//            int localUserId = userHolder.getUser() != null ? userHolder.getUser().getId() : 0;
//            if (localUserId != 0) {
            //用户已登陆
            likeCount=likeService.getLikeStatus(userId.intValue(), news.getId(), EntityType.ENTITY_NEWS);
//            } else {
//                //用户未登陆
//                model.addAttribute("like", 0);
//            }
            //评论
            List<Comment> comments = commentService.getCommentByEntity(news.getId(), EntityType.ENTITY_COMMENT);

            for (Comment comment : comments) {
                CommentRet vo = new CommentRet(comment,userService.getUser(comment.getUserId()));
                commentVOs.add(vo);
            }
        }
        User user=userService.getUser(news.getUserId());
        NewsRet newsRet=new NewsRet(likeCount,commentVOs,news,user,commentVOs.size());
        ReturnObject returnObject=new ReturnObject(newsRet);
        return Common.getRetObject(returnObject);
    }


    //todo
    //put post 区别
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization",value = "Token",required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "image", value = "图片", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "title", value = "文本", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "link", value = "链接", required = false),
    })
    @Audit
    @PostMapping("user/addNews")
    @ResponseBody
    public String addNews(@LoginUser @ApiIgnore @RequestParam(required = false) Long userId,
                          @RequestParam(value = "image",required = false) String image,
                          @RequestParam(value = "title",required = false) String title,
                          @RequestParam(value = "link",required = false) String link) {
        try{
            News news= new News();
            news.setImage(image);
            news.setTitle(title);
            news.setLink(link);
            news.setCreatedTime(new Date());
//            if(userHolder.getUser()!=null){
//                news.setUserId(userHolder.getUser().getId());
//            }else{
//                //设置一个匿名用户
//                news.setUserId(3);
//            }
            news.setUserId(userId.intValue());
            newsService.addNews(news);
            return WeiboUtil.getJSONString(0,"发布成功");
        }catch (Exception e){
            logger.error("添加资讯失败"+e.getMessage());
            return WeiboUtil.getJSONString(1,"发布失败");
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",dataType = "String",name = "authorization",value = "Token",required = true),
            @ApiImplicitParam(paramType = "path",dataType = "int",name = "newsId",value = "资讯id",required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "content", value = "评论内容", required = false)
    })
    @Audit
    @ResponseBody
    @PostMapping("user/addComment/{newsId}")
    public Object addComment(@LoginUser @ApiIgnore @RequestParam(required = false) Long userId,@PathVariable("newsId") int newsId,@RequestParam("content") String content){
        try{
            Comment comment=new Comment();
            comment.setUserId(userId.intValue());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_COMMENT);
            comment.setCreateTime(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);

            //todo
            //更新评论数量，以后用异步实现
            int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            String likeKey = RedisKeyUtil.getCommentKey(newsId);
            jedisUtil.set(likeKey, String.valueOf(count));
            //newsService.updateCommentCount(comment.getEntityId(),count);
        }catch(Exception e){
            logger.error("提交评论错误",e.getMessage());
        }
        return WeiboUtil.getJSONString(0,"发布评论成功");
    }

    @RequestMapping("/uploadImage/")
    @ResponseBody
    public String upLoadOSS(MultipartFile file){
        try{
            String fileUrl=aliService.saveImage(file);
            if(fileUrl==null){
                return WeiboUtil.getJSONString(1,"上传图片失败");
            }
            return WeiboUtil.getJSONString(0,fileUrl);
        }catch(IOException e){
            logger.error("上传失败"+e.getMessage());
            return WeiboUtil.getJSONString(1,"上传失败");
        }
    }


    //todo
    //HttpServletResponse   StreamUtils.copy
    @RequestMapping(path = {"/image/"},method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName, HttpServletResponse response) {
        try {
            response.setContentType("image/jpg");
            StreamUtils.copy(new FileInputStream(new File(WeiboUtil.IMAGE_DIR + imageName)), response.getOutputStream());
        } catch (Exception e) {
            logger.error("读取图片错误" + e.getMessage());
        }
    }


    @ApiOperation(value = "上传图片", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "__file", name = "img", value = "图片", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("image")
    public Object uploadFile(@ApiIgnore @LoginUser Long userId,
                             @RequestParam MultipartFile img){
        System.out.println("UserId: " + userId + ", FileSize: " + img.getSize() + ", FileName: " + img.getOriginalFilename());
        ReturnObject<String> returnObject = newsService.uploadFile(userId.intValue(), img);
        if(returnObject.getData() == null){
            return ResponseUtil.fail(returnObject.getCode(), returnObject.getErrmsg());
        }else{
            return ResponseUtil.ok(returnObject.getData());
        }
    }
}
