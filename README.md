# esquery-proxy
Elasticsearch 를 서비스 용도로 많은 분들이 사용하고 계시는 것으로 압니다.
저 처럼 Java API를 사용하고 계신 분들도 계실 테고 RESTful API 를 사용하고 계신 분들도 계실 것으로 압니다.
그냥 버전 업그레이도 해야 하고 해서 재미 삼아 가볍게 만들어 보았습니다.
관련해서
* 단순 기능 동작 유무만 확인했습니다. (잘 됩니다.)
* 성능 테스트 하지 않았습니다.
* 안정성 테스트 하지 않았습니다.
그래서 가져다 막 고쳐서 사용하시면 좋겠습니다.

일단 만들게 된 동기는
* Search 와 Aggregation 에 대해서 사용할 목적으로 만들었습니다.
* Elasticsearch JAVA API 버전 관리에 대한 유지보수 비용을 절감 해야 했습니다.
* Elasticsearch Cluster 에 대한 Version Upgrade 도 수행 해야 했습니다.
* Multi Cluster 에 대한 Concurrent 처리가 가능 해야 했습니다.

프로젝트 코드를 보시면 아시겠지만 매우 간단 합니다.

## 사용한 Framework)
* SpringMVC + Maven Project
* pom.xml  내 dependency 참고 하시면 됩니다.

## 지원 가능한 API)
* Elasticsearch에서 제공하는 거의 모든 RESTful API 를 제공 합니다.
 ** HTTP POST 만 구현해 놨기 때문에 POST 를 지원하지 않는 API 는 동작 하지 않습니다.
 ** 조만간 시간 나는데로 추가해 보겠습니다.
* Single Request 뿐만 아니라 Multi Request 도 지원 합니다.
* Single Cluster 뿐만 아니라 Multi Cluster 로 Request 를 보낼 수 있습니다.
 *  서로 다른 Version 의 Cluster 라도 상관 없습니다.

## Single Request Example)
<pre><code>
[WAS]
http://localhost:8080/query

[Method]
POST RAW

[Request Body]
{
  "target":"http://{YOUR-CLUSTER}/{YOUR-INDEX}/_search",
  "query":{}
}
</code></pre>
* target
 *  요청할 Elasticsearch Cluster 의 RESTful Endpoint 를 작성 하시면 됩니다.
 * {YOUR-INDEX} 는 alias, single index, multi index  모두 사용 가능 합니다.
* query
 * 기존에 사용하시던 QueryDSL 문을 그대로 넣어 주시면 됩니다.
 * match_all  query 가 실행 됩니다.

## Multi Request Example)
<pre><code>
[WAS]
http://localhost:8080/mquery

[Method]
POST RAW

[Request Body]
[
{
  "target":"http://{YOUR-CLUSTER1}/{YOUR-INDEX1}/_search",
  "query":{}
},
{
  "target":"http://{YOUR-CLUSTER1}/{YOUR-INDEX2}/_search",
  "query":{}
},
{
  "target":"http://{YOUR-CLUSTER2}/{YOUR-INDEX1}/_search",
  "query":{}
},
{
  "target":"http://{YOUR-CLUSTER2}/{YOUR-INDEX2}/_search",
  "query":{}
}
]
</code></pre>

※ Multi Request 의 경우 _msearch API 와 비슷 하게 동작은 합니다.
다만, _msearch의 경우 서로 다른 클러스터간에 통신은 지원 하지 않습니다.

## 추가 Parameters)
* routing
이 기능은 특정 key 를 가지고 문서를 저장 하기 위한 대상 shard 를 지정 하기 위해 사용 합니다.
문서 저장 시 해당 key 에 대한 Grouping 이나 Classify 를 위해 사용 합니다.
자세한 내용은 Elastic 사의 Reference 문서를 참고하세요. (클릭)

* preference
이 기능은 검색 질의 시 아주 유용하게 활용이 가능 합니다.
특정 shard 를 지정 할 수도 있고 질의 하고 싶은 node 를 선택 할 수도 있습니다.﻿
자세한 내용은 Elastic 사의 Reference 문서를 참고하세요. (클릭)

Github Repository)
<https://github.com/HowookJeong/esquery-proxy>