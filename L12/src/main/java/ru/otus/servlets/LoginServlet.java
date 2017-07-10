package ru.otus.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoginServlet extends HttpServlet{
    private static final String LOGIN_PARAM_NAME = "name";
    private static final String PASSWORD_PARAM_NAME = "password";

    private static final String NAME = "admin";
    private static final String PAS = "admin";

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String requestLogin = request.getParameter(LOGIN_PARAM_NAME);
        String requestPassword = request.getParameter(PASSWORD_PARAM_NAME);

        if(requestLogin.equals(NAME) && requestPassword.equals(PAS)) {
            response.sendRedirect("admin.html");
        } else {
            response.sendRedirect("error.html");
        }
    }
}
