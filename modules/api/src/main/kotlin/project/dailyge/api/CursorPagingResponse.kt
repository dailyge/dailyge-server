package project.dailyge.api

class CursorPagingResponse<T>(
    _data: List<T>,
    val size: Int,
) {
    val data: List<T> = if (_data.size > size) _data.take(size - 1) else _data
    val totalCount = data.size
    val hasNext: Boolean = _data.size > size
}
