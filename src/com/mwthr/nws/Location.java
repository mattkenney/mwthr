package com.mwthr.nws;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.javaml.core.kdtree.KDTree;

public abstract class Location
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

    public Location()
    {
    }

    public abstract double[] getCoordinates();

    static double[] getCoordinates(String lat, String lon)
    {
        double[] result = null;
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

    static KDTree getKDTree(Class clazz)
    {
        KDTree result = null;
        String name = clazz.getName();
        int offset = name.lastIndexOf('.');
        if (offset >= 0)
        {
            name = name.substring(offset + 1);
        }
        name += ".txt";
        try
        {
            InputStream in = clazz.getResourceAsStream(name);
            result = getKDTree(clazz, in);
            in.close();
        }
        catch (Exception e)
        {
            Logger.getLogger(clazz.getName()).log(Level.WARNING, "Problem loading \"" + name + "\"", e);
        }
        return result;
    }

    static KDTree getKDTree(Class clazz, InputStream in)
        throws Exception
    {
        KDTree result = new KDTree(2);
        Constructor<Location> init = clazz.getConstructor(String.class);
        BufferedReader lines = new BufferedReader(new InputStreamReader(in, LOAD_CHARSET));
        for (String line = lines.readLine(); line != null; line = lines.readLine())
        {
            Location loc = init.newInstance(line);
            double[] coordinates = loc.getCoordinates();
            if (coordinates != null)
            {
                result.insert(coordinates, loc);
            }
        }
        return result;
    }

    static Map getMap(Class clazz)
    {
        Map result = null;
        String name = clazz.getName();
        int offset = name.lastIndexOf('.');
        if (offset >= 0)
        {
            name = name.substring(offset + 1);
        }
        name += ".txt";
        try
        {
            InputStream in = clazz.getResourceAsStream(name);
            result = getMap(clazz, in);
            in.close();
        }
        catch (Exception e)
        {
            Logger.getLogger(clazz.getName()).log(Level.WARNING, "Problem loading \"" + name + "\"", e);
        }
        return result;
    }

    static Map getMap(Class clazz, InputStream in)
        throws Exception
    {
        Map result = new HashMap();
        Constructor<Location> init = clazz.getConstructor(String.class);
        BufferedReader lines = new BufferedReader(new InputStreamReader(in, LOAD_CHARSET));
        for (String line = lines.readLine(); line != null; line = lines.readLine())
        {
            Location loc = init.newInstance(line);
            String name = loc.getName();
            if (name != null)
            {
                result.put(name, loc);
            }
        }
        return result;
    }

    public abstract String getName();
}
