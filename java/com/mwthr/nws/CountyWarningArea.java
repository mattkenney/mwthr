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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

class CountyWarningArea extends LocatorImpl
{
    private static final Charset LOAD_CHARSET;

    static
    {
        Charset encoding = null;
        try
        {
            encoding = Charset.forName("windows-1252");
        }
        catch (IllegalArgumentException e)
        {
            encoding = Charset.forName("ISO-8859-1");
        }
        LOAD_CHARSET = encoding;
    }

    CountyWarningArea()
    {
        try
        {
            InputStream in = getClass().getResourceAsStream(getFilename());
            
            BufferedReader lines = new BufferedReader(new InputStreamReader(in, LOAD_CHARSET));
            for (String line = lines.readLine(); line != null; line = lines.readLine())
            {
                Map props = parseLine(line);
                double[] where = getCoordinates(props);
                if (where != null && props.containsKey("zone") && props.containsKey("name") && props.containsKey("state") && props.containsKey("fips"))
                {
                    tree.insert(where, props);
                    codeMap.put(props.get("icao"), props);
                }
            }
            
            in.close();
        }
        catch (Exception e)
        {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem loading \"" + getFilename() + "\"", e);
        }
    }

    Map parseLine(String line)
    {
        Map props = new LinkedHashMap(11);
        if (line != null)
        {
            final int length = line.length();
            for (int i = 0, start = 0; i < 11 && start < length; i++)
            {
                int end = line.indexOf('|', start);
                if (end < 0)
                {
                    end = length;
                }
                if (end > start)
                {
                    String field = line.substring(start, end);
                    switch (i)
                    {
                    case 0:
                        props.put("state", field);
                        break;
                    case 1:
                        props.put("zone", field);
                        break;
                    case 2:
                        props.put("cwa", field);
                        break;
                    case 3:
                        props.put("name", field);
                        break;
                    case 4:
                        props.put("state_zone", field);
                        break;
                    case 5:
                        props.put("countyname", field);
                        break;
                    case 6:
                        props.put("fips", field);
                        break;
                    case 7:
                        props.put("time_zone", field);
                        break;
                    case 8:
                        props.put("fe_area", field);
                        break;
                    case 9:
                        props.put("lat", field);
                        break;
                    case 10:
                        props.put("lon", field);
                        break;
                    }
                }
                start = end + 1;
            }
        }
        return Collections.unmodifiableMap(props);
    }
}
