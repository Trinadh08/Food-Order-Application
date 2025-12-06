package package1;

import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class register extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String address = request.getParameter("address");

        // Validate input (very basic)
        if (name == null || email == null || password == null || address == null) {
            try (PrintWriter out = response.getWriter()) {
                out.println("<h3>Missing required fields.</h3>");
            }
            return;
        }

        // JDBC URL - include parameters to avoid common connection issues
        String url = "jdbc:mysql://localhost:3306/onlinefoodorderdb?useSSL=false&allowPublicKeyRetrieval=true";
        String dbUser = "root";
        String dbPass = "0804";

        // SQL: do NOT include id column; let MySQL auto-generate it
        String sql = "INSERT INTO customer (name, email, password, address) VALUES (?, ?, ?, ?)";

        try (PrintWriter out = response.getWriter()) {
            Class.forName("com.mysql.cj.jdbc.Driver"); // optional for newer JDBC, but safe

            try (Connection con = DriverManager.getConnection(url, dbUser, dbPass);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.setString(4, address);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    out.println("<h3>You are successfully registered!</h3>");
                } else {
                    out.println("<h3>Registration failed. Please try again.</h3>");
                }

            } catch (SQLException sqle) {
                // Print full error to browser for debugging (remove or log in production)
                sqle.printStackTrace(out);
            }

        } catch (Exception e) {
            // Print other errors
            e.printStackTrace();
        }
    }
}
