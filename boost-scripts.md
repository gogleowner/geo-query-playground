# boost-scripts

## elasticsearch bulk api
- https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-bulk.html
- https://elasticsearch-py.readthedocs.io/en/master/index.html
- https://elasticsearch-py.readthedocs.io/en/master/helpers.html

```
def gendata():
    mywords = ['foo', 'bar', 'baz']
    for word in mywords:
        yield {
            "_index": "mywords", // index name
            "_type": "_doc",
            "_id": "num",
            "doc": {
              "word": word
            }
        }
bulk(es, gendata())
```

## kibana docker-compose.yml

```
version: '0.1'
services:
  kibana:
    image: docker.elastic.co/kibana/kibana:6.6.1
    container_name: {CONTAINER_NAME}
    environment:
      SERVER_NAME: localhost
      ELASTICSEARCH_URL: {ELASTICSEARCH_URL}
    ports:
      - 5601:5601
```

## geo-point index mapping, setting

```
PUT zigbang-1
{
  "settings": {
    "number_of_replicas": 0,
    "number_of_shards": 1 // for test..
  },
  "mappings": {
    "_doc": {
      "dynamic": "strict", // maybe we cannot use this setting T_T
      "properties": {
        "location": {
          "type": "geo_point"
        },
        "theothercontents...": { }
      }
    }
  }
}
```

## index add alias

```
POST _aliases
{
  "actions": [
    {
      "add": {
        "index": "zigbang-1",
        "alias": "zigbang"
      }
    }
  ]
}
```
