package cn.effine.rest.support;

import javax.servlet.http.HttpServletResponse;

import cn.effine.rest.IRestResponse;
import cn.effine.rest.IRestService;
import cn.effine.rest.RestException;
import cn.effine.web.Response;

/**
 *
 * @author effine
 */
public class GenericRestResponse implements IRestResponse {

	private HttpServletResponse response;
	private IRestService restService;
	private Response responseData; // 返回的JSON 结果

	public Response getResponseData() {
		return responseData;
	}

	public void setResponseData(Response responseData) {
		this.responseData = responseData;
	}

	public GenericRestResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletResponse getHttpResponse() {
		return response;
	}

	public IRestService getRestService() {
		return restService;
	}

	public void setRestService(IRestService restService) {
		this.restService = restService;
	}

	public void writeReponse(Response response) throws RestException {
		this.responseData = response;
	}
}
