package cn.effine.rest.demo;

import javax.servlet.http.Cookie;

import cn.effine.rest.IRestRequest;
import cn.effine.rest.IRestResponse;
import cn.effine.rest.IRestService;
import cn.effine.rest.RestException;
import cn.effine.util.ValidateUtil;
import cn.effine.web.Response;

/**
 *
 * @author effine
 */
public class UserLoginRest implements IRestService {

    public RestType getType() {
        return RestType.POST;
    }

    public String getURI() {
        return "/rest/user.login";
    }

    public void service(IRestRequest request, IRestResponse response) throws RestException {
        //构造一个返回的Reponse
        Response responseData = new Response();
        try {
            //读取参数或者cookie或者别的之类的
            String userName = request.getHttpRequest().getParameter("username");
            String password = request.getHttpRequest().getParameter("password");
            if (ValidateUtil.isNull(userName) || ValidateUtil.isNull(password)) {
                throw new Exception("帐号或者密码不能为空");
            }
            if (!"neil".equals(userName) && !"mao".equals(password)) {
                throw new Exception("帐号或者密码不对");
            }
            responseData.addMessage("doAction", "恭喜，登录成功");
            //这里可以把cookie或者别的信息写到response 里
            Cookie cookie = new Cookie("uid", "onlyTest");
            response.getHttpResponse().addCookie(cookie);
        } catch (Exception e) {
            responseData.addError("doAction", e.getMessage());
        }
        //将结果写到http里
        response.writeReponse(responseData);
    }
}
