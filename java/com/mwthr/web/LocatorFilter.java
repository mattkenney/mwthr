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
package com.mwthr.web;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
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

import com.mwthr.gae.URLDataFetcher;
import com.mwthr.nws.Forecast;
import com.mwthr.nws.Graph;
import com.mwthr.nws.Locator;
import com.mwthr.nws.Observation;

/**
 * This filter functions as the controller for the application. It determines
 * what data to display based on the request and forwards to the appropriate
 * page.
 */
public class LocatorFilter implements Filter
{
    /**
     * The number of nearest observation stations to find.
     */
    private static final int STATION_COUNT = 4;

    /**
     * The <code>FilterConfig</code> passed to
     * {@link com.mwthr.web.LocatorFilter#init}.
     */
    private FilterConfig config = null;

    private URLDataFetcher fetcher = null;

    /**
     * No argument constructor.
     */
    public LocatorFilter()
    {
    }

    /**
     * See {@link javax.servlet.Filter#destroy}.
     */
    @Override
    public void destroy()
    {
    }

    /**
     * See {@link javax.servlet.Filter#doFilter}.
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        PathMatcher path = new PathMatcher(request.getRequestURI());
        List<Map<String, String>> stations = null;
        Map<String, String> cwa = null;
        Map<String, String> radar = null;
        String duration = null;
        boolean showPicker = false;

        if (path.matches("^/+(24|72|120|168)?$"))
        {
            duration = path.group(1);
            double[] where = getCoordinates(request);
            stations = Locator.STATION.nearest(where, STATION_COUNT);
            showPicker = stations.isEmpty();
            if (showPicker)
            {
                where = GeoIP.getCoordinates(config.getServletContext(), request);
                stations = Locator.STATION.nearest(where, STATION_COUNT);
            }
            cwa = Locator.CWA.nearest(where);
            radar = Locator.NEXRAD.nearest(where);
        }
        else if (path.matches("^/+icao/+([a-z]{4})/(24|72|120|168)?$"))
        {
            duration = path.group(2);
            radar = Locator.NEXRAD.get(path.group(1).toUpperCase());
            stations = Locator.STATION.nearest(radar, STATION_COUNT);
            cwa = Locator.CWA.nearest(radar);
            if (stations.isEmpty() || cwa == null)
            {
                response.sendError(404);
                return;
            }
        }
        else if (path.matches("^/+cwa/+([0-9]{3})/(24|72|120|168)?$"))
        {
            duration = path.group(2);
            cwa = Locator.CWA.get(path.group(1).toUpperCase());
            stations = Locator.STATION.nearest(cwa, STATION_COUNT);
            radar = Locator.NEXRAD.nearest(cwa);
            if (stations.isEmpty() || radar == null)
            {
                response.sendError(404);
                return;
            }
        }
        else if (path.matches("^/+station/+([a-z]{4})/(24|72|120|168)?$"))
        {
            duration = path.group(2);
            Map<String, String> station = Locator.STATION.get(path.group(1).toUpperCase());
            radar = Locator.NEXRAD.nearest(station);
            cwa = Locator.CWA.nearest(station);
            if (radar == null || cwa == null)
            {
                response.sendError(404);
                return;
            }
            stations = Collections.singletonList(station);
        }
        else if (path.matches("^/+((cwa)|(icao)|(station))/+$"))
        {
            // just forward to "/national.jsp"
        }
        else if (path.matches("^/+((cwa)|(icao)|(station))(/[0-9a-z]{3,4})?$"))
        {
            response.sendRedirect(request.getRequestURI() + "/");
            return;
        }
        else
        {
            response.sendError(404);
            return;
        }

        request.setAttribute("stations", stations);
        request.setAttribute("cwa", cwa);
        request.setAttribute("radar", radar);
        RequestDispatcher dispatcher = null;
        if (stations == null || stations.isEmpty() || cwa == null || radar == null)
        {
            dispatcher = request.getRequestDispatcher("/national.jsp");
        }
        else if (showPicker)
        {
            dispatcher = request.getRequestDispatcher("/index.jsp");
        }
        else if (duration != null)
        {
            URLDataFetcher.Result current = fetcher.getResult(new Observation(stations));
            URLDataFetcher.Result graph = fetcher.getResult(new Graph(stations));
            request.setAttribute("current", current.getData());
            request.setAttribute("graphurl", graph.getData().get("graph" + duration));
            request.setAttribute("duration", duration);
            dispatcher = request.getRequestDispatcher("/graph.jsp");
        }
        else
        {
            URLDataFetcher.Result current = fetcher.getResult(new Observation(stations));
            URLDataFetcher.Result forecast = fetcher.getResult(new Forecast(stations));
            request.setAttribute("current", current.getData());
            request.setAttribute("forecast", forecast.getData());
            dispatcher = request.getRequestDispatcher("/weather.jsp");
        }
        dispatcher.forward(request, response);
    }

    /**
     * Returns the latitude/longitude from request parameters, or null
     * if not present or not valid.
     */
    double[] getCoordinates(HttpServletRequest request)
    {
        double[] result = null;
        String lat = request.getParameter("lat");
        String lon = request.getParameter("lon");
        if (lat != null && lon != null)
        {
            try
            {
                double latitude = Double.valueOf(lat);
                double longitude = Double.valueOf(lon);
                if (-90.0 <= latitude && latitude <= 90.0 && -180.0 <= longitude && longitude <= 180.0)
                {
                    result = new double[] { latitude, longitude };
                }
            }
            catch (NumberFormatException e)
            {
                // just return null
            }
        }
        return result;
    }

    /**
     * See {@link javax.servlet.Filter#init}.
     */
    @Override
    public void init(FilterConfig config)
        throws ServletException
    {
        this.config = config;
        fetcher = new URLDataFetcher();
    }
}
