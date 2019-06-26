package highlighting

import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.helper.highlighting.toHighlightedString
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.search.HighlightResult
import com.algolia.search.model.search.MatchLevel
import com.algolia.search.serialize.Key_HighlightResult
import kotlinx.serialization.internal.HashMapSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.json
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestHighlighter {

    private val attribute = Attribute("name")
    private val highlightResult = HighlightResult("<em>John</em>ny", MatchLevel.Partial, listOf("Johnny"))
    private val highlights = mapOf(attribute to highlightResult)
    private val hit = ResponseSearch.Hit(json {
        attribute.raw to "Johnny"
        "age" to 42
        Key_HighlightResult to Json(JsonConfiguration.Stable).toJson(
            HashMapSerializer(
                Attribute,
                HighlightResult.serializer()
            ), highlights
        )
    })

    @Test
    fun toHighlightedString() {
        val highlight = hit.toHighlightedString(attribute)

        highlight.shouldNotBeNull()
        highlight?.let {
            highlight.parts.size shouldEqual 2
            highlight.highlightedParts shouldEqual listOf("John")

            printHighlight(highlight)
        }
    }

    private fun printHighlight(highlight: HighlightedString) {
        println("${highlight.parts.size} parts:")
        highlight.parts.forEach {
            println("${it.content.length}: $it")
        }
    }
}