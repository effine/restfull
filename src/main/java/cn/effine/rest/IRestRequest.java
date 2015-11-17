package cn.effine.rest;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author effine
 */
public interface IRestRequest {

	IRestService getRestService();

	public HttpServletRequest getHttpRequest();
}
