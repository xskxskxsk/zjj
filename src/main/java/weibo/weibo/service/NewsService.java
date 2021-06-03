package weibo.weibo.service;


import org.springframework.beans.factory.annotation.Value;
import weibo.weibo.dao.NewsDao;
import weibo.weibo.model.Image;
import weibo.weibo.model.News;
import weibo.weibo.util.ResponseCode;
import weibo.weibo.util.ReturnObject;
import weibo.weibo.util.WeiboUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import weibo.weibo.util.MD5;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {

    @Value("${Weibo.img.path}")
    private String imgPath;

    @Autowired
    private NewsDao newsDao;

    public int addNews(News news) {
        newsDao.addNews(news);
        return news.getId();
    }

    public News getNews(int newsId) {
        return newsDao.selectById(newsId);
    }

    public List<News> getLastedNews(int id, int offset, int limit) {
        return newsDao.selectByUserIdAndOffset(id, offset, limit);
    }

    public int updateCommentCount(int id, int count) {
        return newsDao.updateCommentCount(id, count);
    }

    public int updateLikeCount(int id, int count) {
        return newsDao.updateLikeCount(id, count);
    }

    /**
     * 上传一张图片
     */
    //TODO
    //MultipartFile上传文件
    public String saveImage(MultipartFile multipartFile) throws IOException {
        //dotPos "."在文件名中的位置
        int dotPos = multipartFile.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }
        //文件扩展名
        String fileExt = multipartFile.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if (!WeiboUtil.isFillAllowed(fileExt)) {
            return null;
        }
        //文件名
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        Files.copy(multipartFile.getInputStream(), new File(WeiboUtil.IMAGE_DIR + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return WeiboUtil.WEIBO_DOMIN + "image?name=" + fileName;
    }

    /**
     * 上传多张图片
     */
    public String saveImages(MultipartFile[] multipartFiles) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (MultipartFile multipartFile : multipartFiles) {
            String fileUrl = saveImage(multipartFile);
            sb.append(",");
            sb.append(fileUrl);
        }
        return sb.toString();
    }

    public ReturnObject<String> uploadFile(int userId, MultipartFile img){
        String imgMD5 = MD5.getFileMd5(img);
        System.out.println("MD5: " + imgMD5);

        String fileName = savaImgToDisk(img);
        if(fileName == null){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        System.out.println("FileName: " + fileName);
        Image image = new Image(userId, imgMD5, fileName);
        return new ReturnObject(newsDao.insertImage(image));
    }

    public String savaImgToDisk(MultipartFile img){
        try {
            String realPath = imgPath + "/" + UUID.randomUUID().toString().replace("-", "")+img.getOriginalFilename().substring(img.getOriginalFilename().lastIndexOf("."));
            File dest = new File(realPath);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdir();
            }
            img.transferTo(dest);
            return dest.getName();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
