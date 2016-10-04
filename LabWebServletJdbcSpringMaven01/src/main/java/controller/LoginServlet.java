package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import model.CustomerBean;
import model.CustomerService;
@WebServlet(
		urlPatterns={"/secure/login.controller"}		
)
public class LoginServlet extends HttpServlet {
	private CustomerService service;
	@Override
	public void init() throws ServletException {
		ServletContext application = this.getServletContext();
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(application);
		service = (CustomerService) context.getBean("customerService");
	}
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//接收資料
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
//驗證資料
		Map<String, String> errors = new HashMap<String, String>();
		request.setAttribute("error", errors);
		if(username==null || username.length()==0) {
			errors.put("username", "Please enter ID for login");
		}
		if(password==null || password.length()==0) {
			errors.put("password", "Please enter PWD for login");
		}
		
		if(errors!=null && !errors.isEmpty()) {
			request.getRequestDispatcher(
					"/secure/login.jsp").forward(request, response);
			return;
		}
		
//呼叫model
		CustomerBean bean = service.login(username, password);
		
//根據model執行結果，導向view
		if(bean==null) {
			errors.put("password", "Login failed, please try again.");
			request.getRequestDispatcher(
					"/secure/login.jsp").forward(request, response);
		} else {
			HttpSession session = request.getSession();
			session.setAttribute("user", bean);
			
			String path = request.getContextPath();
			response.sendRedirect(path+"/index.jsp");
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
