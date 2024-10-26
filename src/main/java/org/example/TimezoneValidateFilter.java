package org.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String timeZone = req.getParameter("timezone");
        boolean validTimeZone;

        Pattern pattern = Pattern.compile("^UTC (0|1[0-2]|[1-9])|UTC-([1-9]|1[0-2])$");
        if (timeZone != null && !timeZone.isEmpty()) {
            Matcher matcher = pattern.matcher(timeZone);
            if (matcher.matches()) {
                validTimeZone = true;
            } else {
                validTimeZone = false;
                for (String tz : TimeZone.getAvailableIDs()) {
                    if (tz.equals(timeZone)) {
                        validTimeZone = true;
                        break;
                    }
                }
            }
            if (!validTimeZone) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.getWriter().write("Invalid timezone");
                return;
            }
        }
        chain.doFilter(req, res);
    }
}
