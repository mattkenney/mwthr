package com.mwthr.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

public class GeoIP
{
    private GeoIP()
    {
    }

    public static double[] getCoordinates(ServletContext context, ServletRequest request)
    {
        return getCoordinates(context, request.getRemoteAddr());
    }

    public static double[] getCoordinates(ServletContext context, String ipString)
    {
        double[] result = null;
        if (ipString != null)
        {
            LookupService service = (LookupService) context.getAttribute(GeoIP.class.getName());
            if (service == null)
            {
                try
                {
                    service = new LookupService(getSplitData(context, "/WEB-INF/GeoLiteCity/"));
                    context.setAttribute(GeoIP.class.getName(), service);
                }
                catch (Exception e)
                {
                    Logger.getLogger(GeoIP.class.getName()).log(Level.WARNING, "Problem loading GeoLiteCity data", e);
                }
            }
            Location loc = (service == null) ? null : service.getLocation(ipString);
            if (loc != null)
            {
                result = new double[] { loc.latitude, loc.longitude };
            }
        }
        return result;
    }

    private static byte[] getSplitData(ServletContext context, String path)
        throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        StringBuilder name = new StringBuilder();
        byte[] block = new byte[4096];
        for (char a = 'a'; a <= 'z'; a++)
        {
            for (char b = 'a'; b <= 'z'; b++)
            {
                name.setLength(0);
                name.append(path);
                if (!path.endsWith("/"))
                {
                    name.append('/');
                }
                name.append('x');
                name.append(a);
                name.append(b);
                InputStream in = context.getResourceAsStream(name.toString());
                if (in == null)
                {
                    return buffer.toByteArray();
                }
                while (true)
                {
                    int length = in.read(block);
                    if (length < 0)
                    {
                        break;
                    }
                    buffer.write(block, 0, length);
                }
                in.close();
            }
        }
        // oops, got to xzz?
        return buffer.toByteArray();
    }
}
