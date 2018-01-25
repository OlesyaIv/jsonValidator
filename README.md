# JSON Validation Service

The JSON Validation Service (JVS) is a validator that allows users to check JSON objects.

# How to use

Run container built:
```
docker build -t task https://github.com/OlesyaIv/jsonValidator.git && docker run -d --rm -p 80:80 task
```

Send file for validation:
```
curl -s --data-binary @filename.json http://localhost
```
