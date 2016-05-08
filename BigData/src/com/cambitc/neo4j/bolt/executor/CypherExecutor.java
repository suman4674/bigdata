package com.cambitc.neo4j.bolt.executor;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Michael Hunger @since 22.10.13
 */
public interface CypherExecutor {
    Iterator<Map<String,Object>> query(String statement, Map<String,Object> params);
}
