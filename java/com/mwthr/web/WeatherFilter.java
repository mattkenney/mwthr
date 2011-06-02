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
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
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

import com.mwthr.nws.Locator;

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
     * The time after which a cache entry will not be considered valid; 3600000 ms.
     */
    private static final long CACHE_LIFE = 3600000L;

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
    private void addCurrent(HttpServletRequest request)
    {
        // get max timestamp for a valid cache entry
        long cutoff = System.currentTimeMillis() - CACHE_LIFE;

        List<Map<String, String>> stations = getStations(request);

        // get the cached current observations, if present
        Map<String, String> current = null;
        DateFormat format = getRfc822DateFormat();
        for (int n = 0; stations != null && n < stations.size(); n++)
        {
            Map<String, String> station = stations.get(n);
            String key = "current." + station.get("station_id");
            current = getProperties(key);
            String text = (current == null) ? null : current.get("observation_time_rfc822");
            long timestamp = parseDate(format, text);

            // get a new current observations if there is not a recent cached one
            if (cutoff > timestamp)
            {
                String urlString = station.get("xml_url_www");

                // hit weather.gov for the data, up to two attempts
                for (int i = 0; current == null && i < 2; i++)
                {
                    try
                    {
                        current = new RestXmlCallable(urlString, "current_observation").call();
                    }
                    catch (Exception e)
                    {
                        Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem getting \"" + urlString + "\"", e);
                    }
                }

                // we want to cache failure for one hour too
                // so we put an fake map in the cache
                text = (current == null) ? null : current.get("observation_time_rfc822");
                timestamp = parseDate(format, text);
                if (cutoff > timestamp)
                {
                    current = new LinkedHashMap<String, String>();
                    current.put("observation_time_rfc822", format.format(new Date()));
                }

                // cache it
                cache.put(key, current);
            }

            // if the station's observations are recent, make it THE station
            if (cutoff <= timestamp)
            {
                request.setAttribute("station", station);
                break;
            }
        }
        request.setAttribute("current", current);
    }

    /**
     * Adds the forecast weather properties for the specified station to the
     * request.
     * @param request the request
     * @param station the station properties map
     */
    private void addForecast(HttpServletRequest request)
    {
        // get max timestamp for a valid cache entry
        long cutoff = System.currentTimeMillis() - CACHE_LIFE;

        Map<String, String> station = getStation(request);

        if (station != null)
        {
            // get the cached forecast, if present
            String key = "forecast." + station.get("station_id");
            Map<String, String> forecast = getProperties(key);

            // get the timestamp of the forecast
            DateFormat format = getIsoDateFormat();
            String text = (forecast == null) ? null : forecast.get("creation-date");
            long timestamp = parseDate(format, text);

            // get a new forecast if there is not a recent cached one
            if (cutoff > timestamp)
            {
                // UTC end time 28 hours in the future
                // since forecast periods can be three hours and we cache for one hour
                // we add four hours to capture all possible extremes
                Calendar cal = format.getCalendar();
                cal.setTimeInMillis(System.currentTimeMillis());
                cal.add(Calendar.HOUR_OF_DAY, 28);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                // build the NDFD URL
                StringBuilder buffer = new StringBuilder();
                buffer.append("http://www.weather.gov/forecasts/xml/sample_products/browser_interface/ndfdXMLclient.php");
                buffer.append("?product=time-series&maxt=maxt&mint=mint&pop12=pop12");
                buffer.append("&end=");
                buffer.append(format.format(cal.getTime()));
                buffer.append("&lat=");
                buffer.append(station.get("lat"));
                buffer.append("&lon=");
                buffer.append(station.get("lon"));
                String urlString = buffer.toString();

                // hit weather.gov for the data, up to two attempts
                for (int i = 0; forecast == null && i < 2; i++)
                {
                    try
                    {
                        forecast = new RangeXmlCallable(urlString).call();
                    }
                    catch (Exception e)
                    {
                        Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem getting \"" + urlString + "\"", e);
                    }
                }

                // we want to cache failure for one hour too
                // so we put an fake map in the cache
                if (forecast == null)
                {
                    forecast = new LinkedHashMap<String, String>();
                    forecast.put("creation-date", format.format(new Date()));
                }

                // cache it
                cache.put(key, forecast);
            }
            request.setAttribute("forecast", forecast);
        }
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

        addCurrent(request);
        addForecast(request);

        chain.doFilter(request, response);
    }

    /**
     * Returns the properties map for the specified key from the cache.
     * @param key the key
     * @return the observation station location properties map
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> getProperties(String key)
    {
        return (Map) cache.get(key);
    }

    /**
     * Returns a new UTC ISO 8601 <code>DateFormat</code>.
     * @return a new UTC ISO 8601 <code>DateFormat</code>
     */
    private DateFormat getIsoDateFormat()
    {
        DateFormat result = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        result.setTimeZone(TimeZone.getTimeZone("GMT"));
        return result;
    }

    /**
     * Returns a new UTC RFC 822 <code>DateFormat</code>.
     * @return a new UTC RFC 822 <code>DateFormat</code>
     */
    private DateFormat getRfc822DateFormat()
    {
        DateFormat result = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
        result.setTimeZone(TimeZone.getTimeZone("GMT"));
        return result;
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
     * Returns the observation station locations properties maps set by
     * {@link com.wthr.web.LocationFilter}, or <code>null</code> if no
     * locations were set.
     * @param request the request
     * @return the observation station locations properties maps
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, String>> getStations(HttpServletRequest request)
    {
        return (List) request.getAttribute("stations");
    }

    /**
     * Returns the milliseconds since the epoch parsed from the
     * specified date string using the specified {@ java.text.DateFormat }.
     * The return value is zero if the string is null or it cannot be parsed.
     * @param format the <code>DateFormat</code>
     * @param text the date string
     * @return the miliseconds since the epoch
     */
    private long parseDate(DateFormat format, String text)
    {
        long result = 0L;
        if (text != null)
        {
            try
            {
                Date when = format.parse(text);
                result = when.getTime();
            }
            catch (ParseException e)
            {
                // just return 0L
            }
        }
        return result;
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
