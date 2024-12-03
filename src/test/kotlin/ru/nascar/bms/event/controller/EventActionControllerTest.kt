package ru.nascar.bms.event.controller

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import com.google.protobuf.ByteString
import org.junit.jupiter.api.Named
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import ru.nascar.bms.NascarBmsIntegrationTest
import ru.nascar.bms.event.domain.model.EventBarReview
import ru.nascar.bms.presentation.abstractions.EventActionServiceProto

private const val EVENT_ID = "EVENT-01"
private const val BAR_ID = "BAR-01"
private const val REVIEW_SCORE = 4

class EventActionControllerTest: NascarBmsIntegrationTest() {
    @Test
    @DatabaseSetup("/controller/event_action/start_event/happy_path/before.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/start_event/happy_path/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `start event - happy path - event started`() {
        // arrange
        val request = getStartEventRequest()

        // act
        val response = eventActionServiceGrpc.start(request)

        // assert
        val expectedResponse = EventActionServiceProto.StartCommandResponse.getDefaultInstance()
        grpcAssert.assertProtoEquals(expectedResponse, response)
    }

    @Test
    @DatabaseSetup("/controller/event_action/start_event/event_not_found/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/start_event/event_not_found/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `start event - event not found - error`() {
        // arrange
        val request = getStartEventRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "Event ${request.eventId} not found"
        ) {
            eventActionServiceGrpc.start(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event_action/start_event/user_is_not_event_creator/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/start_event/user_is_not_event_creator/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `start event - user is not event creator - error`() {
        // arrange
        val request = getStartEventRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "User ${request.userId} can't perform action on event ${request.eventId}"
        ) {
            eventActionServiceGrpc.start(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event_action/start_event/event_in_progress/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/start_event/event_in_progress/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `start event - event is already in progress - error`() {
        // arrange
        val request = getStartEventRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "Can't execute action StartEvent on event ${request.eventId} while in status IN_PROGRESS"
        ) {
            eventActionServiceGrpc.start(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event_action/start_event/finished_event/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/start_event/finished_event/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `start event - event is finished - error`() {
        // arrange
        val request = getStartEventRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "Can't execute action StartEvent on event ${request.eventId} while in status FINISHED"
        ) {
            eventActionServiceGrpc.start(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event_action/finish_event/happy_path/before.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/finish_event/happy_path/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `finish event - happy path - event finished`() {
        // arrange
        val request = getFinishEventRequest()

        // act
        val response = eventActionServiceGrpc.finish(request)

        // assert
        val expectedResponse = EventActionServiceProto.FinishCommand.getDefaultInstance()
        grpcAssert.assertProtoEquals(expectedResponse, response)
    }

    @Test
    @DatabaseSetup("/controller/event_action/finish_event/event_not_found/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/finish_event/event_not_found/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `finish event - event not found - error`() {
        // arrange
        val request = getFinishEventRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "Event ${request.eventId} not found"
        ) {
            eventActionServiceGrpc.finish(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event_action/finish_event/user_is_not_event_creator/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/finish_event/user_is_not_event_creator/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `finish event - user is not event creator - error`() {
        // arrange
        val request = getFinishEventRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "User ${request.userId} can't perform action on event ${request.eventId}"
        ) {
            eventActionServiceGrpc.finish(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event_action/finish_event/event_not_started/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/finish_event/event_not_started/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `finish event - event is not started - error`() {
        // arrange
        val request = getFinishEventRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "Can't execute action FinishEvent on event ${request.eventId} while in status CREATED"
        ) {
            eventActionServiceGrpc.finish(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event_action/finish_event/finished_event/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/finish_event/finished_event/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `finish event - event is already finished - error`() {
        // arrange
        val request = getFinishEventRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "Can't execute action FinishEvent on event ${request.eventId} while in status FINISHED"
        ) {
            eventActionServiceGrpc.finish(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event_action/add_receipt/happy_path/by_creator/before.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_receipt/happy_path/by_creator/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add event receipt - happy path, user is event creator - receipt added`() {
        // arrange
        val request = getAddReceiptRequest()

        // act
        val response = eventActionServiceGrpc.addReceipt(request)

        // assert
        val expectedResponse = EventActionServiceProto.AddReceiptCommandResponse.getDefaultInstance()
        grpcAssert.assertProtoEquals(expectedResponse, response)
    }

    @Test
    @DatabaseSetup("/controller/event_action/add_receipt/happy_path/by_participant/before.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_receipt/happy_path/by_participant/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add event receipt - happy path, user is event participant - receipt added`() {
        // arrange
        val request = getAddReceiptRequest()

        // act
        val response = eventActionServiceGrpc.addReceipt(request)

        // assert
        val expectedResponse = EventActionServiceProto.AddReceiptCommandResponse.getDefaultInstance()
        grpcAssert.assertProtoEquals(expectedResponse, response)
    }

    @Test
    @DatabaseSetup("/controller/event_action/add_receipt/happy_path/second_receipt/before.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_receipt/happy_path/second_receipt/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add event receipt - happy path, second receipt of the event - receipt added`() {
        // arrange
        val request = getAddReceiptRequest()

        // act
        val response = eventActionServiceGrpc.addReceipt(request)

        // assert
        val expectedResponse = EventActionServiceProto.AddReceiptCommandResponse.getDefaultInstance()
        grpcAssert.assertProtoEquals(expectedResponse, response)
    }

    @Test
    @DatabaseSetup("/controller/event_action/add_receipt/not_participant/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_receipt/not_participant/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add event receipt - user is not event participant - error`() {
        // arrange
        val request = getAddReceiptRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "User ${request.userId} not found in event ${request.eventId}"
        ) {
            eventActionServiceGrpc.addReceipt(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event_action/add_receipt/not_connected_bar/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_receipt/not_connected_bar/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add event receipt - bar is not connected to event - error`() {
        // arrange
        val request = getAddReceiptRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "Bar ${request.barId} not found in event ${request.eventId}"
        ) {
            eventActionServiceGrpc.addReceipt(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event_action/add_receipt/event_not_found/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_receipt/event_not_found/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add event receipt - event not found - error`() {
        // arrange
        val request = getAddReceiptRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "Event ${request.eventId} not found"
        ) {
            eventActionServiceGrpc.addReceipt(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event_action/add_receipt/not_finished_event/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_receipt/not_finished_event/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add event receipt - event is not finished - error`() {
        // arrange
        val request = getAddReceiptRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "Can't execute action AddReceipt on event ${request.eventId} while in status IN_PROGRESS"
        ) {
            eventActionServiceGrpc.addReceipt(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event_action/add_receipt/receipt_already_exists/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_receipt/receipt_already_exists/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add event receipt - receipt for event and bar already exists - error`() {
        // arrange
        val request = getAddReceiptRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "Receipt for bar ${request.barId} during event ${request.eventId} already exists"
        ) {
            eventActionServiceGrpc.addReceipt(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event_action/add_review/happy_path/full_review/before.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_review/happy_path/full_review/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add event review - happy path, full review with text - review added`() {
        // arrange
        val request = getAddReviewRequest(reviewText = "Очень очень affordable")

        // act
        val response = eventActionServiceGrpc.addReview(request)

        // assert
        val expectedResponse = EventActionServiceProto.AddReviewCommandResponse.getDefaultInstance()
        grpcAssert.assertProtoEquals(expectedResponse, response)
    }

    @Test
    @DatabaseSetup("/controller/event_action/add_review/happy_path/review_without_text/before.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_review/happy_path/review_without_text/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add event review - happy path, review without text - review added`() {
        // arrange
        val request = getAddReviewRequest()

        // act
        val response = eventActionServiceGrpc.addReview(request)

        // assert
        val expectedResponse = EventActionServiceProto.AddReviewCommandResponse.getDefaultInstance()
        grpcAssert.assertProtoEquals(expectedResponse, response)
    }

    @Test
    @DatabaseSetup("/controller/event_action/add_review/happy_path/second_bar_same_user/before.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_review/happy_path/second_bar_same_user/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add event review - happy path, review on second bar by same user - review added`() {
        // arrange
        val request = getAddReviewRequest()

        // act
        val response = eventActionServiceGrpc.addReview(request)

        // assert
        val expectedResponse = EventActionServiceProto.AddReviewCommandResponse.getDefaultInstance()
        grpcAssert.assertProtoEquals(expectedResponse, response)
    }

    @Test
    @DatabaseSetup("/controller/event_action/add_review/happy_path/second_review_on_bar_other_user/before.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_review/happy_path/second_review_on_bar_other_user/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add event review - happy path, second review on bar by another user - review added`() {
        // arrange
        val request = getAddReviewRequest()

        // act
        val response = eventActionServiceGrpc.addReview(request)

        // assert
        val expectedResponse = EventActionServiceProto.AddReviewCommandResponse.getDefaultInstance()
        grpcAssert.assertProtoEquals(expectedResponse, response)
    }

    @Test
    @DatabaseSetup("/controller/event_action/add_review/event_not_found/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_review/event_not_found/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add event review - event not found - error`() {
        // arrange
        val request = getAddReviewRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "Event ${request.eventId} not found"
        ) {
            eventActionServiceGrpc.addReview(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event_action/add_review/not_participant/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_review/not_participant/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add event review - user is not event participant - error`() {
        // arrange
        val request = getAddReviewRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "User ${request.userId} not found in event ${request.eventId}"
        ) {
            eventActionServiceGrpc.addReview(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event_action/add_review/not_connected_bar/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_review/not_connected_bar/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add event review - bar is not connected to event - error`() {
        // arrange
        val request = getAddReviewRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "Bar ${request.barId} not found in event ${request.eventId}"
        ) {
            eventActionServiceGrpc.addReview(request)
        }
    }

    @ParameterizedTest
    @MethodSource("scoresOutOfBounds")
    @DatabaseSetup("/controller/event_action/add_review/score_out_of_bounds/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_review/score_out_of_bounds/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add event review - score out of bounds - error`(score: Int) {
        // arrange
        val request = getAddReviewRequest(score = score)

        // act & assert
        grpcAssert.assertStatusException(
            message = "Score $score is out of bounds. Value should be between ${EventBarReview.MIN_SCORE} and ${EventBarReview.MAX_SCORE}"
        ) {
            eventActionServiceGrpc.addReview(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event_action/add_review/happy_path/review_already_exists/before.xml")
    @ExpectedDatabase(
        value = "/controller/event_action/add_review/happy_path/review_already_exists/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add another event review - happy path, review by user for event and bar already exists - review updated`() {
        // arrange
        val request = getAddReviewRequest(reviewText = "Очень очень affordable")

        // act
        val response = eventActionServiceGrpc.addReview(request)

        // assert
        val expectedResponse = EventActionServiceProto.AddReviewCommandResponse.getDefaultInstance()
        grpcAssert.assertProtoEquals(expectedResponse, response)
    }

    private fun getStartEventRequest(): EventActionServiceProto.StartCommand {
        return EventActionServiceProto.StartCommand.newBuilder()
            .setEventId(EVENT_ID)
            .setUserId(USER_ID)
            .build()
    }

    private fun getFinishEventRequest(): EventActionServiceProto.FinishCommand {
        return EventActionServiceProto.FinishCommand.newBuilder()
            .setEventId(EVENT_ID)
            .setUserId(USER_ID)
            .build()
    }

    private fun getAddReceiptRequest(): EventActionServiceProto.AddReceiptCommand {
        return EventActionServiceProto.AddReceiptCommand.newBuilder()
            .setEventId(EVENT_ID)
            .setBarId(BAR_ID)
            .setUserId(USER_ID)
            .setReceiptData(ByteString.fromHex("DEADBEEF"))
            .build()
    }

    private fun getAddReviewRequest(score: Int = REVIEW_SCORE, reviewText: String? = null): EventActionServiceProto.AddReviewCommand {
        val reviewBuilder = EventActionServiceProto.AddReviewCommandReviewDto.newBuilder()
            .setScore(score)

        reviewText?.let { reviewBuilder.setReviewText(reviewText) }

        val review = reviewBuilder.build()

        return EventActionServiceProto.AddReviewCommand.newBuilder()
            .setEventId(EVENT_ID)
            .setBarId(BAR_ID)
            .setUserId(USER_ID)
            .setReview(review)
            .build()
    }

    companion object {
        @JvmStatic
        fun scoresOutOfBounds():  List<Named<Int>> {
            return listOf(
                Named.of("negative", -1),
                Named.of("zero", 0),
                Named.of("greater than max", 100)
            )
        }
    }
}