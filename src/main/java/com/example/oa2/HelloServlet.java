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

        out.print("<html>");
        out.print("<head>");
        out.print("<meta charset='utf-8'>");
        out.print("<title>部门列表页面</title>");
        out.print("<script type='text/javascript'>");
        out.print("function del(deptno) {");
        out.print("if(window.confirm('确认删除吗?')) {");
        out.print("document.location.href='" + contextPath + "/dept/delete?deptno=' + encodeURIComponent(deptno);");
        out.print("}");
        out.print("}");
        out.print("</script>");
        out.print("<style>");
        out.print("body {");
        out.print("font-family: Arial, sans-serif;");
        out.print("text-align: center;");
        out.print("margin: 0;");
        out.print("padding: 0;");
        out.print("}");
        out.print("h1 {");
        out.print("margin-top: 20px;");
        out.print("}");
        out.print("table {");
        out.print("margin: 0 auto;");
        out.print("border-collapse: collapse;");
        out.print("width: 50%;");
        out.print("}");
        // 其他样式部分省略
        out.print("</style>");
        out.print("</head>");
        out.print("<body>");
        out.print("<h1>部门列表</h1>");
        out.print("<hr />");
        out.print("<table>");
        out.print("<tr>");
        out.print("<th>序号</th>");
        out.print("<th>部门编号</th>");
        out.print("<th>部门名称</th>");
        out.print("<th>部门位置</th>");
        out.print("<th>操作</th>");
        out.print("</tr>");

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
                out.print("<a href='javascript:void(0)' onclick='del(\"" + deptno + "\")'>删除</a>");
                out.print("<a href='/oa/edit.html'>修改</a>");
                out.print("<a href='" + contextPath + "/dept/detail?deptno=" + deptno + "'>详情</a>");
                out.print("</td>");
                out.print("</tr>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUil.close(connection, preparedStatement, resultSet);
        }

        out.print("</table>");
        out.print("<hr />");
        out.print("<a href='/oa/add.html'>新增部门</a>");
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
                // 其他样式部分省略
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            out.print("<input type='button' value='后退' onclick='window.history.back()'>");
            out.print("</div>");
            out.print("</body>");
            out.print("</html>");
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
        if(i == 1){
            resp.sendRedirect(req.getContextPath() + "/dept/list");
        } else {
            resp.sendRedirect(req.getContextPath() + "/error.html");
        }
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

        if(count == 1){
            req.getRequestDispatcher("/dept/list").forward(req, resp);
        } else {
            req.getRequestDispatcher("/error.html").forward(req, resp);
        }
    }

    private void doEdit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
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
            if (rowsUpdated > 0) {
                resp.sendRedirect(req.getContextPath() + "/dept/list");
            } else {
                resp.sendRedirect(req.getContextPath() + "/error.html");
            }
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
