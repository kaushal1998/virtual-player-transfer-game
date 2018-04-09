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

@WebServlet("/Add")
public class Add extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
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
	
    public Add() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		PreparedStatement ps;
		ResultSet rs = null;
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Admin</title>");
		out.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-beta/css/materialize.min.css\">");
		out.println("<script src=\"https://code.jquery.com/jquery-3.3.1.min.js\"></script>");
		out.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-beta/js/materialize.min.js\"></script>");
		out.println("</head>");
		out.println("<body>");
		out.println("<nav>\r\n" + 
				"    <div class=\"nav-wrapper blue\">\r\n" + 
				"      <a href=\"#\" class=\"brand-logo\" style=\"margin-left: 15px\">Admin</a>\r\n" + 
				"      <ul id=\"nav-mobile\" class=\"right hide-on-med-and-down\">\r\n" + 
				"        <li><a href=\"Admin\">Home</a></li>\r\n" + 
				"      </ul>\r\n" + 
				"    </div>\r\n" + 
				"  </nav>");
		out.println("<div class='card' style='padding: 15px;margin: 20px;'>");
		out.println("<h4>Add Team</h4>");
		out.println("<form method='POST' action='Add'>");
		out.println("<div class=\"input-field\">\r\n" + 
				"          <input id=\"team_name\" name=\"team_name\" type=\"text\" class=\"validate\">\r\n" + 
				"          <label for=\"team_name\">Enter team name:</label>\r\n" + 
				"        </div>");
		out.println("<div class=\"input-field\">\r\n" + 
				"          <input id=\"t_submit\" name=\"t_submit\" type=\"submit\" class=\"btn blue\">\r\n" + 
				"        </div>");
		out.println("</form>");
		out.println("</div>");
		
		// SQL query for adding teams in select box.
		try {
			ps = con.prepareStatement("SELECT * FROM teams");
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		out.println("<div class='card' style='padding: 15px;margin: 20px;'>");
		out.println("<h4>Add Player</h4>");
		out.println("<form method='POST' action='Add'>");
		out.println("<div class=\"input-field\">\r\n" + 
				"          <input id=\"player\" name=\"player\" type=\"text\" class=\"validate\">\r\n" + 
				"          <label for=\"player\">Enter player name</label>\r\n" + 
				"        </div>");
		out.println("<div class=\"input-field\">\r\n" + 
				"          <input id=\"p_value\" name=\"p_value\" type=\"number\" class=\"validate\">\r\n" + 
				"          <label for=\"p_value\">Player value:</label>\r\n" + 
				"        </div>");
		out.println("<div class=\"input-field\">\r\n" + 
				"    <select name='team_id'>\r\n" + 
				"      <option value=\"\" disabled selected>Choose your option</option>\r\n");
		try {
			while(rs.next()) {
				out.println("<option value='" + rs.getString("id") +"'>"+ rs.getString("name") + "</option>\r\n"); 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("</select>\r\n" + 
				"    <label>Materialize Select</label>\r\n" + 
				"  </div>");
		out.println("<div class=\"input-field\">\r\n" + 
				"          <input id=\"p_submit\" name=\"p_submit\" type=\"submit\" class=\"btn blue\">\r\n" + 
				"        </div>");
		out.println("</form>");
		out.println("</div>");
		out.println("</body>");
		out.println("<script>$(document).ready(function() {\r\n" + 
				"    $('select').formSelect();\r\n" + 
				"  });</script>");
		out.println("</html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		if(request.getParameter("t_submit") != null) {
			String team_name = request.getParameter("team_name");
			try {
				PreparedStatement ps = con.prepareStatement("INSERT INTO teams(name, recent_match_points) VALUES (?, 0)");
				ps.setString(1, team_name);
				int i = ps.executeUpdate();
				if(i!=0) {
					out.println("<script>alert('Team added successfully!');</script>");
				}
			} catch (SQLException e) {
				out.println("<script>alert('Something went wrong! Please try again;');</script>");
			}
		}
		if (request.getParameter("p_submit") != null) {
			String player = request.getParameter("player");
			String p_value = request.getParameter("p_value");
			String team_id = request.getParameter("team_id");
			try {
				PreparedStatement ps = con.prepareStatement("INSERT INTO players(name, value, team_id, points) VALUES (?, ?, ?, 0)");
				ps.setString(1, player);
				ps.setString(2, p_value);
				ps.setString(3, team_id);
				int i = ps.executeUpdate();
				if(i!=0) {
					out.println("<script>alert('Player added successfully!');</script>");
				}
			} catch (SQLException e) {
				out.println("<script>alert('Something went wrong! Please try again;');</script>");
			}
		}
		doGet(request, response);
	}

}
