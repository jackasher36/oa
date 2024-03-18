package com.example.oa2;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import utils.DBUil;

@WebServlet({"/dept/list","/dept/detail","/dept/save","/dept/delete","/dept/edit"})
public class HelloServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        HttpSession session = req.getSession(false);
        if (session !=null && session.getAttribute("username") != null) {
            if ("/dept/list".equals(servletPath)) {
                doList(req, resp);
            } else if ("/dept/detail".equals(servletPath)) {
                doDetail(req, resp);
            } else if ("/dept/save".equals(servletPath)) {
                doSave(req, resp);
            } else if ("/dept/delete".equals(servletPath)) {
                doDel(req, resp);
            } else if ("/dept/edit".equals(servletPath)) {
                doEdit(req, resp);
            } else if ("/session".equals(servletPath)) {
                doSession(req, resp);
            }
        }else {
            resp.sendRedirect(req.getContextPath());
        }
    }

    private void doList(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        String contextPath = req.getContextPath();

        out.print("<!DOCTYPE html>");
        out.print("<html lang=\"en\">");
        out.print("<head>");
        out.print("<meta charset='utf-8'>");
        out.print("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.print("<title>部门列表页面</title>");
        out.print("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\">");
        out.print("<style>");
        out.print("body {");
        out.print("font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;");
        out.print("text-align: center;");
        out.print("margin: 0;");
        out.print("padding: 0;");
        out.print("background-color: #f5f5f7;");
        out.print("}");
        out.print(".container {");
        out.print("max-width: 800px;");
        out.print("margin: 0 auto;");
        out.print("padding-top: 20px;");
        out.print("}");
        out.print("h1 {");
        out.print("color: #333;");
        out.print("}");
        out.print("table {");
        out.print("width: 100%;");
        out.print("background-color: #fff;");
        out.print("border-radius: 8px;");
        out.print("box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);");
        out.print("}");
        out.print("th, td {");
        out.print("padding: 12px 15px;");
        out.print("text-align: left;");
        out.print("border-bottom: 1px solid #e0e0e0;");
        out.print("}");
        out.print("th {");
        out.print("background-color: #f7f7f9;");
        out.print("}");
        out.print(".btn {");
        out.print("padding: 8px 16px;");
        out.print("border-radius: 6px;");
        out.print("text-decoration: none;");
        out.print("color: #fff;");
        out.print("}");
        out.print(".btn-danger {");
        out.print("background-color: #f44336;");
        out.print("}");
        out.print(".btn-danger:hover {");
        out.print("background-color: #d32f2f;");
        out.print("}");
        out.print(".btn-primary {");
        out.print("background-color: #2196f3;");
        out.print("}");
        out.print(".btn-primary:hover {");
        out.print("background-color: #1976d2;");
        out.print("}");
        out.print(".btn-info {");
        out.print("background-color: #4caf50;");
        out.print("}");
        out.print(".btn-info:hover {");
        out.print("background-color: #388e3c;");
        out.print("}");
        out.print(".btn-success {");
        out.print("background-color: #8bc34a;");
        out.print("}");
        out.print(".btn-success:hover {");
        out.print("background-color: #689f38;");
        out.print("}");
        out.print(".btn-warning {");
        out.print("background-color: #ff9800;");
        out.print("}");
        out.print(".btn-warning:hover {");
        out.print("background-color: #f57c00;");
        out.print("}");
        out.print("</style>");
        out.print("</head>");
        out.print("<body>");
        out.print("<div class=\"container\">");
        out.print("<h1>部门列表</h1>");
        out.print("<hr />");
        out.print("<table class=\"table\">");
        out.print("<thead>");
        out.print("<tr>");
        out.print("<th scope=\"col\">序号</th>");
        out.print("<th scope=\"col\">部门编号</th>");
        out.print("<th scope=\"col\">部门名称</th>");
        out.print("<th scope=\"col\">部门位置</th>");
        out.print("<th scope=\"col\">操作</th>");
        out.print("</tr>");
        out.print("</thead>");
        out.print("<tbody>");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUil.getConnection();
            String selectSql = "SELECT deptno, dname, loc FROM dept";
            preparedStatement = connection.prepareStatement(selectSql);
            resultSet = preparedStatement.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                String deptno = resultSet.getString("deptno");
                String dname = resultSet.getString("dname");
                String loc = resultSet.getString("loc");

                out.print("<tr>");
                out.print("<td>" + ++i + "</td>");
                out.print("<td>" + deptno + "</td>");
                out.print("<td>" + dname + "</td>");
                out.print("<td>" + loc + "</td>");
                out.print("<td>");
                out.print("<a href='javascript:void(0)' onclick='del(\"" + deptno + "\")' class=\"btn btn-danger\">删除</a>");
                out.print("<a href='/oa/edit.html' class=\"btn btn-primary\">修改</a>");
                out.print("<a href='" + contextPath + "/dept/detail?deptno=" + deptno + "' class=\"btn btn-info\">详情</a>");
                out.print("</td>");
                out.print("</tr>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUil.close(connection, preparedStatement, resultSet);
        }

        out.print("</tbody>");
        out.print("</table>");
        out.print("<hr />");
        out.print("<a href='/oa/add.html' class=\"btn btn-success\">新增部门</a>");
        out.print("<hr>");
        out.print("<a href='/oa/dept/exit' class=\"btn btn-warning\">退出系统</a>");
        out.print("</div>");
        out.print("</body>");
        out.print("</html>");
    }


    private void doDetail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        String deptno = req.getParameter("deptno");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUil.getConnection();
            String sql = "select * from dept where deptno = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, deptno);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String dname = resultSet.getString("dname");
                String loc = resultSet.getString("loc");

                out.print("<!DOCTYPE html>");
                out.print("<html>");
                out.print("<head>");
                out.print("<meta charset='utf-8'>");
                out.print("<title>部门详情</title>");
                out.print("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\">");
                out.print("<style>");
                out.print("body {");
                out.print("font-family: Arial, sans-serif;");
                out.print("text-align: center;");
                out.print("margin: 0;");
                out.print("padding: 0;");
                out.print("}");
                out.print(".container {");
                out.print("width: 50%;");
                out.print("margin: 0 auto;");
                out.print("}");
                out.print("h1 {");
                out.print("margin-top: 20px;");
                out.print("}");
                out.print(".info {");
                out.print("margin-top: 30px;");
                out.print("}");
                out.print("</style>");
                out.print("</head>");
                out.print("<body>");
                out.print("<div class='container'>");
                out.print("<h1>部门详情</h1>");
                out.print("<hr />");
                out.print("<div class='info'>");
                out.print("<p><strong>部门编号："  + deptno +  "</strong> </p>");
                out.print("<p><strong>部门名称：" + dname + "</strong> </p>");
                out.print("<p><strong>部门位置：" + loc + "</strong> </p>");
                out.print("</div>");
                out.print("<input type='button' value='后退' onclick='window.history.back()' class='btn btn-primary'>");
                out.print("</div>");
                out.print("</body>");
                out.print("</html>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUil.close(connection, preparedStatement, resultSet);
        }
    }


    private void doSave(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String deptno = req.getParameter("depton");
        String dname = req.getParameter("dname");
        String loc = req.getParameter("loc");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int i = 0;
        try {
            connection = DBUil.getConnection();
            String sql = "insert into dept values(?,?,?);";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, deptno);
            preparedStatement.setString(2, dname);
            preparedStatement.setString(3, loc);
            i = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUil.close(connection, preparedStatement, null);
        }

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='utf-8'>");
        out.println("<title>Save Department</title>");
        out.println("<link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>Save Department</h1>");
        out.println("<hr>");

        if (i == 1) {
            out.println("<div class='alert alert-success' role='alert'>Department saved successfully!</div>");
        } else {
            out.println("<div class='alert alert-danger' role='alert'>Failed to save department!</div>");
        }

        out.println("<a href='" + req.getContextPath() + "/dept/list' class='btn btn-primary'>Back to Department List</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }


    private void doDel(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String deptno = req.getParameter("deptno");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int count = 0;
        try {
            connection = DBUil.getConnection();
            String sql = "delete from dept where deptno = ?;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, deptno);
            count = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUil.close(connection, preparedStatement, null);
        }

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='utf-8'>");
        out.println("<title>Delete Department</title>");
        out.println("<link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>Delete Department</h1>");
        out.println("<hr>");

        if (count == 1) {
            out.println("<div class='alert alert-success' role='alert'>Department deleted successfully!</div>");
            req.getRequestDispatcher("/dept/list").forward(req, resp);
        } else {
            out.println("<div class='alert alert-danger' role='alert'>Failed to delete department!</div>");
            req.getRequestDispatcher("/error.html").forward(req, resp);
        }

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }


    private void doEdit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        String deptno = req.getParameter("depton");
        String dname = req.getParameter("dname");
        String loc = req.getParameter("loc");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBUil.getConnection();
            String sql = "UPDATE dept SET dname=?, loc=? WHERE deptno=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, dname);
            preparedStatement.setString(2, loc);
            preparedStatement.setString(3, deptno);
            int rowsUpdated = preparedStatement.executeUpdate();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='utf-8'>");
            out.println("<title>Edit Department</title>");
            out.println("<link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h1>Edit Department</h1>");
            out.println("<hr>");

            if (rowsUpdated > 0) {
                out.println("<div class='alert alert-success' role='alert'>Department updated successfully!</div>");
                resp.sendRedirect(req.getContextPath() + "/dept/list");
            } else {
                out.println("<div class='alert alert-danger' role='alert'>Failed to update department!</div>");
                resp.sendRedirect(req.getContextPath() + "/error.html");
            }

            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUil.close(connection, preparedStatement, null);
        }
    }




    private void doSession(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        PrintWriter writer = resp.getWriter();
        writer.print(session);
        System.out.println(session);
    }
}
