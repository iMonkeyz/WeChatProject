package com.imonkeyz.demo.interceptor;

import com.imonkeyz.demo.annotation.WeChatAdminRequired;
import com.imonkeyz.demo.annotation.WeChatGuestRequired;
import com.imonkeyz.demo.constants.WeChatConstants;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Jesse on 2017/9/28.
 */
public class WeChatGuestAuthInterceptor extends HandlerInterceptorAdapter {

	private final static Logger LOG = Logger.getLogger(WeChatGuestAuthInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		WeChatGuestRequired methodAnnotation = getWeChatGuestRequired(handler);
		if ( methodAnnotation != null ) {
			LOG.debug("正在检查 " + request.getRequestURL());
			if ( request.getSession().getAttribute(WeChatConstants.SESSION_GUEST) == null ) {
				//response.sendRedirect(request.getContextPath() + methodAnnotation.failed());
				response.getWriter().write("You don't have permission to access this page.");
				LOG.debug("拒绝访问 " + request.getRequestURL());
				return false;
			}
		}
		return super.preHandle(request, response, handler);
	}

	private WeChatGuestRequired getWeChatGuestRequired(Object handler) {
		WeChatGuestRequired methodAnnotation = null;
		if ( handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			methodAnnotation = handlerMethod.getMethodAnnotation(WeChatGuestRequired.class);
			if ( methodAnnotation == null ) {
				handlerMethod.getBeanType().getAnnotation(WeChatGuestRequired.class);
			}
		}
		return methodAnnotation;
	}
}
