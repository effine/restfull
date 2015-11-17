package cn.effine.rest;

import javax.servlet.http.HttpServletResponse;

import cn.effine.web.Response;

/**
 *
 * @author effine
 */
public interface IRestResponse {

	IRestService getRestService();

	public HttpServletResponse getHttpResponse();

	public void writeReponse(Response response) throws RestException;
}
