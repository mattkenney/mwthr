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

/**
 * This filter adds weather data properties maps to requests.
 * The observation station location properties map must already have been
 * added to the request by the {@link com.mwthr.web.LocatorFilter}.
 * Current conditions and forecasts are retrieved from the
 * <a href="http://www.weather.gov/">US NWS</a>.
 */
public class WeatherFilter implements Filter
{
    /**
     * Empty weather properties map used in cache so that we won't try to get
     * them again for one hour.
     */
    @SuppressWarnings("unchecked") // empty map has no generic type parameters
    private static final Map<String, String> EMPTY_PROPS = Collections.EMPTY_MAP;

    /**
     * The <code>FilterConfig</code> passed to
     * {@link com.mwthr.web.WeatherFilter#init}.
     */
    private FilterConfig config = null;

    /**
     * Reference to the memcache from the servlet context.
     */
    private Map<Object, Object> cache = null;

    /**
     * No argument constructor.
     */
    public WeatherFilter()
    {
    }

    /**
     * Adds the current conditions weather properties for the specified
     * station to the request.
     * @param request the request
     * @param station the station properties map
     */
    private void addCurrent(HttpServletRequest request, Map<String, String> station)
    {
        String key = "current." + station.get("station_id");
        Map<String, String> current = getProperties(key);
        if (current == null)
        {
            String urlString = station.get("xml_url_www");

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
                current = EMPTY_PROPS;
            }

            // cache it
            cache.put(key, current);
        }
        request.setAttribute("current", current);
    }

    /**
     * Adds the forecast weather properties for the specified station to the
     * request.
     * @param request the request
     * @param station the station properties map
     */
    private void addForecast(HttpServletRequest request, Map<String, String> station)
    {
        String key = "forecast." + station.get("station_id");
        Map<String, String> forecast = getProperties(key);
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
                forecast = EMPTY_PROPS;
            }

            // cache it
            cache.put(key, forecast);
        }
        request.setAttribute("forecast", forecast);
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

        Map<String, String> station = getStation(request);
        if (station != null)
        {
            addCurrent(request, station);
            addForecast(request, station);
        }

        chain.doFilter(request, response);
    }

    /**
     * Returns the properties map for the specified key from the memcache.
     * @param key the key
     * @return the observation station location properties map
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> getProperties(String key)
    {
        return (Map) cache.get(key);
    }

    /**
     * Returns the observation station location properties map set by
     * {@link com.wthr.web.LocationFilter}, or <code>null</code> if no
     * location was set.
     * @param request the request
     * @return the observation station location properties map
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> getStation(HttpServletRequest request)
    {
        return (Map) request.getAttribute("station");
    }

    /**
     * See {@link javax.servlet.Filter#init}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void init(FilterConfig config)
        throws ServletException
    {
        this.config = config;
        ServletContext context = config.getServletContext();
        cache = (Map) context.getAttribute("cache");
    }
}
