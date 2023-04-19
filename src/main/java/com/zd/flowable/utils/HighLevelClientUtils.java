package com.zd.flowable.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ES工具类
 *
 * @author zhangda
 * @date: 2023/4/17
 **/
public class HighLevelClientUtils {

    private static final Logger log = LoggerFactory.getLogger(HighLevelClientUtils.class);

    public static RestHighLevelClient createRestHighLevelClient(String esUrl, Long keepAlive) {
        RestClientBuilder clientBuilder = RestClient.builder(createHttpHost(URI.create(esUrl)))
                .setHttpClientConfigCallback(requestConfig -> requestConfig.setKeepAliveStrategy(
                        (response, context) -> keepAlive));
        return new RestHighLevelClient(clientBuilder);
    }

    private static HttpHost createHttpHost(URI uri) {
        if (StringUtils.isEmpty(uri.getUserInfo())) {
            return HttpHost.create(uri.toString());
        }
        try {
            return HttpHost.create(new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(),
                    uri.getQuery(), uri.getFragment()).toString());
        } catch (URISyntaxException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * 创建索引
     *
     * @param restHighLevelClient 客户端
     * @param indexName           索引名
     * @return
     */
    public static boolean createIndex(RestHighLevelClient restHighLevelClient, String indexName) {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        try {
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            return createIndexResponse.isAcknowledged();
        } catch (Exception e) {
            log.error("[ElasticSearch]createIndex，indexName={}", JSON.toJSONString(indexName), e);
        }
        return false;
    }

    /**
     * 创建索引
     *
     * @param restHighLevelClient 客户端
     * @param indexName           索引名
     * @param setting
     * @param mapping
     * @return
     */
    public static boolean createIndex(RestHighLevelClient restHighLevelClient, String indexName, XContentBuilder setting, XContentBuilder mapping) {
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            createIndexRequest.settings(setting);
            createIndexRequest.mapping(mapping);
            return restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT).isFragment();
        } catch (Exception e) {
            log.error("[ElasticSearch]createIndex，indexName={}", indexName, e);
        }
        return false;
    }

    /**
     * 创建索引
     *
     * @param restHighLevelClient 客户端
     * @param indexName           索引名
     * @param setting
     * @return
     */
    public static boolean createIndex(RestHighLevelClient restHighLevelClient, String indexName, String setting) {
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            createIndexRequest.settings(setting, XContentType.JSON);
            restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            return true;
        } catch (Exception e) {
            log.error("[ElasticSearch]createIndex，indexName={}", indexName, e);
        }
        return false;
    }

    /**
     * 创建索引
     *
     * @param restHighLevelClient 客户端
     * @param indexName           索引名
     * @param setting
     * @param mapping
     * @return
     */
    public static boolean createIndex(RestHighLevelClient restHighLevelClient, String indexName, String setting, String mapping) {
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            createIndexRequest.settings(setting, XContentType.JSON);
            createIndexRequest.mapping(mapping, XContentType.JSON);
            return restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT).isFragment();
        } catch (Exception e) {
            log.error("[ElasticSearch]createIndex，indexName={}", indexName, e);
        }
        return false;
    }

    /**
     * 创建索引
     *
     * @param restHighLevelClient 客户端
     * @param indexName           索引名
     * @param setting
     * @param mapping
     * @return
     */
    public static boolean createIndex(RestHighLevelClient restHighLevelClient, String indexName, String setting, XContentBuilder mapping) {
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            createIndexRequest.settings(setting, XContentType.JSON);
            createIndexRequest.mapping(mapping);
            return restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT).isFragment();
        } catch (Exception e) {
            log.error("[ElasticSearch]createIndex，indexName={}", indexName, e);
        }
        return false;
    }

    /**
     * 删除索引
     *
     * @param restHighLevelClient 客户端
     * @param indexName           索引名
     * @return
     */
    public static boolean deleteIndex(RestHighLevelClient restHighLevelClient, String indexName) {
        try {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
            AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            return delete.isAcknowledged();
        } catch (Exception e) {
            log.error("[ElasticSearch]deleteIndex，indexName={}", indexName, e);
        }
        return false;
    }

    /**
     * 创建映射
     *
     * @param restHighLevelClient 客户端
     * @param indexName           索引名
     * @param mapping
     * @return
     */
    public static boolean createIndexMapping(RestHighLevelClient restHighLevelClient, String indexName, XContentBuilder mapping) {
        PutMappingRequest putMappingRequest = new PutMappingRequest(indexName);
        putMappingRequest.source(mapping);
        try {
            AcknowledgedResponse putMappingResponse = restHighLevelClient.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
            System.out.println(putMappingResponse);
            return putMappingResponse.isAcknowledged();
        } catch (IOException e) {
            log.error("[ElasticSearch]createIndexMapping，indexName={}", JSON.toJSONString(indexName), e);
        }
        return false;
    }

    /**
     * 查询 setting
     *
     * @param restHighLevelClient 客户端
     * @param indexName           索引名
     * @return
     */
    public static String getIndexSettings(RestHighLevelClient restHighLevelClient, String indexName) {
        try {
            GetSettingsRequest request = new GetSettingsRequest().indices(indexName);
            Settings indexSettings = restHighLevelClient.indices().getSettings(request, RequestOptions.DEFAULT).getIndexToSettings().get(indexName);
            return JSONObject.toJSONString(indexSettings);
        } catch (Exception e) {
            log.error("[ElasticSearch]createIndexMapping，indexName={}", JSON.toJSONString(indexName), e);
        }
        return null;
    }

    /**
     * 更新 setting
     *
     * @param restHighLevelClient 客户端
     * @param indexName           索引名
     * @param setting
     * @return
     */
    public static boolean updateIndexSettings(RestHighLevelClient restHighLevelClient, String indexName, String setting) {
        try {
            UpdateSettingsRequest request = new UpdateSettingsRequest(indexName);
            request.settings(setting, XContentType.JSON);
            return restHighLevelClient.indices().putSettings(request, RequestOptions.DEFAULT).isAcknowledged();
        } catch (Exception e) {
            log.error("[ElasticSearch]deleteDoc", e);
        }
        return false;
    }

    /**
     * 索引是否存在
     *
     * @param restHighLevelClient 客户端
     * @param indexName           索引名
     * @return
     */
    public static boolean indexExists(RestHighLevelClient restHighLevelClient, String indexName) {
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
            return restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("[ElasticSearch]indexExists，indexName={}", indexName, e);
        }
        return false;
    }

    /**
     * 更新文档
     *
     * @param restHighLevelClient 客户端
     * @param indexName           索引名
     * @param targetVo            date
     * @return
     */
    public static boolean updateDoc(RestHighLevelClient restHighLevelClient, String indexName, Object targetVo, String id) {
        try {
            if (targetVo != null && !StringUtils.isEmpty(id)) {
                GetRequest getRequest = new GetRequest(indexName, id);
                boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);

                if (exists) {
                    // 存在更新
                    UpdateRequest updateRequest = new UpdateRequest(indexName, id).doc(JSON.toJSONString(targetVo), XContentType.JSON);
                    UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
                    log.info("[ElasticSearch]updateDocs indexName:{},response:{}", indexName, update);
                } else {
                    // 不存在则添加
                    IndexRequest indexRequest = new IndexRequest(indexName).id(id).source(JSON.toJSONString(targetVo), XContentType.JSON);
                    IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
                    log.info("[ElasticSearch]updateDocs indexName:{},response:{}", indexName, index);
                }
                return true;
            }
        } catch (Exception e) {
            log.error("[ElasticSearch]updateDoc，indexName={}", indexName, e);
        }
        return false;
    }

    /**
     * 批量更新文档
     *
     * @param restHighLevelClient 客户端
     * @param indexName           索引
     * @param voListStr           数据list
     * @param idKeyStr            id Str
     * @return
     */
    public static boolean updateDocs(RestHighLevelClient restHighLevelClient, String indexName, String voListStr, String idKeyStr) {
        try {
            BulkRequest bulkRequest = new BulkRequest();
            if (StringUtils.isEmpty(voListStr)) {
                return false;
            }
            List<Object> voList = JSONArray.parseArray(voListStr, Object.class);
            voList.forEach(vo -> {
                Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSONString(vo), Map.class);
                if (map.containsKey(idKeyStr)) {
                    IndexRequest indexRequest = new IndexRequest(indexName).source(map, XContentType.JSON).id(map.get(idKeyStr) + "");
                    bulkRequest.add(indexRequest);
                }
            });
            BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info("[ElasticSearch]updateDocs indexName:{},response:{}", indexName, response);
            return !response.hasFailures();
        } catch (Exception e) {
            log.error("[ElasticSearch]updateDocs，indexName={}", indexName, e);
        }
        return false;
    }

    /**
     * 删除文档
     *
     * @param restHighLevelClient 客户端
     * @param indexName           索引名
     * @param id                  id
     * @return
     */
    public static boolean deleteDoc(RestHighLevelClient restHighLevelClient, String indexName, String id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(indexName);
            deleteRequest.id(id);
            DeleteResponse response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            log.info("[ElasticSearch]deleteDoc indexName:{},response:{}", indexName, response);
            return true;
        } catch (Exception e) {
            log.error("[ElasticSearch]deleteDoc，indexName={}", indexName, e);
        }
        return false;
    }

    /**
     * 批量删除条件查出来的数据
     *
     * @param restHighLevelClient
     * @param indexName
     * @param request
     * @return
     */
    public static boolean deleteDocByQuery(RestHighLevelClient restHighLevelClient, String indexName, DeleteByQueryRequest request) {
        try {
            BulkByScrollResponse response = restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
            log.info("[ElasticSearch]deleteDocByQuery indexName:{},response:{}", indexName, response);
            return true;
        } catch (Exception e) {
            log.error("[ElasticSearch]deleteDocByQuery，indexName={}", indexName, e);
        }
        return false;
    }


    /**
     * 清理缓存
     *
     * @param restHighLevelClient 客户端
     */
    public static void clearCache(RestHighLevelClient restHighLevelClient) {
        try {
            ClearIndicesCacheRequest requestAll = new ClearIndicesCacheRequest();
            requestAll.indicesOptions(IndicesOptions.lenientExpandOpen());
            restHighLevelClient.indices().clearCache(requestAll, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("[ElasticSearch]deleteDoc", e);
        }
    }

    /**
     * Map忽略null
     *
     * @param vo
     * @return
     */
    public static Map<String, Object> getIgnoreNullMap(Object vo) {
        if (StringUtils.isEmpty(vo)) {
            return null;
        }
        Map<String, Object> targetMap = new HashMap(16);
        try {
            Map<String, Object> map = JSONObject.parseObject(JSONObject.toJSONString(vo), Map.class);
            if (map == null) {
                return null;
            }
            map.forEach((key, value) -> {
                if (!StringUtils.isEmpty(value)) {
                    targetMap.put(key, value);
                }
            });
        } catch (Exception e) {
            log.error("vo 转 Map失败，e" + e);
            return null;
        }
        return targetMap;
    }

    /**
     * 搜索
     *
     * @param restHighLevelClient 客户端
     * @param indexName           索引名
     * @param searchSourceBuilder 条件
     * @return
     */
    public static List<Map<String,Object>> search(RestHighLevelClient restHighLevelClient, String indexName, SearchSourceBuilder searchSourceBuilder) {
        try {
            List<Map<String,Object>> voList = new ArrayList<>();
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(indexName);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().getTotalHits().value > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    voList.add(hit.getSourceAsMap());
                }
            }

            log.info("返回对象为：{}", voList);
            return voList;
        } catch (Exception e) {
            log.error("[ElasticSearch]search", e);
        }
        return null;
    }

}
