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

import java.io.InputStream;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RestXmlCallable
    implements Callable<Map<String, String>>
{
    private class XmlHandler extends DefaultHandler
    {
        final StringBuilder buffer = new StringBuilder();
        boolean hasStartElement = false;
        boolean hasEndElement = false;

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
        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            if (elementName.equals(qName))
            {
                hasStartElement = true;
            }
            buffer.setLength(0);
        }
    }

    private static final SAXParserFactory factory;

    private final String urlString;
    private final String elementName;
    private final Map<String, String> result = new LinkedHashMap<String, String>();

    static
    {
        factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
    }

    public RestXmlCallable(String urlString, String elementName)
    {
        this.urlString = urlString;
        this.elementName = elementName;
    }

    @Override
    public Map<String, String> call()
        throws Exception
    {
        InputStream in = null;
        try
        {
            URL restUrl = new URL(urlString);
            SAXParser parser = factory.newSAXParser();
            DefaultHandler handler = new XmlHandler();
            in = restUrl.openStream();
            parser.parse(in, handler);
        }
        catch (Exception e)
        {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem getting \"" + urlString + "\"", e);
            throw e;
        }
        finally
        {
            if (in != null)
            {
                in.close();
            }
        }
        return result;
    }
}
