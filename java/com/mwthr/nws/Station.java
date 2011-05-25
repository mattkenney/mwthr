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

class Station extends LocatorImpl
{
    private class XmlHandler extends DefaultHandler
    {
        final String elementName;
        final StringBuilder buffer = new StringBuilder();
        Map props = null;

        XmlHandler(String elementName)
        {
            this.elementName = elementName;
        }

        public void characters(char[] ch, int start, int length)
            throws SAXException
        {
            buffer.append(ch, start, length);
        }

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
                props = Collections.unmodifiableMap(props);
                double[] where = getCoordinates(props);
                if (where != null && props.containsKey("station_id") && props.containsKey("xml_url"))
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

        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            if (elementName.equals(qName))
            {
                props = new LinkedHashMap();
                buffer.setLength(0);
            }
        }
    }

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
