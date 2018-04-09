package com.kaushal.ddb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Standings")
public class Standings extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection con;
	
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
       
    public Standings() {
        super();
    }

    
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		ArrayList<String> al = new ArrayList<>();
		String username="";
		try {
			username = (String) session.getAttribute("username");
		}catch(NullPointerException e) {
			response.sendRedirect("Login");
			return;
		}
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		PreparedStatement ps;
		String budget = "", name = "", id="", points="", league_id="";
		try {
			ps = con.prepareStatement("SELECT user_vgame.id as id, name, points, budget, league_id, country, points FROM user_vgame LEFT JOIN league ON user_vgame.league_id = league.id WHERE name=?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				budget = rs.getString("budget");
				name = rs.getString("name");
				id = rs.getString("id");
				points = rs.getString("points");
				league_id = rs.getString("league_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
				"        <li><a href=\"Home\">Home</a></li>\r\n" +
				"        <li><a href=\"Logout\">Logout</a></li>\r\n" +
				"      </ul>\r\n" + 
				"    </div>\r\n" + 
				"  </nav>");
		switch (league_id) {
		case "1":
			out.println("<ul class=\"collection with-header\"><li class=\"collection-header\"><h4>Standings (India)</h4></li>");
			try {
				out.println("<script>console.log("+league_id+")</script>");
				PreparedStatement ps2 = con.prepareStatement("SELECT user_vgame.id as id, points, league_id, name FROM user_vgame WHERE league_id=1 ORDER BY points DESC");
				ResultSet rs2 = ps2.executeQuery();
				while(rs2.next()) {
					out.println("<li class=\"collection-item\"><strong>"+rs2.getString("name")+"</strong>&nbsp&nbsp&nbspPonits: "+rs2.getString("points")+"</li>");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.println("</ul>");
			break;
			
		case "2":
			out.println("<ul class=\"collection with-header\"><li class=\"collection-header\"><h4>Standings (USA)</h4></li>");
			try {
				out.println("<script>console.log("+league_id+")</script>");
				PreparedStatement ps2 = con.prepareStatement("SELECT user_vgame.id as id, points, league_id, name FROM user_vgame WHERE league_id=2 ORDER BY points DESC");
				ResultSet rs2 = ps2.executeQuery();
				while(rs2.next()) {
					out.println("<li class=\"collection-item\"><strong>"+rs2.getString("name")+"</strong>&nbsp&nbsp&nbspPonits: "+rs2.getString("points")+"</li>");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.println("</ul>");
			break;
			
		case "3":
			out.println("<ul class=\"collection with-header\"><li class=\"collection-header\"><h4>Standings (Spain)</h4></li>");
			try {
				out.println("<script>console.log("+league_id+")</script>");
				PreparedStatement ps2 = con.prepareStatement("SELECT user_vgame.id as id, points, league_id, name FROM user_vgame WHERE league_id=3 ORDER BY points DESC");
				ResultSet rs2 = ps2.executeQuery();
				while(rs2.next()) {
					out.println("<li class=\"collection-item\"><strong>"+rs2.getString("name")+"</strong>&nbsp&nbsp&nbspPonits: "+rs2.getString("points")+"</li>");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.println("</ul>");
			break;

		default:
			break;
		}
		
		out.println("<ul class=\"collection with-header\"><li class=\"collection-header\"><h4>Standings(Global)</h4></li>");
		
		try {
			out.println("<script>console.log("+league_id+")</script>");
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
		doGet(request, response);
	}

}
