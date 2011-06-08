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
 * Package-private implementation of {@link com.mwthr.nws.Locator#NEXRAD}.
 * <p>The data file is from here:</p>
 * https://mi3.ncdc.noaa.gov//mi3report/MISC/nexrad-stations.TXT
 */
class Nexrad extends LocatorImpl
{
    /**
     * Charset of the data file.
     */
    private static final Charset LOAD_CHARSET = Charset.forName("UTF-8");

    /**
     * No argument constructor. Loads the data file and populates the map and
     * the 2-dimentional tree.
     */
    Nexrad()
    {
        try
        {
            InputStream in = getClass().getResourceAsStream(getFilename());
            
            BufferedReader lines = new BufferedReader(new InputStreamReader(in, LOAD_CHARSET));
            for (String line = lines.readLine(); line != null; line = lines.readLine())
            {
                Map<String, String> props = parseLine(line);
                double[] where = getCoordinates(props);
                if (where != null && props.containsKey("icao") && props.containsKey("name"))
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
            result = line.substring(start - 1, stop);
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
                    parseField(props, "ncdcid",     line, length,   1,   8);
                    break;
                case 1:
                    String icao = parseField(props, "icao", line, length,  10,  13);
                    if (icao != null && icao.length() > 0)
                    {
                        props.put("baseurl", "http://radar.weather.gov/ridge/lite/N0R/" + icao.substring(1) + "_");
                        props.put("statusurl", "http://weather.noaa.gov/monitor/radar2/site/" + icao.toLowerCase() + ".html");
                    }
                    break;
                case 2:
                    parseField(props, "wban",       line, length,  15,  19);
                    break;
                case 3:
                    parseField(props, "name",       line, length,  21,  50);
                    break;
                case 4:
                    parseField(props, "country",    line, length,  52,  71);
                    break;
                case 5:
                    parseField(props, "st",         line, length,  73,  74);
                    break;
                case 6:
                    parseField(props, "county",     line, length,  76, 106);
                    break;
                case 7:
                    parseField(props, "lat",        line, length, 107, 115);
                    break;
                case 8:
                    parseField(props, "lon",        line, length, 117, 126);
                    break;
                case 9:
                    parseField(props, "elev",       line, length, 128, 133);
                    break;
                case 10:
                    parseField(props, "time",       line, length, 135, 140);
                    break;
                case 11:
                    parseField(props, "stntype",    line, length, 142, 191);
                    break;
                }
            }
        }
        return Collections.unmodifiableMap(props);
    }
}
