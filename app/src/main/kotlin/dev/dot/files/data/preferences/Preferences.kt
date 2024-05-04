package dev.dot.files.data.preferences

import kotlinx.serialization.Serializable

enum class SortType {
    Alphabetical,
    LastModified,
    Length,
    Size,
}

enum class SortOrder {
    Ascending,
    Descending,
}

@Serializable
data class Preferences(
    val showHidden: Boolean,
    val sortOrder: SortOrder,
    val sortType: SortType,
)
