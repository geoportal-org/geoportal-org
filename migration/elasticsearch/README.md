Export Elasticsearch index geoss_index to a JSON file and store it in an archive.

To export data from the geoss_index in Elasticsearch to a JSON file, the elasticdump tool was used ->  ->  https://www.npmjs.com/package/elasticdump

Project page on GitHub -> https://github.com/elasticsearch-dump/elasticsearch-dump

Install npm (Node Package Manager) and check the version.

```bash
npm -v
  ```

Installation of elasticdump using npm

```bash
npm install elasticdump -g
```

Example of exporting data from Elasticsearch to a JSON file for the DEV environment:

```bash
elasticdump --input=http://10.254.2.196:9200/geoss_index --output=/geoss_index.json --type=data
```

Connect to Full VPN Eversis network. Establish a network tunnel connection to the Elasticsearch PROD server and initiate the export.
    
```bash
ssh -L 9201:localhost:9201 -p 60022 user@89.40.146.201 -i key-private.pem
```

On PROD, there are at least 2,629,649 documents, so the processing will take several hours. In this case, the file splitting should be enabled, and the file size should be set to 50MB per chunk.

```bash
elasticdump --input=http://localhost:9201/geoss_index --output=/geoss_index.json --type=data --fileSize=50mb
```