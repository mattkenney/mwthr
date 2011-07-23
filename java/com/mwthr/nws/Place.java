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

/**
 * Package-private implementation of {@link com.mwthr.nws.Locator#PLACE}.
 * <p>The data file is from here:</p>
 * http://www.census.gov/tiger/tms/gazetteer/places2k.txt
 * <p>File layout is here:</p>
 * http://www.census.gov/geo/www/gazetteer/places2k_layout.html#places
 */
class Place extends LocatorImpl
{
    /**
     * Charset of the data file.
     */
    private static final Charset LOAD_CHARSET = Charset.forName("ISO-8859-1");

    /**
     * No argument constructor. Loads the data file and populates the map and
     * the 2-dimentional tree.
     */
    Place()
    {
        try
        {
            InputStream in = getClass().getResourceAsStream(getFilename());
            
            BufferedReader lines = new BufferedReader(new InputStreamReader(in, LOAD_CHARSET));
            for (String line = lines.readLine(); line != null; line = lines.readLine())
            {
                Map<String, String> props = parseLine(line);
                double[] where = getCoordinates(props);
                if (where != null && props.containsKey("fips") && props.containsKey("name"))
                {
                    tree.insert(where, props);
                    codeMap.put(props.get("fips"), props);
                }
            }
            
            in.close();
        }
        catch (Exception e)
        {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem loading \"" + getFilename() + "\"", e);
        }
    }

    /**
     * Extracts, trims, and stores one property for one location/line in the
     * data file, if it is non-empty.
     */
    String parseField(Map<String, String> props, String key, String line, int length, int start, int end)
    {
        String result = null;
        int stop = (end < length) ? end : length;
        if (line != null && start < length && stop >= start)
        {
            result = line.substring(start - 1, stop).trim();
            props.put(key, result);
        }
        return result;
    }

    /**
     * Parses the location properties for one location/line in the data file.
     * The fields are fixed width.
     */
    Map<String, String> parseLine(String line)
    {
        Map<String, String> props = new LinkedHashMap<String, String>(12);
        if (line != null)
        {
            final int length = line.length();
            for (int i = 0, start = 0; i < 12; i++)
            {
                switch (i)
                {
                case 0:
                    parseField(props, "state",     line, length,   1,   2);
                    break;
                case 1:
                    parseField(props, "statefips", line, length,   3,   4);
                    break;
                case 2:
                    parseField(props, "fips",      line, length,   5,   9);
                    break;
                case 3:
                    String name = parseField(props, "name", line, length, 10, 73);
                    int offset = (name == null) ? -1 : name.lastIndexOf(' ');
                    if (offset > 0)
                    {
                        props.put("nameshort", name.substring(0, offset));
                    }
                    else
                    {
                        props.put("nameshort", name);
                    }
                    break;
                case 4:
                    //skip Columns 74-82: Total Population (2000)
                    break; 
                case 5:
                    //skip Columns 83-91: Total Housing Units (2000)
                    break;
                case 6:
                    //skip Columns 92-105: Land Area (square meters)
                    break;
                case 7:
                    //skip Columns 106-119: Water Area(square meters)
                    break;
                case 8:
                    //skip Columns 120-131: Land Area (square miles)
                    break;
                case 9:
                    //skip Columns 132-143: Water Area (square miles)
                    break;
                case 10:
                    parseField(props, "lat",       line, length, 144, 153);
                    break;
                case 11:
                    parseField(props, "lon",       line, length, 154, 164);
                    break;
                }
            }
        }
        return Collections.unmodifiableMap(props);
    }
}
