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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

/**
 * This <code>ServletContextListener</code> creates a
 * <a href="http://jcp.org/en/jsr/detail?id=107">JSR 107</a> cache and stores a
 * reference to it as a servlet context attribute with name "cache". The cache
 * implements {@link java.util.Map java.util.Map} interface.
 * <p>
 * To use, include the following in the
 * deployment descriptor (web.xml):
 * <pre>
 *     &lt;listener&gt;
 *         &lt;listener-class&gt;com.mwthr.gae.CacheContextListener&lt;/listener-class&gt;
 *     &lt;/listener&gt;
 * </pre>
 * The cache will expire entries after 1 hour.
 */
public class CacheContextListener implements ServletContextListener
{
    /**
     * The attribute name used to store a reference to the Memcache, that
     * is, "cache".
     */
    public static final String ATTRIBUTE_NAME = "cache";

    /**
     * The time after which cache entries will expire, that is, 3600 seconds = 1 hour.
     */
    public static final int EXPIRATION_DELTA = 3600;

    /**
     * No argument constructor.
     */
    public CacheContextListener()
    {
    }

    /**
    * See {@link javax.servlet.ServletContextListener#contextDestroyed}.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
    }

    /**
     * Creates a Memcache and stores a reference to it as a servlet context
     * attribute with name "cache".
     * @param  sce the <code>ServletContextEvent</code> that links to the <code>ServletContext</code>
     */
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        Map<Object, Object> props = new HashMap<Object, Object>();
        props.put(GCacheFactory.EXPIRATION_DELTA, EXPIRATION_DELTA);
        Map cache = null;
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
        ServletContext context = sce.getServletContext();
        context.setAttribute(ATTRIBUTE_NAME, cache);
    }
}
