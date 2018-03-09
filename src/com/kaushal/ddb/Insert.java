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

@WebServlet("/Insert")
public class Insert extends HttpServlet {
	private static final long serialVersionUID = 1L;
    PreparedStatement ps;
    Connection con;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","kaushal","kaushal");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public Insert() {
        super();
        // TODO Auto-generated constructor stub
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		response.sendRedirect("Register.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Inserting</title>");
		out.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.min.css\">");
		out.println("<script src=\"https://code.jquery.com/jquery-3.3.1.min.js\"></script>");
		out.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js\"></script>");
		out.println("</head>");
		out.println("<body>");
		if(request.getParameter("submit") != null && request.getParameter("username") != null && request.getParameter("password") != null && request.getParameter("country") != null) {
			String username = request.getParameter("username").trim();
			String password = request.getParameter("password").trim();
			String league_id = request.getParameter("country").trim();
			String number = "0";
			try {
				ps = con.prepareStatement("SELECT count(*) as n FROM user_vgame WHERE name=?");
				ps.setString(1, username);
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					number = rs.getString("n");
				}
				if(!number.equals("0")) {
					out.println("<script>alert('User already exists!');</script>");
					out.println("<a href='Register.jsp' class='waves-effect waves-light btn red'>Back</a>");
				}else {
					PreparedStatement ps2 = con.prepareStatement("INSERT INTO user_vgame(name, points, password, budget, league_id) VALUES (?, 0, ?, 15000, ?)");
					ps2.setString(1, username);
					ps2.setString(2, password);
					ps2.setString(3, league_id);
					int i = ps2.executeUpdate();
					if(i!=0) {
						out.println("User has been registered successfully");
						out.println("<a href='Login' class='waves-effect waves-light btn red'>Go to Login Page</a>");
						out.println("<a href='Register.jsp' class='waves-effect waves-light btn red'>Register again</a>");
					}else {
						out.println("Something went wrong! Please try again");
						out.println("<a href='Register.jsp' class='waves-effect waves-light btn red'>Register Page</a>");
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
			out.println("<a href='Register.jsp' class='waves-effect waves-light btn red'>Insufficient data - try again</a>");
		}
		out.println("</body>");
		out.println("</html>");
	}

}
