/*
 * Copyright 2011 Matt Kenney
 *
 * This file is part of mwthr.
 *
 * mwthr is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * mwthr is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with mwthr.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mwthr.nws;

import java.io.IOException;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mwthr.web.GeoIP;
import com.mwthr.web.PathMatcher;

public class WeatherFilter implements Filter
{
    private FilterConfig config = null;

    public WeatherFilter()
    {
    }

    public void destroy()
    {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        PathMatcher path = new PathMatcher(request.getRequestURI());
        Map cwa = null;
        Map radar = null;

        if (path.matches("^/+$"))
        {
            double[] where = GeoIP.getCoordinates(config.getServletContext(), request);
            cwa = Locator.CWA.nearest(where);
            radar = Locator.NEXRAD.nearest(where);
        }
        else if (path.matches("^/+cwa/+([0-9]{3})$"))
        {
            cwa = Locator.CWA.get(path.group(1).toUpperCase());
            radar = Locator.NEXRAD.nearest(cwa);
            if (radar == null)
            {
                response.sendError(404);
                return;
            }
        }
        else if (path.matches("^/+icao/+([a-z]{4})$"))
        {
            radar = Locator.NEXRAD.get(path.group(1).toUpperCase());
            cwa = Locator.CWA.nearest(radar);
            if (cwa == null)
            {
                response.sendError(404);
                return;
            }
        }
        else if (path.matches("^/+((cwa)|(icao))/+$"))
        {
            // just forward to "/national.jsp"
        }
        else if (path.matches("^/+((cwa)|(icao))$"))
        {
            response.sendRedirect(request.getRequestURI() + "/");
            return;
        }
        else
        {
            response.sendError(404);
            return;
        }

        request.setAttribute("cwa", cwa);
        request.setAttribute("radar", radar);
        RequestDispatcher dispatcher = null;
        if (cwa == null || radar == null)
        {
            dispatcher = request.getRequestDispatcher("/national.jsp");
        }
        else
        {
            dispatcher = request.getRequestDispatcher("/weather.jsp");
        }
        dispatcher.forward(request, response);
    }

    public void init(FilterConfig config)
        throws ServletException
    {
        this.config = config;
    }
}
