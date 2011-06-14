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
package com.mwthr.gae;

import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

public class URLDataFetcher
{
    public class Result
    {
        private final Map<URL, Future<HTTPResponse>> futures = new LinkedHashMap<URL, Future<HTTPResponse>>();
        private final URLDataHandler handler;
        private Map<String, String> value = null;

        Result(URLDataHandler handler)
        {
            this.handler = handler;

            boolean isFirst = true;
            for (URL dataURL : handler.getURLs())
            {
                Map<String, String> props = cache.get(dataURL);
Logger.getLogger(getClass().getName()).log(Level.INFO, "cache: " + dataURL + " -> " + (props != null));

                if (props == null || !handler.isValid(props))
                {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "Fetching \"" + dataURL + "\"");
                    HTTPRequest request = new HTTPRequest(dataURL, HTTPMethod.GET, options);
                    futures.put(dataURL, service.fetchAsync(request));
                }
                else if (isFirst)
                {
                    value = props;
                    break;
                }
                isFirst = false;
            }
        }

        public Map<String, String> getData()
        {
            while (value == null && !futures.isEmpty())
            {
                boolean isFirst = true;
                for (Iterator<Map.Entry<URL, Future<HTTPResponse>>> i = futures.entrySet().iterator(); i.hasNext(); )
                {
                    Map.Entry<URL, Future<HTTPResponse>> entry = i.next();
                    byte[] data = null;
                    if (isFirst || entry.getValue().isDone())
                    {
                        isFirst = false;
                        try
                        {
                            HTTPResponse response = entry.getValue().get(DEADLINE_DELTA, TimeUnit.MILLISECONDS);
                            if (response.getResponseCode() == 200)
                            {
                                data = response.getContent();
                            }
                            else
                            {
                                Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem fetching \"" + entry.getKey() + "\": " + response.getResponseCode());
                            }
                        }
                        catch (TimeoutException e)
                        {
                            continue;
                        }
                        catch (Exception e)
                        {
                            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem fetching \"" + entry.getKey() + "\"", e);
                        }
                        i.remove();
                        Map<String, String> props = (data == null) ? null : handler.parse(entry.getKey(), data);
                        if (props != null && handler.isValid(props))
                        {
Logger.getLogger(getClass().getName()).log(Level.INFO, "caching: " + entry.getKey());
                            cache.put(entry.getKey(), props);
                            value = props;
                            break;
                        }
                        else
                        {
                            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Problem fetching \"" + entry.getKey() + "\" (isnull == " + (props == null) + ")");
                        }
                    }
                }
            }
            for (Future<HTTPResponse> f : futures.values())
            {
                f.cancel(true);
            }
            return value;
        }
    }

    /**
     * The time after which cache entries will expire, that is, 5400 seconds = 90 minutes.
     */
    public static final int EXPIRATION_DELTA = 5400;

    private static final double DEADLINE = 10.0;

    private static final long DEADLINE_DELTA = 100;

    private final static FetchOptions options = FetchOptions.Builder
                                            .disallowTruncate()
                                            .followRedirects()
                                            .setDeadline(DEADLINE)
                                            .validateCertificate()
                                            ;

    private Map<URL, Map<String, String>> cache = null;
    private final URLFetchService service = URLFetchServiceFactory.getURLFetchService();

    @SuppressWarnings("unchecked")
    public URLDataFetcher()
    {
        Map props = new HashMap();
        props.put(GCacheFactory.EXPIRATION_DELTA, EXPIRATION_DELTA);
        try
        {
            CacheManager manager = CacheManager.getInstance();
            CacheFactory cacheFactory = manager.getCacheFactory();
            cache = cacheFactory.createCache(props);
        }
        catch (Exception e)
        {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Unable to create cache", e);
        }
    }

    public Result getResult(URLDataHandler handler)
    {
        return new Result(handler);
    }
}
