package com.sec.webs.gui;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * Servlet Filter implementation class NeedLoginFilter
 */
public class NeedLoginFilter implements Filter {

    /**
     * Default constructor. 
     */
    public NeedLoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hReq = (HttpServletRequest) request;
		HttpServletResponse hRes = (HttpServletResponse) response;
		String userId = (String) hReq.getSession().getAttribute("UserID");

		if (userId == null) {
	        String accept = hReq.getHeader("accept");
	        if (accept.contains("json")) {
	        	JSONObject jsonObject = new JSONObject();
	            jsonObject.put("Result", "Error");
	            jsonObject.put("Message", "You have to login first.");
	            hRes.getWriter().println(jsonObject.toJSONString());
	        } else {
	        	hRes.sendRedirect("/login.jsp");
	        }
			return;
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
