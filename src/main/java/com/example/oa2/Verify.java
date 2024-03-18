package com.example.oa2;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import utils.DBUil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/verify")
public class Verify extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try {
            connection = DBUil.getConnection();
            String sql = "Select * from users where username= ? and password= ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                HttpSession session = req.getSession();
                session.setAttribute("username", username);
                String login = req.getParameter("login");
                if ("yes".equals(login)){
                    Cookie cookie = new Cookie("username", username);
                    Cookie cookie1 = new Cookie("password", password);
                    cookie.setMaxAge(60*60*24*10);
                    cookie1.setMaxAge(60*60*24*10);
                    resp.addCookie(cookie);
                    resp.addCookie(cookie1);
                }

                resp.sendRedirect("/oa/dept/list");
            } else {

                resp.sendRedirect("/oa/login.html");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            DBUil.close(connection, preparedStatement, resultSet);
        }
    }
}
