# JSON Validation Service

The JSON Validation Service (JVS) is a validator that allows users to check JSON objects.

# How to use

## Docker

Run container built:
```
docker build -t task github.com/OlesyaIv/jsonValidator && docker run -d --rm -p 80:80 task
```

Send file for validation:
```
curl -s --upload-file filename.json http://localhost
```

## Gradle

Run built:
```
chmod +x gradlew
./gradlew builtTask && docker run -d -p 80:80 task
```

Send file for validation:
```
curl -s --upload-file filename.json http://localhost
```

