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

@WebServlet("/Home")
public class Home extends HttpServlet {
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
    
    public Home() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		ArrayList<String> al = new ArrayList<>();
		String username = (String) session.getAttribute("username");
		if (username == null) {
			response.sendRedirect("Login");
		}
		PrintWriter out = response.getWriter();
		PreparedStatement ps;
		String budget = "", name = "", id="", points="";
		try {
			ps = con.prepareStatement("SELECT user_vgame.id as id, name, points, budget, league_id, country, points FROM user_vgame LEFT JOIN league ON user_vgame.league_id = league.id WHERE name=?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				budget = rs.getString("budget");
				name = rs.getString("name");
				id = rs.getString("id");
				points = rs.getString("points");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Home</title>");
		out.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.min.css\">");
		out.println("<script src=\"https://code.jquery.com/jquery-3.3.1.min.js\"></script>");
		out.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js\"></script>");
		out.println("</head>");
		out.println("<body>");
		out.println("<nav>\r\n" + 
				"    <div class=\"nav-wrapper\">\r\n" + 
				"      <a href=\"#\" class=\"brand-logo\" style=\"margin-left: 15px\">Welcome " + name + "</a>\r\n" + 
				"    </div>\r\n" + 
				"  </nav>");
		out.println("<h5 class='left-align' style='margin: 5px;'>Remaining Budget: "+budget + "</h5>");
		out.println("<h5 class='right-align'  style='margin: 5px;'>Points: "+points + "</h5>");
		out.println("<div class='card' style='padding: 15px; margin: 20px;'>");
		out.println("<h5>My Team</h5>");
		out.println("<ul class=\"collapsible popout\" data-collapsible=\"expandable\">");
		try {
			PreparedStatement ps2 = con.prepareStatement("SELECT buy.id as id, user_id, player_id, players.name, team_id, teams.name as team_name, value FROM buy LEFT JOIN players ON players.id = buy.player_id LEFT JOIN teams ON teams.id = players.team_id WHERE user_id=?");
			ps2.setString(1, id);
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()) {
				al.add(rs2.getString("player_id"));
				out.println("<li>");
				out.println("<div class='collapsible-header'><strong>"+ rs2.getString("name") +"</strong></div>");
				out.println("<div class='collapsible-body'>Team: <strong>"+rs2.getString("team_name")+"</strong></br>Value: <strong>"+ rs2.getString("value") +"</strong>");
				out.println("<form action='Home' method='POST'><input type='hidden' name='player_id' value='"+ rs2.getString("player_id") +"'/><input type='submit' class='waves-effect waves-light btn' value='SELL'/></form>");
				out.println("</div>");
				out.println("</li>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("</ul>");
		out.println("</div>");
		out.println("<div class='card' style='padding: 15px; margin: 20px;'>");
		out.println("<h5>All Players</h5>");
		out.println("<ul class=\"collapsible popout\" data-collapsible=\"expandable\">");
		try {
			PreparedStatement ps3 = con.prepareStatement("SELECT players.id, team_id, players.name as name, value, teams.name as team_name, points FROM players LEFT JOIN teams ON players.team_id = teams.id");
			ResultSet rs3 = ps3.executeQuery();
			while(rs3.next()) {
				out.println("<li>");
				out.println("<div class='collapsible-header'><strong>"+ rs3.getString("name") +"</strong></div>");
				out.println("<div class='collapsible-body'>Team: <strong>"+rs3.getString("team_name")+"</strong></br>Value: <strong>"+ rs3.getString("value") +"</strong>");
				if(!al.contains(rs3.getString("id")))
					out.println("<form action='Home' method='POST'><input type='hidden' name='player_id_buy' value='"+ rs3.getString("id") +"'/><input type='submit' class='waves-effect waves-light btn' value='BUY'/></form>");
				out.println("</div>");
				out.println("</li>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println("</ul>");
		out.println("</div>");
		out.println("<script>");
		out.println("$(document).ready(function(){\r\n" + 
				"    $('.collapsible').collapsible();\r\n" + 
				"  });");
		out.println("</script>");
		out.println("</body>");
		out.println("</html>");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// doGet(request, response);
		HttpSession session = request.getSession(false);
		String username = (String) session.getAttribute("username");
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		if (username == null) {
			response.sendRedirect("Login");
		}
		PreparedStatement ps;
		String user_id="",budget="";
		try {
			ps = con.prepareStatement("SELECT * FROM user_vgame WHERE name=?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				user_id = rs.getString("id");
				budget = rs.getString("budget");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (request.getParameter("player_id") != null) {
			String player_id = request.getParameter("player_id");
			try {
				con.setAutoCommit(false);
				PreparedStatement ps2 = con.prepareStatement("DELETE FROM buy WHERE user_id=? AND player_id=?");
				ps2.setString(1, user_id);
				ps2.setString(2, request.getParameter("player_id"));
				int i = ps2.executeUpdate();
				String value="0";
				if(i!=0) {
					PreparedStatement ps3 = con.prepareStatement("SELECT value FROM players WHERE id=?");
					ps3.setString(1, player_id);
					ResultSet rs3 = ps3.executeQuery();
					while(rs3.next()) {
						value = rs3.getString("value");
					}
					String final_budget = String.valueOf(Integer.parseInt(budget) + Integer.parseInt(value));
					if(Integer.parseInt(final_budget) > 15000)
						final_budget = "15000";
					PreparedStatement ps4 = con.prepareStatement("UPDATE user_vgame SET budget=? WHERE id=?");
					ps4.setString(1, final_budget);
					ps4.setString(2, user_id);
					int j = ps4.executeUpdate();
					if(j != 0) {
						con.commit();
						out.println("<script>alert('Player sold successfully!')</script>");
					}else {
						con.rollback();
						out.println("<script>alert('Something went wrong! Please try again!')</script>");
					}
				}else {
					con.rollback();
					out.println("<script>alert('Something went wrong! Please try again!')</script>");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				try {
					con.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				out.println("<script>alert('Something went wrong! Please try again!')</script>");
				e.printStackTrace();
			}
			
		}else if(request.getParameter("player_id_buy") != null) {
			String player_id = request.getParameter("player_id_buy");
			try {
				con.setAutoCommit(false);
				PreparedStatement ps2 = con.prepareStatement("INSERT INTO buy(user_id, player_id) VALUES (?, ?)");
				ps2.setString(1, user_id);
				ps2.setString(2, request.getParameter("player_id_buy"));
				int i = ps2.executeUpdate();
				String value="0";
				if(i!=0) {
					PreparedStatement ps3 = con.prepareStatement("SELECT value FROM players WHERE id=?");
					ps3.setString(1, player_id);
					ResultSet rs3 = ps3.executeQuery();
					while(rs3.next()) {
						value = rs3.getString("value");
					}
					String final_budget = String.valueOf(Integer.parseInt(budget) - Integer.parseInt(value));
					if(Integer.parseInt(final_budget) < 0)
						throw new Exception("Invalid budget");
					PreparedStatement ps4 = con.prepareStatement("UPDATE user_vgame SET budget=? WHERE id=?");
					ps4.setString(1, final_budget);
					ps4.setString(2, user_id);
					int j = ps4.executeUpdate();
					if(j != 0) {
						con.commit();
						out.println("<script>alert('Player Bought successfully!')</script>");
					}else {
						con.rollback();
						out.println("<script>alert('Something went wrong! Please try again!')</script>");
					}
				}else {
					con.rollback();
					out.println("<script>alert('Something went wrong! Please try again!')</script>");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				out.println("<script>alert('Something went wrong! Please try again!')</script>");
				e.printStackTrace();
			} catch (Exception e) {
				if (e.getMessage() == "Invalid budget") {
					try {
						con.rollback();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					out.println("<script>alert('Not enough balance!')</script>");
				}
				e.printStackTrace();
			}
		}
		doGet(request, response);
	}

}
