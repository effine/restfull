package cn.effine.rest.support;

import javax.servlet.http.HttpServletRequest;

import cn.effine.rest.IRestRequest;
import cn.effine.rest.IRestService;

/**
 *
 * @author effine
 */
public class GenericRestRequest implements IRestRequest {

	private HttpServletRequest httpRequest;
	private IRestService restService;

	public IRestService getRestService() {
		return restService;
	}

	public GenericRestRequest(HttpServletRequest request) {
		this.httpRequest = request;
	}

	public HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	public void setRestService(IRestService restService) {
		this.restService = restService;
	}
}
