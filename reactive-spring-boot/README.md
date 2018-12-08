[![GitHub license](https://img.shields.io/github/license/mashape/apistatus.svg)]()

# reactive-shop

## Requirements

1. Java - 1.8.x

2. Maven - 3.x.x

3. MongoDB - 3.x.x


### Run command

```bash
mvn package && java -jar target/reactive-shop-[version]-SNAPSHOT.jar
```

using maven plugin:

```bash
mvn spring-boot:run
```

The server will start at <http://localhost:9000>

## Docker integration

### Run MONGO using Docker for Windows
- `docker volume create --name=mongodata` 
- `docker run -d -p 27017:27017 -v mongodata:/data/db mongo`

## Swagger integration

<http://localhost:9000/swagger-ui.html#/>