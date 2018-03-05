package com.kaushal.ddb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Players
 */
@WebServlet("/Players")
public class Players extends HttpServlet {
	private static final long serialVersionUID = 1L;
    String id, name;
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
	
    public Players() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		try {
			PrintWriter out = response.getWriter();
			if(request.getParameter("id")==null) {
				PreparedStatement ps = con.prepareStatement("SELECT * FROM tp");
				ResultSet rs = ps.executeQuery();
				out.println("<!DOCTYPE html><html><head><title>Players</title><body>");
				out.println("<table caption='Table Timepas' border='2px'>");
				out.println("<tr><th>ID</th><th>Name</th><th>Link</th></tr>");
				while(rs.next()) {
					id = rs.getString("id");
					name = rs.getString("name");
					out.print("<tr><td>"+ id +" </td><td>"+name+"</td><td><a href='Players?id="+id+"'>Click Here</a></td></tr>");
				}
				out.println("</table>");
				out.println("</body></html>");
			}else {
				String parameter = request.getParameter("id");
				PreparedStatement ps = con.prepareStatement("SELECT * FROM tp WHERE id=?");
				ps.setString(1, parameter);
				ResultSet rs = ps.executeQuery();
				out.println("<!DOCTYPE html><html><head><title>Players</title><body>");
				out.println("<table caption='Table Timepas' border='2px'>");
				out.println("<tr><th>ID</th><th>Name</th></tr>");
				while(rs.next()) {
					id = rs.getString("id");
					name = rs.getString("name");
					out.print("<tr><td>"+ id +" </td><td>"+name+"</td></tr>");
				}
				out.println("</table>");
				out.println("</body></html>");
			}
		}catch (Exception e) {
			
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
