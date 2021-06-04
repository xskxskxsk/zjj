package weibo.weibo;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.RequestResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import weibo.weibo.controller.LoginController;
import weibo.weibo.util.JwtHelper;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = WeiboApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
//@Rollback(false)
public class ControllerTest {
    @Autowired
    LoginController loginController;

    @Autowired
    private MockMvc mvc;


    private final String creatTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        return token;
    }

    @Test
    public void registerTest()throws Exception{

        String expectedResponse="";
        String responseString=null;

        try{
            responseString=this.mvc.perform(post("/reg?username=zzx&password=123456"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"code\":0,\"msg\":\"注册成功\"}";

        try{
            JSONAssert.assertEquals(expectedResponse,responseString,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void loginTest()throws Exception{

        String expectedResponse="";
        String responseString=null;

        try{
            responseString=this.mvc.perform(get("/login?username=xliiin&password=123456"))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"code\":0,\"msg\":\"登录成功\"}";

        JSONObject jsonObect = new JSONObject(responseString);
        String token=jsonObect.getString("data");

//        try{
//            JSONAssert.assertEquals(expectedResponse,responseString,false);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }



        try{
            responseString=this.mvc.perform(post("/user/addNews?image=123.jpg&title=1&link=1").header("authorization",token))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"code\":0,\"msg\":\"发布成功\"}";
        try{
            JSONAssert.assertEquals(expectedResponse,responseString,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void uploadImgTest()throws Exception{
        String token = creatTestToken(60L, 0L, 100);

        String expectedResponse="";
        String responseString=null;

        File file = new File("."+File.separator + "src" + File.separator + "test" + File.separator+"resources" + File.separator + "img" + File.separator+"timg.png");
        MockMultipartFile firstFile = new MockMultipartFile("img", "timg.png" , "multipart/form-data", new FileInputStream(file));


        try{
            responseString=this.mvc.perform(MockMvcRequestBuilders
                    .multipart("/image")
                    .file(firstFile)
                    .header("authorization",token))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"code\":0,\"msg\":\"发布成功\"}";
        try{
            JSONAssert.assertEquals(expectedResponse,responseString,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getNewsTest()throws Exception{
        String token = creatTestToken(60L, 0L, 100);

        String expectedResponse="";
        String responseString=null;

        try{
            responseString=this.mvc.perform(get("/news/44").header("authorization",token))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"errno\":0,\"data\":{\"id\":44,\"image\":\"123.jpg\",\"title\":\"1\",\"likeCount\":1,\"commentCount\":1,\"commentList\":[{\"commentId\":1,\"content\":\"你好\",\"entityId\":44,\"status\":0,\"userName\":\"ssm\",\"urlHead\":\"http://images.tyella.com/head/1t.png\"}],\"user\":{\"id\":60,\"name\":\"xliiin\",\"headUrl\":\"http://images.tyella.com/head/1t.png\"}},\"errmsg\":\"成功\"}";
        try{
            JSONAssert.assertEquals(expectedResponse,responseString,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getAllNewsTest()throws Exception{
        String token = creatTestToken(60L, 0L, 100);

        String expectedResponse="";
        String responseString=null;

        try{
            responseString=this.mvc.perform(get("/news").header("authorization",token))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"errno\":0,\"data\":[{\"id\":44,\"image\":\"123.jpg\",\"title\":\"1\",\"likeCount\":1,\"commentCount\":1,\"user\":{\"id\":60,\"name\":\"xliiin\",\"headUrl\":\"http://images.tyella.com/head/1t.png\"}}],\"errmsg\":\"成功\"}";
        try{
            JSONAssert.assertEquals(expectedResponse,responseString,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMyNewsTest()throws Exception{
        String token = creatTestToken(60L, 0L, 100);

        String expectedResponse="";
        String responseString=null;

        try{
            responseString=this.mvc.perform(get("/user/news").header("authorization",token))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"errno\":0,\"data\":[{\"id\":44,\"image\":\"123.jpg\",\"title\":\"1\",\"likeCount\":1,\"commentCount\":1,\"user\":{\"id\":60,\"name\":\"xliiin\",\"headUrl\":\"http://images.tyella.com/head/1t.png\"}}],\"errmsg\":\"成功\"}";
        try{
            JSONAssert.assertEquals(expectedResponse,responseString,false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addMessageTest()throws Exception {
        String token = creatTestToken(60L, 0L, 100);

        String expectedResponse = "";
        String responseString = null;

        try {
            responseString = this.mvc.perform(post("/msg/addMessage/62?content=你好").header("authorization",token))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/plain;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"code\":0,\"msg\":\"发送成功\"}";

        try {
            JSONAssert.assertEquals(expectedResponse, responseString, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void addCommentTest()throws Exception {
        String token = creatTestToken(61L, 0L, 100);

        String expectedResponse = "";
        String responseString = null;

        try {
            responseString = this.mvc.perform(post("/user/addComment/44?content=你好").header("authorization",token))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"code\":0,\"msg\":\"发布评论成功\"}";

        try {
            JSONAssert.assertEquals(expectedResponse, responseString, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getMessageTest()throws Exception {
        String token = creatTestToken(60L, 0L, 100);

        String expectedResponse = "";
        String responseString = null;

        try {
            responseString = this.mvc.perform(get("/msg/xliiin_ssm").header("authorization",token))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"errno\":0,\"data\":[{\"message\":{\"id\":4,\"content\":\"你好\",\"conversationId\":\"xliiin_ssm\"},\"fromHeadUrl\":\"http://images.tyella.com/head/1t.png\",\"fromUserName\":\"xliiin\",\"toHeadUrl\":\"http://images.tyella.com/head/1t.png\",\"toUserName\":\"ssm\"}],\"errmsg\":\"成功\"}";

        try {
            JSONAssert.assertEquals(expectedResponse, responseString, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMessageListTest()throws Exception {
        String token = creatTestToken(60L, 0L, 100);

        String expectedResponse = "";
        String responseString = null;

        try {
            responseString = this.mvc.perform(get("/msg/list").header("authorization",token))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"errno\":0,\"data\":[{\"message\":{\"id\":4,\"content\":\"你好\",\"conversationId\":\"xliiin_ssm\"},\"toHeadUrl\":\"http://images.tyella.com/head/1t.png\",\"toUserName\":\"ssm\",\"totalCount\":3},{\"message\":{\"id\":10,\"content\":\"你好\",\"conversationId\":\"xliiin_zzx\"},\"toHeadUrl\":\"http://images.tyella.com/head/1t.png\",\"toUserName\":\"zzx\",\"totalCount\":1}],\"errmsg\":\"成功\"}";

        try {
            JSONAssert.assertEquals(expectedResponse, responseString, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void addLikeTest()throws Exception {
        String token = creatTestToken(60L, 0L, 100);

        String expectedResponse = "";
        String responseString = null;

        try {
            responseString = this.mvc.perform(post("/like?newsId=44").header("authorization",token))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/plain;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        expectedResponse = "{\"code\":0,\"msg\":\"1\"}";

        try {
            JSONAssert.assertEquals(expectedResponse, responseString, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
