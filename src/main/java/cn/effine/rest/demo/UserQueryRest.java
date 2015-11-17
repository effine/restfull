package cn.effine.rest.demo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.effine.rest.IRestRequest;
import cn.effine.rest.IRestResponse;
import cn.effine.rest.IRestService;
import cn.effine.rest.RestException;
import cn.effine.web.Response;

/**
 *
 * @author effine
 */
public class UserQueryRest implements IRestService {

    public RestType getType() {
        return RestType.GET;
    }

    public String getURI() {
        return "/rest/user.query";
    }

    public void service(IRestRequest request, IRestResponse response) throws RestException {
        //构造一个返回的Reponse
        Response responseData = new Response();
        try {
            //执行查询
            JSONArray array = new JSONArray();
            //
            for (int i = 0; i < 5; i++) {
                JSONObject user = new JSONObject();
                user.element("name", "user" + i);
                array.add(user);
            }
            responseData.addMessage("list", array);
        } catch (Exception e) {
            responseData.addError("doAction", e.getMessage());
        }
        //将结果写到http里
        response.writeReponse(responseData);
    }
}
