package ru.nascar.bms.event.domain.exception

import ru.nascar.bms.shared.exception.BmsException

class EventBarReviewScoreOutOfBoundsException(
    message: String,
    errorCode: EventErrorCode,
    errorData: Map<String, String> = emptyMap()
): BmsException(message, errorCode, errorData) {
    companion object {
        fun forActualMinAndMaxScore(actualScore: Int, minScore: Int, maxScore: Int): EventBarReviewScoreOutOfBoundsException {
            return EventBarReviewScoreOutOfBoundsException(
                "Score $actualScore is out of bounds. Value should be between $minScore and $maxScore",
                EventErrorCode.EVENT_BAR_REVIEW_SCORE_OUT_OF_BOUNDS,
                mapOf(
                    "actualScore" to actualScore.toString(),
                    "minScore" to minScore.toString(),
                    "maxScore" to maxScore.toString()
                )
            )
        }
    }
}
