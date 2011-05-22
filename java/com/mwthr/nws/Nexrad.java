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

import java.util.Map;

import net.sf.javaml.core.kdtree.KDTree;

public class Nexrad extends Location
{
    private final String ncdcid;
    private final String icao;
    private final String wban;
    private final String name;
    private final String country;
    private final String st;
    private final String county;
    private final String lat;
    private final String lon;
    private final String elev;
    private final String time;
    private final String stntype;

    public Nexrad(String line)
    {
        String ncdcid = null;
        String icao = null;
        String wban = null;
        String name = null;
        String country = null;
        String st = null;
        String county = null;
        String lat = null;
        String lon = null;
        String elev = null;
        String time = null;
        String stntype = null;
        if (line != null)
        {
            final int length = line.length();
            for (int i = 0, start = 0; i < 12; i++)
            {
                switch (i)
                {
                case 0:
                    ncdcid  = substring(line, length,   1,   8);
                    break;
                case 1:
                    icao    = substring(line, length,  10,  13);
                    break;
                case 2:
                    wban    = substring(line, length,  15,  19);
                    break;
                case 3:
                    name    = substring(line, length,  21,  50);
                    break;
                case 4:
                    country = substring(line, length,  52,  71);
                    break;
                case 5:
                    st      = substring(line, length,  73,  74);
                    break;
                case 6:
                    county  = substring(line, length,  76, 106);
                    break;
                case 7:
                    lat     = substring(line, length, 107, 115);
                    break;
                case 8:
                    lon     = substring(line, length, 117, 126);
                    break;
                case 9:
                    elev    = substring(line, length, 128, 133);
                    break;
                case 10:
                    time    = substring(line, length, 135, 140);
                    break;
                case 11:
                    stntype = substring(line, length, 142, 191);
                    break;
                }
            }
        }
        this.ncdcid =   ncdcid;
        this.icao =     icao;
        this.wban =     wban;
        this.name =     name;
        this.country =  country;
        this.st =       st;
        this.county =   county;
        this.lat =      lat;
        this.lon =      lon;
        this.elev =     elev;
        this.time =     time;
        this.stntype =  stntype;
    }
    
    public String getNcdcid()
    {
        return ncdcid;
    }

    public String getIcao()
    {
        return icao;
    }

    public String getWban()
    {
        return wban;
    }

    public String getName()
    {
        return name;
    }

    public String getCountry()
    {
        return country;
    }

    public String getSt()
    {
        return st;
    }

    public String getCounty()
    {
        return county;
    }

    public String getLat()
    {
        return lat;
    }

    public String getLon()
    {
        return lon;
    }

    public String getElev()
    {
        return elev;
    }

    public String getTime()
    {
        return time;
    }

    public String getStntype()
    {
        return stntype;
    }

    public String getBaseURL()
    {
        return "http://radar.weather.gov/ridge/lite/N0R/" + icao.substring(1) + "_";
    }

    public double[] getCoordinates()
    {
        return Location.getCoordinates(lat, lon);
    }

    public static KDTree getKDTree()
    {
        return Location.getKDTree(Nexrad.class);
    }

    public String getKey()
    {
        return icao;
    }

    public static Map getMap()
    {
        return Location.getMap(Nexrad.class);
    }

    private String substring(String line, int length, int start, int end)
    {
        String result = null;
        if (start < length)
        {
            result = line.substring(start - 1, end < length ? end : length);
        }
        return result;
    }

    public boolean isValid()
    {
        return
            icao != null && icao.length() > 0 &&
            name != null && name.length() > 0;
    }

    public String toString()
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append(ncdcid);
        buffer.append(' ');
        buffer.append(icao);
        buffer.append(' ');
        buffer.append(wban);
        buffer.append(' ');
        buffer.append(name);
        buffer.append(' ');
        buffer.append(country);
        buffer.append(' ');
        buffer.append(st);
        buffer.append(' ');
        buffer.append(county);
        buffer.append(' ');
        buffer.append(lat);
        buffer.append(' ');
        buffer.append(lon);
        buffer.append(' ');
        buffer.append(elev);
        buffer.append(' ');
        buffer.append(time);
        buffer.append(' ');
        buffer.append(stntype);
        return buffer.toString();
    }
}
