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

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.quickstart.clusteredcache.util.ClusterValidation;

import java.io.IOException;

@SuppressWarnings("unused")
public abstract class AbstractNode {

	private static EmbeddedCacheManager createCacheManagerProgramatically() {
		return new DefaultCacheManager(buildGlobalConfiguration(),
							buildCacheConfiguration());
	}

	private static GlobalConfiguration buildGlobalConfiguration() {
		return GlobalConfigurationBuilder.defaultClusteredBuilder()
			.transport().addProperty("configurationFile", "jgroups.xml")
			.globalJmxStatistics().enable().allowDuplicateDomains(true)
			.build();
	}
	
	private static Configuration buildCacheConfiguration() {
		return new ConfigurationBuilder()
				.clustering()
				.cacheMode(CacheMode.DIST_SYNC)
				.indexing().enable()
				.indexing().addProperty("hibernate.search.default.exclusive_index_use", "false")
				.indexing().addProperty("hibernate.search.lucene_version", "LUCENE_36")
				.build();
	}
	
	public static final int CLUSTER_SIZE = 4;

	private final EmbeddedCacheManager cacheManager;

	public AbstractNode() {
		this.cacheManager = createCacheManagerProgramatically();
	}

	protected EmbeddedCacheManager getCacheManager() {
		return cacheManager;
	}

	protected void waitForClusterToForm() {
		// Wait for the cluster to form, erroring if it doesn't form after the
		// timeout
		if (!ClusterValidation.waitForClusterToForm(getCacheManager(), getNodeId(), CLUSTER_SIZE)) {
			throw new IllegalStateException("Error forming cluster, check the log");
		}
	}

	protected abstract int getNodeId();

}
