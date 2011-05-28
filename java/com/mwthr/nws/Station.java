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

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Package-private implementation of {@link com.mwthr.nws.Locator#STATION}.
 * <p>The data file is from here:</p>
 * http://www.weather.gov/xml/current_obs/index.xml
 */
class Station extends LocatorImpl
{
    /**
     * SAX parser data handler that populates the map and the 2-dimentional
     * tree.
     */
    private class XmlHandler extends DefaultHandler
    {
        final String elementName;
        final StringBuilder buffer = new StringBuilder();
        Map<String, String> props = null;

        XmlHandler(String elementName)
        {
            this.elementName = elementName;
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
            if (elementName.equals(qName))
            {
                if (props.containsKey("latitude"))
                {
                    props.put("lat", props.get("latitude"));
                }
                if (props.containsKey("longitude"))
                {
                    props.put("lon", props.get("longitude"));
                }
                if (props.containsKey("xml_url"))
                {
                    String value = props.get("xml_url");
                    if (value.startsWith("http://weather.gov/"))
                    {
                        value = "http://www.weather.gov/".concat(value.substring(19));
                    }
                    props.put("xml_url_www", value);
                }
                props = Collections.unmodifiableMap(props);
                double[] where = getCoordinates(props);
                if (where != null && props.containsKey("station_id") && props.containsKey("xml_url_www"))
                {
                    tree.insert(where, props);
                    codeMap.put(props.get("station_id"), props);
                }
                props = null;
            }
            else if (props != null)
            {
                String value = buffer.toString().trim();
                if (value.length() > 0)
                {
                    props.put(qName, value);
                }
                buffer.setLength(0);
            }
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            if (elementName.equals(qName))
            {
                props = new LinkedHashMap<String, String>();
                buffer.setLength(0);
            }
        }
    }

    /**
     * No argument constructor. Loads the data file and populates the map and
     * the 2-dimentional tree.
     */
    Station()
    {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            XmlHandler handler = new XmlHandler("station");

            InputStream in = getClass().getResourceAsStream(getFilename());
            factory.newSAXParser().parse(in, handler);
            in.close();
        }
        catch (Exception e)
        {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem loading \"" + getFilename() + "\"", e);
        }
    }

    /**
     * Returns filename of the data file.
     */
    @Override
    String getFilename()
    {
        String result = getClass().getName();
        int offset = result.lastIndexOf('.');
        if (offset >= 0)
        {
            result = result.substring(offset + 1);
        }
        result += ".xml";
        return result;
    }
}
