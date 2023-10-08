package com.github.inflab.spring.data.mongodb.core.util

import io.kotest.matchers.shouldBe
import org.bson.Document
import org.bson.json.JsonWriterSettings
import org.intellij.lang.annotations.Language
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.AggregationOperation

internal val jsonWriterSettings = JsonWriterSettings.builder().indent(true).build()

fun Document.shouldBeJson(@Language("JSON") json: String) {
    toBsonDocument().toJson(jsonWriterSettings) shouldBe json
}

fun AggregationOperation.shouldBeJson(@Language("JSON") json: String) {
    toPipelineStages(Aggregation.DEFAULT_CONTEXT).first().shouldBeJson(json)
}

fun AggregationExpression.shouldBeJson(@Language("JSON") json: String) {
    toDocument(Aggregation.DEFAULT_CONTEXT).shouldBeJson(json)
}
