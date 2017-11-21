/**  
 * ---------------------------------------------------------------------------
 * Copyright (c) 2017, xsjt- All Rights Reserved.
 * Project Name:springboot-test  
 * File Name:XbqServlet.java  
 * Package Name:com.xsjt.servlet
 * Author   Joe
 * Date:2017年11月6日下午5:13:10
 * ---------------------------------------------------------------------------  
*/  
  
package com.xsjt.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**  
 * ClassName:XbqServlet 
 * 通过 代码注册Servlet 
 * Date:     2017年11月6日 下午5:13:10
 * @author   Joe  
 * @version    
 * @since    JDK 1.8
 */
public class XbqServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html");  
        PrintWriter out = resp.getWriter();  
        out.println("<html>");  
        out.println("<head>");  
        out.println("<title>Hello World</title>");  
        out.println("</head>");  
        out.println("<body><center>");  
        out.println("<h3>我是通过代码注册Servlet</h3>");  
        out.println("</center></body>");  
        out.println("</html>"); 
	}
}