package cn.effine.rest;

/**
 *
 * Rest服务
 */
public interface IRestService {

	public static enum RestType {

		POST, GET, PUT, DELETE, OTHERS
	};

	public String getURI();

	public RestType getType();

	public void service(IRestRequest request, IRestResponse response)
			throws RestException;
}
