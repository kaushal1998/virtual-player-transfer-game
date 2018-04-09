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

@WebServlet("/Admin")
public class Admin extends HttpServlet {
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
	
    public Admin() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
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
				"      <a href=\"#\" class=\"brand-logo\" style=\"margin-left: 15px\">Welcome Admin</a>\r\n" + 
				"      <ul id=\"nav-mobile\" class=\"right hide-on-med-and-down\">\r\n" +
				"        <li><a href=\"StandingAdmin\">Standings</a></li>\r\n" + 
				"        <li><a href=\"Add\">Add Team/Player</a></li>\r\n" + 
				"      </ul>\r\n" + 
				"    </div>\r\n" + 
				"  </nav>");
		out.println("<h4 style=\"margin-left: 15px;\">Give Points to Players</h4>");
		out.println("<ul class=\"collapsible popout\" data-collapsible=\"expandable\">");
		try {
			PreparedStatement ps = con.prepareStatement("SELECT players.id as id, team_id, players.name as name, value, points, teams.name as team_name FROM players LEFT JOIN teams ON teams.id = players.team_id");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				out.println("<li>");
				out.println("<div class='collapsible-header'><strong>"+ rs.getString("name") +"</strong></div>");
				out.println("<div class='collapsible-body'>Team: <strong>"+rs.getString("team_name")+"</strong></br>Value: <strong>"+ rs.getString("value") +"</strong></br>Points: <strong>"+ rs.getString("points") +"</strong>");
				out.println("<form action='Admin' method='POST'><input type='hidden' name='player_id' value='"+ rs.getString("id") +"'/><div class='input-field'><input type='number' class='validate' name='points' id='points'><label for='points'>Enter Points</label></div><input type='submit' class='btn blue' value='Add Points'/></form>");
				out.println("</div>");
				out.println("</li>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.println("</ul>");
		out.println("</body>");
		out.println("<script>$(document).ready(function(){\r\n" + 
				"    $('.collapsible').collapsible();\r\n" + 
				"  });</script>");
		out.println("</html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		if(request.getParameter("player_id") != null && request.getParameter("points") != null) {
			String player_id = request.getParameter("player_id");
			String points = request.getParameter("points");
			String points_next = "0";
			try {
				con.setAutoCommit(false);
				PreparedStatement ps = con.prepareStatement("SELECT points FROM players WHERE id=?");
				ps.setString(1, player_id);
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					points_next = rs.getString("points");
				}
				points_next = String.valueOf(Integer.parseInt(points_next) + Integer.parseInt(points));
				PreparedStatement ps2 = con.prepareStatement("UPDATE players SET points=? WHERE id=?");
				ps2.setString(1, points_next);
				ps2.setString(2, player_id);
				int i = ps2.executeUpdate();
				if(i!=0) {
					PreparedStatement ps3 = con.prepareStatement("SELECT user_vgame.id as id, points FROM buy LEFT JOIN user_vgame ON user_vgame.id = buy.user_id WHERE buy.player_id=?");
					ps3.setString(1, player_id);
					ResultSet rs3 = ps3.executeQuery();
					while(rs3.next()) {
						String user_id = rs3.getString("id");
						String ind_points = rs3.getString("points");
						String ind_points_next = String.valueOf(Integer.parseInt(ind_points) + Integer.parseInt(points));
						PreparedStatement ps4 = con.prepareStatement("UPDATE user_vgame SET points=? WHERE id=?");
						ps4.setString(1, ind_points_next);
						ps4.setString(2, user_id);
						int j = ps4.executeUpdate();
						if(j==0) {
							out.println("<script>alert('Something went wrong! Please try again!')</script>");
							con.rollback();
							break;
						}
						if (!rs3.isAfterLast()) {
							con.commit();
							System.out.println("True");
							out.println("<script>alert('Player points updated successfully!')</script>");
			            }
					}
				}else {
					con.rollback();
					out.println("<script>alert('Something went wrong! Please try again!')</script>");
				}
			} catch (SQLException e) {
				out.println("<script>alert('Something went wrong! Please try again!')</script>");
				try {
					con.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		doGet(request, response);
	}

}
