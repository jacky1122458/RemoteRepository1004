package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import model.ProductBean;
import model.ProductService;

@WebServlet(
		urlPatterns={"/pages/product.controller"}
)
public class ProductServlet extends HttpServlet {
	private ProductService productService;
	@Override
	public void init() throws ServletException {
		ServletContext application = this.getServletContext();
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(application);
		productService = (ProductService) context.getBean("productService");
	}
	private SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
//接收資料
		String temp1 = request.getParameter("id");
		String name = request.getParameter("name");
		String temp2 = request.getParameter("price");
		String temp3 = request.getParameter("make");
		String temp4 = request.getParameter("expire");
		String prodaction = request.getParameter("prodaction");
		
//轉換資料
		Map<String, String> errors = new HashMap<String, String>();
		request.setAttribute("error", errors);
		
		int id = 0;
		if(temp1!=null && temp1.trim().length()!=0) {
			try {
				id = Integer.parseInt(temp1);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				errors.put("id", "Id必須是整數");
			}
		}
		double price = 0;
		if(temp2!=null && temp2.trim().length()!=0) {
			try {
				price = Double.parseDouble(temp2);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				errors.put("price", "Price必須是數字");
			}
		}
		java.util.Date make = null;
		if(temp3!=null && temp3.trim().length()!=0) {
			try {
				make = sFormat.parse(temp3);
			} catch (ParseException e) {
				e.printStackTrace();
				errors.put("make", "Make必須是日期:yyyy-MM-dd");
			}
		}
		int expire = 0;
		if(temp4!=null && temp4.trim().length()!=0) {
			try {
				expire = Integer.parseInt(temp4);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				errors.put("expire", "Expire必須是整數");
			}
		}
		
//驗證資料	
		if("Insert".equals(prodaction) || "Update".equals(prodaction) || "Delete".equals(prodaction)) {
			if(id==0) {
				errors.put("id", "請輸入Id以便於執行"+prodaction);
			}
		}
		
		if(errors!=null && !errors.isEmpty()) {
			request.getRequestDispatcher(
					"/pages/product.jsp").forward(request, response);
			return;
		}
//呼叫Model, 根據Model執行結果顯示View
		ProductBean bean = new ProductBean();
		bean.setId(id);
		bean.setName(name);
		bean.setPrice(price);
		bean.setMake(make);
		bean.setExpire(expire);

		if("Select".equals(prodaction)) {
			List<ProductBean> result = productService.select(bean);
			request.setAttribute("select", result);
			request.getRequestDispatcher(
					"/pages/display.jsp").forward(request, response);
		} else if("Insert".equals(prodaction)) {
			ProductBean result = productService.insert(bean);
			if(result==null) {
				errors.put("action", "新增資料失敗");
			} else {
				request.setAttribute("insert", result);
			}
			request.getRequestDispatcher(
					"/pages/product.jsp").forward(request, response);
		} else if("Update".equals(prodaction)) {
			ProductBean result = productService.update(bean);
			if(result==null) {
				errors.put("action", "修改資料失敗");
			} else {
				request.setAttribute("update", result);
			}
			request.getRequestDispatcher(
					"/pages/product.jsp").forward(request, response);
		} else if("Delete".equals(prodaction)) {
			boolean result = productService.delete(bean);
			request.setAttribute("delete", result);
			request.getRequestDispatcher(
					"/pages/product.jsp").forward(request, response);
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
