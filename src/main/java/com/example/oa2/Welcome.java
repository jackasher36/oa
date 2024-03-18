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

@WebServlet("/welcome")
public class Welcome extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        String username =null;
        String password = null;
        if (cookies != null){
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if ("username".equals(name)){
                    username = cookie.getValue();
                }else if ("password".equals(name)){
                    password = cookie.getValue();
                }
            }
            if (password != null&&username != null){
//                resp.sendRedirect("/oa/dept/list");
                Connection connection =null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                boolean success = false;

                try {
                    connection = DBUil.getConnection();
                    String sql = "Select * from users where username = ? and password = ?";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1,username);
                    preparedStatement.setString(2,password);
                    ResultSet resultSet1 = preparedStatement.executeQuery();
                    if (resultSet1.next()) {
                        success = true;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if (success){
                    HttpSession session = req.getSession();
                    session.setAttribute("username", username);
                    resp.sendRedirect(req.getContextPath() + "/dept/list");
                }else {
                    resp.sendRedirect(req.getContextPath() + "/login.html");
                }


            }else {

                resp.sendRedirect(req.getContextPath() + "/login.html");
            }
        }else {

            resp.sendRedirect("/login.html");
        }
    }
}
