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

public class RangeXmlCallable
    implements Callable<Map<String, String>>
{
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

    private static final SAXParserFactory factory;

    private final String urlString;
    private final Map<String, String> result = new LinkedHashMap<String, String>();

    static
    {
        factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
    }

    public RangeXmlCallable(String urlString)
    {
        this.urlString = urlString;
    }

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

//    public static void main(String[] args)
//        throws IOException
//    {
//        SAXParserFactory factory = SAXParserFactory.newInstance();
//        factory.setValidating(false);
//        
//        XmlHandler handler = new XmlHandler();
//        URL url = new URL("http://www.weather.gov/forecasts/xml/sample_products/browser_interface/ndfdXMLclient.php?product=time-series&maxt=maxt&mint=mint&pop12=pop12&sky=sky&wspd=wspd&wgust=wgust&wdir=wdir&end=2011-05-17T14:45:00&lat=40.00&lon=-75.00");
//        InputStream in = url.openStream();
//        try
//        {
//            factory.newSAXParser().parse(in, handler);
//        }
//        catch (SAXException e)
//        {
//            LOG.log(Level.WARNING, "SAXException", e);
//        }
//        catch (ParserConfigurationException e)
//        {
//            LOG.log(Level.WARNING, "ParserConfigurationException", e);
//        }
//        in.close();
//        
//        System.out.println(handler);
//    }

}
