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

/**
 * This class fetches XML data from a URL and returns it as a
 * {@link java.util.Map java.util.Map}. The data values in the XML should be
 * in &lt;value&gt; children and be integers, and multiple data values will be
 * combined into a range in the result map with the parent element name as the
 * key.
 * <p>
 * For example, this data:
 * <pre>
 *    &lt;temperature&gt;
 *        &lt;name&gt;Temperature&lt;/name&gt;
 *        &lt;value&gt;84&lt;/value&gt;
 *        &lt;value&gt;87&lt;/value&gt;
 *        &lt;value&gt;94&lt;/value&gt;
 *        &lt;value&gt;92&lt;/value&gt;
 *        &lt;value&gt;89&lt;/value&gt;
 *        &lt;value&gt;82&lt;/value&gt;
 *        &lt;value&gt;77&lt;/value&gt;
 *    &lt;/temperature&gt;
 * </pre>
 * will become key "temperature", value "77-94" in the result map.
 */
public class RangeXmlCallable
    implements Callable<Map<String, String>>
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
        Map<String, Range> rangeMap = new LinkedHashMap<String, Range>();
        StringBuilder buffer = new StringBuilder();
        String key = null;

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
        }

        Map<String, String> getMap()
        {
            Map<String, String> result = new LinkedHashMap<String, String>();
            for (Map.Entry<String, Range> entry : rangeMap.entrySet())
            {
                result.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            return result;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            if ("value".equals(qName) || "name".equals(qName))
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
     * SAX parser factory.
     */
    private static final SAXParserFactory factory;

    /**
     * The URL from which to retrieve the XML.
     */
    private final String urlString;

    /**
     * The map where the extracted data is stored.
     */
    private final Map<String, String> result = new LinkedHashMap<String, String>();

    static
    {
        factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
    }

    /**
     * Constructor.
     * @param urlString the URL from which to retrieve the XML
     */
    public RangeXmlCallable(String urlString)
    {
        this.urlString = urlString;
    }

    /**
     * Returns the map of data extracted.
     */
    @Override
    public Map<String, String> call()
        throws Exception
    {
        InputStream in = null;
        XmlHandler handler = new XmlHandler();
        try
        {
            URL restUrl = new URL(urlString);
            SAXParser parser = factory.newSAXParser();
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
        return handler.getMap();
    }
}
