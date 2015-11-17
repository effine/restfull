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
public class UserModifyRest implements IRestService {

    public RestType getType() {
        return RestType.POST;
    }

    public String getURI() {
        return "/rest/user.modify";
    }

    public void service(IRestRequest request, IRestResponse response) throws RestException {
        //构造一个返回的Reponse
        Response responseData = new Response();
        try {
            //读取参数或者cookie或者别的之类的
            String uid = null;
            Cookie[] cookies = request.getHttpRequest().getCookies();
            for (int i = 0; cookies != null && i < cookies.length; i++) {
                if ("uid".equals(cookies[i].getValue())) {
                    uid = cookies[i].getValue();
                    break;
                }
            }
            if (ValidateUtil.isNull(uid)) {
                throw new Exception("尚未登录");
            }
            //---执行其他逻辑

            //............更新成功
            responseData.addMessage("doAction", "修改成功");
        } catch (Exception e) {
            responseData.addError("doAction", e.getMessage());
        }
        //将结果写到http里
        response.writeReponse(responseData);
    }
}
