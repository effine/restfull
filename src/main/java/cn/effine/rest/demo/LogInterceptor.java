package cn.effine.rest.demo;

import cn.effine.rest.IRestInterceptor;
import cn.effine.rest.IRestRequest;
import cn.effine.rest.IRestResponse;
import cn.effine.rest.RestException;

/**
 *
 * 测试日志拦截器
 */
public class LogInterceptor implements IRestInterceptor {

    public void handleRest(IRestRequest request, IRestResponse response) throws RestException {
        System.out.println("拦截Rest请求.URI=" + request.getRestService().getURI());
    }
}
