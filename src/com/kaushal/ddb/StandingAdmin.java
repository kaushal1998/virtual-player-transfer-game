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

/**
 * Servlet implementation class StandingAdmin
 */
@WebServlet("/StandingAdmin")
public class StandingAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Connection con;
       
    public StandingAdmin() {
        super();
    }
    
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

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		PreparedStatement ps;
		ResultSet rs = null;
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Home</title>");
		out.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-beta/css/materialize.min.css\">");
		out.println("<script src=\"https://code.jquery.com/jquery-3.3.1.min.js\"></script>");
		out.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0-beta/js/materialize.min.js\"></script>");
		out.println("</head>");
		out.println("<body>");
		out.println("<nav>\r\n" + 
				"    <div class=\"nav-wrapper blue\">\r\n" + 
				"      <a href=\"#\" class=\"brand-logo\" style=\"margin-left: 15px\">Standings</a>\r\n" +
				"      <ul id=\"nav-mobile\" class=\"right hide-on-med-and-down\">\r\n" +
				"        <li><a href=\"Admin\">Home</a></li>\r\n" +
				"      </ul>\r\n" + 
				"    </div>\r\n" + 
				"  </nav>");
		out.println("<div class='row'>");
		out.println("<div class='col s12 m4'>");
		out.println("<ul class=\"collection with-header\"><li class=\"collection-header\"><h4>Standings (India)</h4></li>");
		try {
			PreparedStatement ps2 = con.prepareStatement("SELECT user_india.id as id, points, league_id, name FROM user_india ORDER BY points DESC");
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()) {
				out.println("<li class=\"collection-item\"><strong>"+rs2.getString("name")+"</strong>&nbsp&nbsp&nbspPonits: "+rs2.getString("points")+"</li>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("</ul>");
		out.println("</div>");
		out.println("<div class='col s12 m4'>");
		out.println("<ul class=\"collection with-header\"><li class=\"collection-header\"><h4>Standings (USA)</h4></li>");
		try {
			PreparedStatement ps2 = con.prepareStatement("SELECT user_usa.id as id, points, league_id, name FROM user_usa ORDER BY points DESC");
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()) {
				out.println("<li class=\"collection-item\"><strong>"+rs2.getString("name")+"</strong>&nbsp&nbsp&nbspPonits: "+rs2.getString("points")+"</li>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("</ul>");
		out.println("</div>");
		out.println("<div class='col s12 m4'>");
		out.println("<ul class=\"collection with-header\"><li class=\"collection-header\"><h4>Standings (Spain)</h4></li>");
		try {
			PreparedStatement ps2 = con.prepareStatement("SELECT user_spain.id as id, points, league_id, name FROM user_spain ORDER BY points DESC");
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()) {
				out.println("<li class=\"collection-item\"><strong>"+rs2.getString("name")+"</strong>&nbsp&nbsp&nbspPonits: "+rs2.getString("points")+"</li>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("</ul>");
		out.println("</div>");
		out.println("</div>");
		out.println("<ul class=\"collection with-header\"><li class=\"collection-header\"><h4>Standings(Global)</h4></li>");
		
		try {
			PreparedStatement ps2 = con.prepareStatement("SELECT user_vgame.id as id, points, league_id, name FROM user_vgame ORDER BY points DESC");
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()) {
				out.println("<li class=\"collection-item\"><strong>"+rs2.getString("name")+"</strong>&nbsp&nbsp&nbspPonits: "+rs2.getString("points")+"</li>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("</ul>");
		
		out.println("</body>");
		out.println("</html>");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
