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

/**
 * This class fetches XML data from a URL and returns it as a
 * {@link java.util.Map java.util.Map}.
 */
public class Observation
    extends WeatherDataHandler
{
    /**
     * SAX parser data handler that populates the map.
     */
    private static class XmlHandler extends DefaultHandler
    {
        final Map<String, String> result;
        final StringBuilder buffer = new StringBuilder();
        boolean hasStartElement = false;
        boolean hasEndElement = false;

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
        public void endElement(String uri, String localName, String qName)
            throws SAXException
        {
            if (ELEMENT_NAME.equals(qName))
            {
                hasEndElement = true;
            }
            else if (hasStartElement && !hasEndElement)
            {
                String value = buffer.toString().trim();
                if (value.length() > 0)
                {
                    result.put(qName, value);
                }
            }
            buffer.setLength(0);
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
            if (ELEMENT_NAME.equals(qName))
            {
                hasStartElement = true;
            }
            buffer.setLength(0);
        }
    }

    /**
     * 90 minutes.
     */
    private static final long CACHE_LIFE = 5400000L;

    /**
     * The name of the element from which to extract values.
     */
    private static final String ELEMENT_NAME = "current_observation";

    private final DateFormat format;

    private final List<Map<String, String>> stations;

    private final long cutoff;

    /**
     * Constructor.
     * @param stations observation station properties maps
     */
    public Observation(List<Map<String, String>> stations)
    {
        this.stations = stations;

        // get max timestamp for a valid cache entry
        cutoff = System.currentTimeMillis() - CACHE_LIFE;

        // format for parsing observation timestamp
        format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    @Override
    public List<URL> getURLs()
    {
        List<URL> result = new ArrayList<URL>();
        for (Map<String, String> station : stations)
        {
            try
            {
                result.add(new URL(station.get("xml_url_www")));
            }
            catch (Exception e)
            {
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem parsing \"" + station.get("xml_url_www") + "\"", e);
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
        String text = props.get("observation_time_rfc822");
Logger.getLogger(getClass().getName()).log(Level.INFO, "observation_time_rfc822: " + text);
        long timestamp = parseDate(format, text);
        return (timestamp > cutoff);
    }
}
