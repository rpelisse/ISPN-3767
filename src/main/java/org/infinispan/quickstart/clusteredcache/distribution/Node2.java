/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other
 * contributors as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.infinispan.quickstart.clusteredcache.distribution;

import static org.infinispan.quickstart.clusteredcache.util.InfiniSpanUtils.pause;
import static org.infinispan.quickstart.clusteredcache.util.InfiniSpanUtils.postImportIndexing;
import static org.infinispan.quickstart.clusteredcache.util.InfiniSpanUtils.searchValueByIndexedField;

import java.util.List;

import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.infinispan.AdvancedCache;
import org.infinispan.Cache;
import org.infinispan.context.Flag;
import org.infinispan.query.Search;
import org.infinispan.query.SearchManager;
import org.infinispan.quickstart.clusteredcache.util.LoggingListener;


public class Node2 extends AbstractNode {

	public static final String INDEXED_VALUE_PREFIX = "indexMe";
	public static final String INDEXED_FIELD_NAME = "indexedField";
	public static final String KEY_PREFIX = "key";
	public static final String CACHE_NAME = "indexedCache";

	public static void main(String[] args) throws Exception {
		new Node2().run();
	}

	public void run() {

		waitForClusterToForm();

		AdvancedCache<Object, Object> cacheNoIndex = getCacheManager().getCache(CACHE_NAME).getAdvancedCache()
				.withFlags(Flag.SKIP_INDEXING);
		// Add a listener so that we can see the puts to this node
		cacheNoIndex.addListener(new LoggingListener());

		// Put a few entries into the cache so that we can see them distribution to the other nodes
		System.out.print("Loading data in  ");
		loadData(cacheNoIndex);
		System.out.println("Done.");
		// Index'em afterward
		System.out.print("Post import indexing... ");
		postImportIndexing(getCacheManager().getCache(CACHE_NAME));
		System.out.print("Wait for MassIndexer to flush(): ");
		pause(60);
		searchValueByIndexedField(getCacheManager().getCache(CACHE_NAME),INDEXED_FIELD_NAME,INDEXED_VALUE_PREFIX + "0", Value.class);
		System.out.println("Test case finished.");
	}

	private static void loadData(Cache<Object,Object> c) {
		final long NB_ITEMS = 10L;

		long startTime = System.currentTimeMillis();
		for ( long i = 0 ; i < NB_ITEMS ; i++ )
			c.put(KEY_PREFIX + i, new Value(INDEXED_VALUE_PREFIX + i));
		System.out.println((System.currentTimeMillis() - startTime) + "ms");		
	}

	@Override
	protected int getNodeId() {
		return 2;
	}

}
