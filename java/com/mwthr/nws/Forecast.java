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
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Forecast
    extends WeatherDataHandler
{
    /**
     * Range values used while extracting values from the XML.
     */
    private static class Range
    {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        public String toString()
        {
            StringBuilder buffer = new StringBuilder();
            buffer.append(min);
            if (max > min)
            {
                buffer.append('-');
                buffer.append(max);
            }
            return buffer.toString();
        }

        void update(int value)
        {
            min = Math.min(min, value);
            max = Math.max(max, value);
        }
    }

    /**
     * SAX parser data handler that populates the map.
     */
    private static class XmlHandler extends DefaultHandler
    {
        final Map<String, String> result;
        Map<String, Range> rangeMap = new LinkedHashMap<String, Range>();
        StringBuilder buffer = new StringBuilder();
        String key = null;
        String creationDate = null;

        XmlHandler(Map<String, String> result)
        {
            this.result = result;
        }

        @Override
        public void characters(char[] ch, int start, int length)
            throws SAXException
        {
            buffer.append(ch, start, length);
        }

        @Override
        public void endDocument()
            throws SAXException
        {
            for (Map.Entry<String, Range> entry : rangeMap.entrySet())
            {
                result.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            if (creationDate != null)
            {
                result.put("creation-date", creationDate);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
            throws SAXException
        {
            if (key != null && "value".equals(qName))
            {
                try
                {
                    int value = Integer.valueOf(buffer.toString());
                    Range theRange = rangeMap.get(key);
                    if (theRange == null)
                    {
                        theRange = new Range();
                        rangeMap.put(key, theRange);
                    }
                    theRange.update(value);
                }
                catch (NumberFormatException e)
                {
                    // ignore
                }
            }
            else if ("creation-date".equals(qName))
            {
                creationDate = buffer.toString();
            }
        }

        @Override
        public InputSource resolveEntity(String publicID, String systemID)
            throws IOException, SAXException
        {
            return new InputSource(new StringReader(""));
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            if ("value".equals(qName) || "name".equals(qName) || "creation-date".equals(qName))
            {
                buffer.setLength(0);
            }
            else
            {
                key = qName;
            }
        }
    }

    /**
     * 90 minutes.
     */
    private static final long CACHE_LIFE = 5400000L;

    private final DateFormat format;

    private final List<Map<String, String>> stations;

    private final long cutoff;

    /**
     * Constructor.
     * @param stations observation station properties maps
     */
    public Forecast(List<Map<String, String>> stations)
    {
        this.stations = stations;

        // get max timestamp for a valid cache entry
        cutoff = System.currentTimeMillis() - CACHE_LIFE;

        // format for parsing observation timestamp
        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Override
    public List<URL> getURLs()
    {
        List<URL> result = new ArrayList<URL>();

        // UTC end time 24 hours in the future
        Calendar cal = format.getCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        StringBuilder buffer = new StringBuilder();
        buffer.append("http://www.weather.gov/forecasts/xml/sample_products/browser_interface/ndfdXMLclient.php");
        buffer.append("?product=time-series&temp=temp&pop12=pop12");
        buffer.append("&end=");
        buffer.append(format.format(cal.getTime()));
        int length = buffer.length();

        for (Map<String, String> station : stations)
        {
            // build the NDFD URL
            buffer.setLength(length);
            buffer.append("&lat=");
            buffer.append(station.get("lat"));
            buffer.append("&lon=");
            buffer.append(station.get("lon"));
            String urlString = buffer.toString();
            try
            {
                result.add(new URL(urlString));
            }
            catch (Exception e)
            {
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem parsing \"" + urlString + "\"", e);
            }
        }
        return result;
    }

    @Override
    protected DefaultHandler getXMLHandler(Map<String, String> props)
    {
        return new XmlHandler(props);
    }

    @Override
    public boolean isValid(Map<String, String> props)
    {
        String text = props.get("creation-date");
        long timestamp = parseDate(format, text);
        boolean result = (timestamp > cutoff);
        Calendar cal = format.getCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        Logger.getLogger(getClass().getName()).log(result ? Level.FINE : Level.INFO, text + ", parsed " + format.format(cal.getTime()) + ", returning " + result);
        return result;
    }
}
