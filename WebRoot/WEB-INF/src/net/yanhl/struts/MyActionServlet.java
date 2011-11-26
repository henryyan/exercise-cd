package net.yanhl.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyActionServlet extends org.apache.struts.action.ActionServlet {
	private static final long serialVersionUID = -5966483093670648072L;

	protected void process(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, javax.servlet.ServletException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		//response.setContentType("text/html;charset=UTF-8");
		super.process(request, response);
	}
}
