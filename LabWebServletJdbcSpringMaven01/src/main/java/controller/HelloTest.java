package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class HelloTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException{
    	  response.setContentType("text/html; charset=UTF-8");
          PrintWriter out = response.getWriter();
          out.println("<html><head>");
          out.println("<link rel='stylesheet' href='../css/styles.css' type='text/css' />");
          out.println("<title>Hello, World 大家好 (Servlet)</title>");
          out.println("</head>");
          out.println("<body>");
          out.println("<H3>Hello, World 大家好(Servlet)</H3>");
          java.util.Date d = new java.util.Date();
          out.println("現在時刻是" + d);
          String referer = request.getHeader("referer"); 
          out.println("<br><P><P><P><hr><center><small>&lt;&lt;<a href=" + referer + ">回前頁</a>&gt;&gt;</small>");
          out.println("</body>");
          out.println("</html>");
    }
}