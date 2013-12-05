package org.infinispan.quickstart.clusteredcache.distribution;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Data
@Indexed
class Value implements Serializable {
	
	private static final long serialVersionUID = 625017012604010075L;
	@Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
	private String indexedField;
	private Map<String, String> data = new HashMap<String, String>();

	public Value(String indexedField) {
		this.indexedField = indexedField;
	}

	public Map<String, String> getData() {
		return data;
	}
}