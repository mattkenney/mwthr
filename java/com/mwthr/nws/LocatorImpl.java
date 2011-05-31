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

import java.util.HashMap;
import java.util.Map;

import net.sf.javaml.core.kdtree.KDTree;

/**
 * Package-private abstract base class for objects that provide the
 * implementations of the {@link com.mwthr.nws.Locator} enum entries.
 */
abstract class LocatorImpl
{
    /**
     * 2-dimensional tree of property maps.
     */
    final KDTree tree = new KDTree(2);

    /**
     * Map of location codes to property maps.
     */
    final Map<String, Map<String, String>> codeMap = new HashMap<String, Map<String, String>>();

    /**
     * No argument constructor.
     */
    LocatorImpl()
    {
    }

    /**
     * Returns the latitude/longitude from a properties map, or
     * <code>null</code> if the map does not contain a valid latitude/longitude
     * pair.
     */
    double[] getCoordinates(Map<String, String> props)
    {
        double[] result = null;
        String lat = props.get("lat");
        String lon = props.get("lon");
        if (lat != null && lon != null)
        {
            try
            {
                double latitude = Double.valueOf(lat);
                double longitude = Double.valueOf(lon);
                if (-90.0 <= latitude && latitude <= 90.0 && -180.0 <= longitude && longitude <= 180.0)
                {
                    result = new double[] { latitude, longitude };
                }
            }
            catch (NumberFormatException e)
            {
                // just return null
            }
        }
        return result;
    }

    /**
     * Returns filename of the data file for the location type.
     */
    String getFilename()
    {
        String result = getClass().getName();
        int offset = result.lastIndexOf('.');
        if (offset >= 0)
        {
            result = result.substring(offset + 1);
        }
        result += ".txt";
        return result;
    }

    /**
     * Returns the internal map of location codes to property maps.
     */
    Map<String, Map<String, String>> getMap()
    {
        return codeMap;
    }

    /**
     * Returns the internal 2-dimensional tree of property maps.
     */
    KDTree getKDTree()
    {
        return tree;
    }
}
