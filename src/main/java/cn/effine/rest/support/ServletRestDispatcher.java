package cn.effine.rest.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.effine.rest.IRestInterceptor;
import cn.effine.rest.IRestService;
import cn.effine.web.Response;

/**
 *
 * 基于Servlet的实现
 */
public class ServletRestDispatcher extends HttpServlet {

	public Map<String, IRestService> services = new HashMap<String, IRestService>();
	public List<IRestInterceptor> interceptors = new ArrayList<IRestInterceptor>();

	@Override
	public void init(ServletConfig config) throws ServletException {
		// 1.从init里加载相应的Rest服务，如果是有Spring框架或者别的，原理都一样
		String serviceClass = config.getInitParameter("service-class");
		if (serviceClass != null) {
			System.out.println("Rest服务:" + serviceClass);
			String[] classes = serviceClass.split(",");
			try {
				for (String className : classes) {
					Class newClass = Class.forName(className);
					Object newObject = newClass.newInstance();
					if (newObject instanceof IRestService) {
						IRestService restService = (IRestService) newObject;
						services.put(restService.getURI(), restService);
						System.out.println("加载Rest服务:"
								+ newObject.getClass().getName() + ",URI="
								+ restService.getURI());
					}
				}
			} catch (Exception e) {
				System.out.println("加载Rest服务出错:" + e.getMessage());
			}
		}
		// 2.加载拦截器
		String interceptorClas = config.getInitParameter("interceptor-class");
		if (interceptorClas != null) {
			System.out.println("拦截器:" + serviceClass);
			String[] classes = interceptorClas.split(",");
			try {
				for (String className : classes) {
					Class newClass = Class.forName(className);
					Object newObject = newClass.newInstance();
					if (newObject instanceof IRestInterceptor) {
						IRestInterceptor interceptor = (IRestInterceptor) newObject;
						interceptors.add(interceptor);
						System.out.println("加载Rest拦截器:"
								+ newObject.getClass().getName());
					}
				}
			} catch (Exception e) {
				System.out.println("加载Rest拦截器出错:" + e.getMessage());
			}
		}
		//
		super.init(config);
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 处理HTTP请求
		response.setContentType("text/html;charset=GBK");
		response.setCharacterEncoding("GBK");
		Response returnResponse = new Response(); // 最终返回的结果，可以是JSON或者XML格式
		try {
			// 1.获得请求的URI
			String uri = request.getRequestURI();
			// System.out.println("URI===="+uri);
			// 2.获得相应的RestService
			IRestService service = services.get(uri);
			if (service != null) {
				// 2.0 校验service是否符合当前环境

				// 2.1 构造相应的 request 和 response 上下文
				GenericRestRequest restRequest = new GenericRestRequest(request);
				GenericRestResponse restResponse = new GenericRestResponse(
						response);
				// 2.2 填充环境变量之类
				restRequest.setRestService(service);
				restResponse.setRestService(service);

				// 2.3 执行拦截器
				for (IRestInterceptor interceptor : interceptors) {
					interceptor.handleRest(restRequest, restResponse);
				}
				// 2.4 执行服务
				service.service(restRequest, restResponse);
				// 2.5
				if (restResponse.getResponseData() != null) {
					returnResponse = restResponse.getResponseData();
				}
			} else {
				throw new Exception("未找到对应的Rest服务:" + uri);
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnResponse.addError("doAction", e.getMessage());
		}
		response.getWriter().write(returnResponse.toJSON());
	}
}
