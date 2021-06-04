package weibo.weibo.async;



import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import weibo.weibo.controller.LoginController;
import weibo.weibo.dao.NewsDao;
import weibo.weibo.service.LikeService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 点赞的定时任务
 */
@Slf4j
public class LikeTask extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(LikeTask.class);
    @Autowired
    LikeService likeService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        logger.info("LikeTask-------- {}", sdf.format(new Date()));

        //将 Redis 里的点赞信息同步到数据库里
        //likedService.transLikedFromRedis2DB();
        likeService.transLikedCountFromRedis();
    }
}

