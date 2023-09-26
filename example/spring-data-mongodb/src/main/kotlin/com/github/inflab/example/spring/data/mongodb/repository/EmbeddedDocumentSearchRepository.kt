package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.entity.supplies.SaleItem
import com.github.inflab.example.spring.data.mongodb.entity.supplies.Sales
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import com.github.inflab.spring.data.mongodb.core.aggregation.search.ScoreEmbeddedAggregateStrategy
import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository

@Repository
class EmbeddedDocumentSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class Item(
        val name: String,
        val tags: List<String>,
    )

    data class ItemDto(
        val items: List<Item>,
        val score: Double,
    )

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/embedded-document/#examples">Basic Example</a>
     */
    fun findItemsByMean(): AggregationResults<ItemDto> {
        val aggregation = aggregation {
            search {
                embeddedDocument {
                    path(Sales::items)
                    operator {
                        compound {
                            must {
                                text {
                                    path { +(Sales::items..SaleItem::tags) }
                                    query("school")
                                }
                            }
                            should {
                                text {
                                    path { +(Sales::items..SaleItem::name) }
                                    query("backpack")
                                }
                            }
                        }
                    }
                    score {
                        embedded(ScoreEmbeddedAggregateStrategy.MEAN)
                    }
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                Sales::items..SaleItem::name alias "items.name"
                Sales::items..SaleItem::tags alias "items.tags"
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Sales, ItemDto>(aggregation)
    }

    data class FacetDto(val count: Count, val facet: Facet)
    data class Count(val lowerBound: Long)
    data class Facet(val purchaseMethodFacet: PurchaseMethodFacet)
    data class PurchaseMethodFacet(val buckets: List<Bucket>)
    data class Bucket(val id: String, val count: Long)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/embedded-document/#examples">Facet Example</a>
     */
    fun findItemsByFacet(): AggregationResults<FacetDto> {
        val aggregation = aggregation {
            searchMeta {
                facet {
                    operator {
                        embeddedDocument {
                            path(Sales::items)
                            operator {
                                compound {
                                    must {
                                        text {
                                            path { +(Sales::items..SaleItem::tags) }
                                            query("school")
                                        }
                                    }
                                    should {
                                        text {
                                            path { +(Sales::items..SaleItem::name) }
                                            query("backpack")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    "purchaseMethodFacet" stringFacet {
                        path(Sales::purchaseMethod)
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Sales, FacetDto>(aggregation)
    }
}
