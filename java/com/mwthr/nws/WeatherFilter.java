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

import net.sf.javaml.core.kdtree.KDTree;

import com.mwthr.web.GeoIP;
import com.mwthr.web.PathMatcher;

public class WeatherFilter implements Filter
{
    private FilterConfig config = null;
    private KDTree zoneTree = null;
    private Map zoneMap = null;
    private KDTree radarTree = null;
    private Map radarMap = null;

    public WeatherFilter()
    {
    }

    public void destroy()
    {
        zoneTree = null;
        zoneMap = null;
        radarTree = null;
        radarMap = null;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        PathMatcher path = new PathMatcher(request.getRequestURI());
        CountyWarningArea cwa = null;
        Nexrad radar = null;

        if (path.matches("^/+$"))
        {
            double[] where = GeoIP.getCoordinates(config.getServletContext(), request);
            cwa = (where == null) ? null : (CountyWarningArea) zoneTree.nearest(where);
            radar = (where == null) ? null : (Nexrad) radarTree.nearest(where);
        }
        else if (path.matches("^/+cwa/+([0-9]{3})$"))
        {
            cwa = (CountyWarningArea) zoneMap.get(path.group(1).toUpperCase());
            double[] where = (cwa == null) ? null : cwa.getCoordinates();
            radar = (where == null) ? null : (Nexrad) radarTree.nearest(where);
            if (radar == null)
            {
                response.sendError(404);
                return;
            }
        }
        else if (path.matches("^/+icao/+([a-z]{4})$"))
        {
            radar = (Nexrad) radarMap.get(path.group(1).toUpperCase());
            double[] where = (radar == null) ? null : radar.getCoordinates();
            cwa = (where == null) ? null : (CountyWarningArea) zoneTree.nearest(where);
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
            dispatcher = request.getRequestDispatcher("/index.jsp");
        }
        dispatcher.forward(request, response);
    }

    public void init(FilterConfig config)
        throws ServletException
    {
        this.config = config;
        zoneTree = CountyWarningArea.getKDTree();
        zoneMap = CountyWarningArea.getMap();
        radarTree = Nexrad.getKDTree();
        radarMap = Nexrad.getMap();
    }
}
