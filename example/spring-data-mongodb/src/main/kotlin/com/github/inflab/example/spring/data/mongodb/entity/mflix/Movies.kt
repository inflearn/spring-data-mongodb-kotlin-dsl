package com.github.inflab.example.spring.data.mongodb.entity.mflix

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("movies")
data class Movies(
    @Id
    val id: String,
    val plot: String,
    val genres: List<String>,
    val runtime: Int,
    val cast: List<String>,
    val poster: String,
    val title: String,
    val fullplot: String,
    val languages: List<String>,
    val released: LocalDateTime,
    val directors: List<String>,
    val rated: String,
    val lastupdated: LocalDateTime,
    val year: Int,
    val imdb: MovieImdb,
    val contries: List<String>,
    val type: String,
    val numMflixComments: Int,
)
