package ru.nascar.bms.event.domain.event

/**
 * Событие добавления отзыва к бару
 */
data class ReviewAddedEvent(
    /**
     * Идентификатор мероприятия
     */
    val eventId: String,
    /**
     * Идентификатор бара, к которому добавили отзыв
     */
    val barId: String,
    /**
     * Текст отзыва
     */
    val reviewText: String,
)
