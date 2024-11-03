package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("HTML");
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String timeZone = req.getParameter("timezone");
        String offset = "";
        if (timeZone == null || timeZone.isEmpty()) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null){
                for (Cookie cookie : cookies){
                    if ("timezone".equals(cookie.getName())){
                        timeZone = cookie.getValue();
                        break;
                    }
                }
            }
        }else {
            timeZone = timeZone.trim();
            timeZone = timeZone.replace(" ", "_");
            Cookie timezoneCookie = new Cookie("timezone", timeZone);
            timezoneCookie.setMaxAge(60 * 60 * 24);
            resp.addCookie(timezoneCookie);
        }
        if (timeZone == null || timeZone.isEmpty()) {
            offset = "";
        } else {
            if (timeZone.startsWith("UTC_")){
                offset = "+" + (timeZone.substring(4));
            }else if (timeZone.startsWith("UTC-")){
                offset = (timeZone.substring(3));
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeZone = "GMT" + offset;
        TimeZone tz = TimeZone.getTimeZone(currentTimeZone);
        dateFormat.setTimeZone(tz);

        String currentTime = dateFormat.format(new Date());
        Context context = new Context();
        context.setVariable("currentTime", currentTime);
        context.setVariable("timeZone", offset);

        resp.setContentType("text/html;charset=UTF-8");
        templateEngine.process("time", context, resp.getWriter());
    }
}
