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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WeatherFilter implements Filter
{
    private FilterConfig config = null;
    private Map cache = null;

    public WeatherFilter()
    {
    }

    private void addCurrent(HttpServletRequest request, Map cache, Map station)
    {
        String key = "current." + station.get("station_id");
        Map current = (Map) cache.get(key);
        if (current == null)
        {
            String urlString = (String) station.get("xml_url_www");

            // hit weather.gov for the data
            try
            {
                current = new RestXmlCallable(urlString, "current_observation").call();
            }
            catch (Exception e)
            {
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem getting \"" + urlString + "\"", e);
            }

            // we want to cache failure for one hour too
            // so we put an empty map in the cache
            if (current == null)
            {
                current = Collections.EMPTY_MAP;
            }

            // cache it
            cache.put(key, current);
        }
        request.setAttribute("current", current);
    }

    private void addForecast(HttpServletRequest request, Map cache, Map station)
    {
        String key = "forecast." + station.get("station_id");
        Map forecast = (Map) cache.get(key);
        if (forecast == null)
        {
            // UTC end time 28 hours in the future
            // since forecast periods can be three hours and we cache for one hour
            // we add four hours to capture all possible extremes
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            Calendar cal = format.getCalendar();
            cal.setTimeInMillis(System.currentTimeMillis());
            cal.add(Calendar.HOUR_OF_DAY, 28);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            // build the NDFD URL
            StringBuilder buffer = new StringBuilder();
            buffer.append("http://www.weather.gov/forecasts/xml/sample_products/browser_interface/ndfdXMLclient.php");
            buffer.append("?product=time-series&maxt=maxt&mint=mint&pop12=pop12&sky=sky&wspd=wspd&wgust=wgust&wdir=wdir");
            buffer.append("&end=");
            buffer.append(format.format(cal.getTime()));
            buffer.append("&lat=");
            buffer.append(station.get("lat"));
            buffer.append("&lon=");
            buffer.append(station.get("lon"));
            String urlString = buffer.toString();

            // hit weather.gov for the data
            try
            {
                forecast = new RangeXmlCallable(urlString).call();
            }
            catch (Exception e)
            {
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem getting \"" + urlString + "\"", e);
            }

            // we want to cache failure for one hour too
            // so we put an empty map in the cache
            if (forecast == null)
            {
                forecast = Collections.EMPTY_MAP;
            }

            // cache it
            cache.put(key, forecast);
        }
        request.setAttribute("forecast", forecast);
    }

    @Override
    public void destroy()
    {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        ServletContext context = config.getServletContext();
        Map cache = (Map) context.getAttribute("cache");
        Map station = (Map) request.getAttribute("station");

        if (station != null)
        {
            addCurrent(request, cache, station);
            addForecast(request, cache, station);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig config)
        throws ServletException
    {
        this.config = config;
        ServletContext context = config.getServletContext();
        cache = (Map) context.getAttribute("cache");
    }
}
