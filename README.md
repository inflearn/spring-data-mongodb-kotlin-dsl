# spring-data-mongodb-kotlin-dsl

This project is a Kotlin library that provides a type-safe DSL for aggregations in Spring Data MongoDB.

## Key Features

- Type-safe DSL for building pipeline of [aggregation operations](https://www.mongodb.com/docs/manual/aggregation/)
- Support for [Atlas Search](https://docs.atlas.mongodb.com/atlas-search)

## Requirements

- Spring Data MongoDB 3.4.0 or higher
- Java 8 or higher
- Kotlin 1.8 or higher

## Installation

> [!NOTE]  
> We do not have a publishing mechanism to a maven repository so the easiest way to add the library to your app is via a JitPack Dependency.

[![](https://jitpack.io/v/inflearn/spring-data-mongodb-kotlin-dsl.svg)](https://jitpack.io/#inflearn/spring-data-mongodb-kotlin-dsl)

- build.gradle.kts

```kotlin
repositories {
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    implementation("com.github.inflearn:spring-data-mongodb-kotlin-dsl:$version")
}
```

## Examples

This project provides `example` module that contains examples of how to use the library.
The codes used in the example are all DSL representations of the examples from the official MongoDB documentation.
Each aggregation operation is implemented as a separate repository class with `MongoTemplate` as a dependency.

For example, There is an example of `text` operation from `$search` stage
in [TextSearchRepository.kt](example/spring-data-mongodb/src/main/kotlin/com/github/inflab/example/spring/data/mongodb/repository/atlas/TextSearchRepository.kt)

```kotlin
@Repository
class TextSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class TitleAndScoreDto(
        val title: String,
        val score: Double,
    )

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/text/#basic-example">Basic Example</a>
     */
    fun findTitleWithSufer(): AggregationResults<TitleAndScoreDto> {
        val aggregation = aggregation {
            search {
                text {
                    path(Movies::title)
                    query("surfer")
                }
            }

            project {
                excludeId()
                +Movies::title
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndScoreDto>(aggregation)
    }
}
```

It is equivalent to the following MongoDB query
from [the official documentation](https://www.mongodb.com/docs/atlas/atlas-search/text/#basic-example).

```javascript
db.movies.aggregate([
    {
        $search: {
            "text": {
                "path": "title",
                "query": "surfer"
            }
        }
    },
    {
        $project: {
            "_id": 0,
            "title": 1,
            score: {$meta: "searchScore"}
        }
    }
])
```

### Running tests from examples

Each example repository contains a test code that verifies that the query is executed correctly.
There are two types of tests: one that requires a MongoDB Community instance and another that requires MongoDB Atlas.

- MongoDB Community

The repositories that are not under the `com.github.inflab.example.spring.data.mongodb.repository.atlas` package requires a MongoDB Community instance.
To run the MongoDB Community instance, we use [Testcontainers](https://www.testcontainers.org/).
Therefore, you need to install and run Docker on your machine.

- MongoDB Atlas

The repositories that are under the `com.github.inflab.example.spring.data.mongodb.repository.atlas` package requires a MongoDB Atlas instance.
You need to create a MongoDB Atlas instance and set the connection information in the `application.yml` file.

> [!NOTE]
> you can copy a sample `application.yml` file
> from [application.sample.yml](example/spring-data-mongodb/src/test/resources/application.sample.yml)

```yaml
spring:
  data:
    mongodb:
      username: "username"
      password: "password"
      host: "host"
```

You should refer to [the following manual](https://www.mongodb.com/docs/atlas/sample-data/) to configure sample data as well.
Because most example codes are provided based on the sample data.
If test codes are using `Atlas Search`, you also need to create a suitable search index.
Please refer to each example documentation for more information.

## Contributors

<a href="https://github.com/inflearn/spring-data-mongodb-kotlin-dsl/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=inflearn/spring-data-mongodb-kotlin-dsl" />
</a>

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
