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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;

import com.mwthr.gae.URLDataHandler;

public abstract class WeatherDataHandler
    implements URLDataHandler
{
    /**
     * SAX parser factory.
     */
    private static final SAXParserFactory factory;

    static
    {
        factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
    }

    /**
     * Constructor.
     * @param stations observation station properties maps
     */
    protected WeatherDataHandler()
    {
    }

    protected abstract DefaultHandler getXMLHandler(Map<String, String> props);

    /**
     * Returns the date parsed from the
     * specified date string using the specified {@ java.text.DateFormat }.
     * The return value is zero if the string is null or it cannot be parsed.
     * @param format the <code>DateFormat</code>
     * @param text the date string
     * @return the date
     */
    static long parseDate(DateFormat format, String text)
    {
        long result = 0L;
        if (text != null)
        {
            try
            {
                Date when = format.parse(text);
                result = when.getTime();
            }
            catch (Exception e)
            {
                // just return oL
            }
        }
        return result;
    }

    public Map<String, String> parse(URL dataURL, byte[] data)
    {
        Map<String, String> result = new LinkedHashMap<String, String>();
        DefaultHandler handler = getXMLHandler(result);
        InputStream in = new ByteArrayInputStream(data);
        try
        {
            SAXParser parser = factory.newSAXParser();
            parser.parse(in, handler);
        }
        catch (Exception e)
        {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem parsing XML", e);
        }
        return result;
    }
}
