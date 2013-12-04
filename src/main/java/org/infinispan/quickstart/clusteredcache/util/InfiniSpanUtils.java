package org.infinispan.quickstart.clusteredcache.util;

import java.util.List;

import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.infinispan.Cache;
import org.infinispan.query.Search;
import org.infinispan.query.SearchManager;

public final class InfiniSpanUtils {
	
	private InfiniSpanUtils() {}
	
	public static void pause(long nbSeconds) {
		long second = 1000L;
		System.out.print("Pause for " + nbSeconds + ", ");
		for ( long countdown = nbSeconds; countdown > 0 ; countdown--) {
			try {
				Thread.sleep(second);
				System.out.print(countdown + " ");
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}
		System.out.println("... Done.");
	}

	public static <T> void searchValueByIndexedField(Cache<Object,Object> c, String indexedFieldName, String indexedValue, Class<T> clazz) {
		SearchManager searchManager = Search.getSearchManager(c);
		QueryBuilder builder = searchManager.buildQueryBuilderForClass(clazz).get();
		Query query = builder.bool()
				.must(builder.keyword().onField(indexedFieldName)
				.matching(indexedValue).createQuery())
				.createQuery();
		System.out.println("Query:" + query);
		List<Object> results = searchManager.getQuery(query, clazz).list();
		System.out.println("Results from query (" + results.size() + ")");
		for ( Object o :  results ) {
			System.out.println(o);
		}

	}

	
	public static void postImportIndexing(Cache<Object,Object> cacheWithIndexOn) {
		long startTime = System.currentTimeMillis();
		SearchManager searchManager = Search.getSearchManager(cacheWithIndexOn);
		searchManager.getMassIndexer().start();
		System.out.print((System.currentTimeMillis() - startTime) + ".");
	}

}
