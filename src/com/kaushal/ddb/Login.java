package com.kaushal.ddb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    Connection con;
    public Login() {
        super();    
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	try {
			Class.forName("oracle.jdbc.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","kaushal","kaushal");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Login</title>");
		out.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-beta/css/materialize.min.css\">");
		out.println("<script src=\"https://code.jquery.com/jquery-3.3.1.min.js\"></script>");
		out.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-beta/js/materialize.min.js\"></script>");
		out.println("</head>");
		out.println("<body>");
		out.println("<nav>\r\n" + 
				"    <div class=\"nav-wrapper blue\">\r\n" + 
				"      <a href=\"#\" class=\"brand-logo\" style=\"margin-left: 15px\">VPTGame</a>\r\n" + 
				"    </div>\r\n" + 
				"  </nav>");
		out.println("<div class='row'>");
		out.println("<form class='card col s12 m6 l6' style='margin-left: 22%; margin-top: 25px;' action='Login' method='POST'>");
		out.println("<h5>Login</h5>");
		out.println("<div class=\"input-field\">\r\n" + 
				"	          <input name=\"username\" id=\"username\" type=\"text\" class=\"validate\">\r\n" + 
				"	          <label for=\"username\">Username</label>\r\n" + 
				"	        </div>\r\n" + 
				"	        ");
		out.println("<div class=\"input-field\">\r\n" + 
				"	          <input name=\"password\" id=\"password\" type=\"password\" class=\"validate\">\r\n" + 
				"	          <label for=\"password\">Password</label>\r\n" + 
				"	        </div>");
		out.println("<div class=\"input-field\">\r\n" + 
				"	          <input name=\"submit\" id=\"submit\" type=\"submit\" class=\"btn blue\" value=\"Login\" style=\"margin: 15px\">\r\n" + 
				"	        </div>");
		out.println("</form>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		if(request.getParameter("username") == null || request.getParameter("password") == null) {
			doGet(request, response);
		}else {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String n = "0";
			try {
				PreparedStatement ps = con.prepareStatement("SELECT count(*) as n from user_vgame WHERE name=? AND password=?");
				ps.setString(1, username);
				ps.setString(2, password);
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					n = rs.getString("n");
				}
				if(!n.equals("0")) {
					HttpSession session = request.getSession();
					session.setAttribute("username", username);
					response.sendRedirect("Home");
				}else {
					out.println("<script>alert('Incorrect credentials!')</script>");
					doGet(request, response);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
