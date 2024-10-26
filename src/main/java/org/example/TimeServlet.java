package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static java.lang.Integer.parseInt;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String timeZone = req.getParameter("timezone");
        String n = "";
        if (timeZone == null || timeZone.isEmpty()) {
            n = "";
        } else {
            if (timeZone.startsWith("UTC ")){
                n = "+" + (timeZone.substring(4));
            }else if (timeZone.startsWith("UTC-")){
                n = (timeZone.substring(3));
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeZone = "GMT" + n;
        TimeZone tz = TimeZone.getTimeZone(currentTimeZone);
        dateFormat.setTimeZone(tz);

        String currentTime = dateFormat.format(new Date());
        resp.setContentType("text/html");
        resp.getWriter().println(currentTime + " UTC" + n);
        resp.getWriter().close();
    }
}
