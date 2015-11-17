package cn.effine.rest;

/**
 *
 * Rest 拦截器
 */
public interface IRestInterceptor {

	public void handleRest(IRestRequest request, IRestResponse response)
			throws RestException;
}
