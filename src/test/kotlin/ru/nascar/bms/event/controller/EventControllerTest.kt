package ru.nascar.bms.event.controller

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import com.google.protobuf.Timestamp
import org.junit.jupiter.api.Test
import ru.nascar.bms.NascarBmsIntegrationTest
import ru.nascar.bms.event.controller.mapping.toTimestamp
import ru.nascar.bms.presentation.abstractions.EventServiceProto
import java.time.Instant

private const val EVENT_ID = "EVENT-01"
private const val EVENT_NAME = "ITMOtion Night"
private const val EVENT_PASSCODE = "M34021"
private val EVENT_START_DATE = Instant.parse("2025-10-10T11:11:11.111Z").toTimestamp()

class EventControllerTest : NascarBmsIntegrationTest() {
    @Test
    @DatabaseSetup("/controller/event/create/happy_path/single_bar/before.xml")
    @ExpectedDatabase(
        value = "/controller/event/create/happy_path/single_bar/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `create event - happy path with single bar - create event with given parameters`() {
        // arrange
        val request = getCreateEventRequest(setOf("BAR-01"))

        // act
        val response = eventServiceGrpc.create(request)

        // assert
        val expectedEvent = getEventDto(
            name = request.event.name,
            startDate = request.event.startDate,
            authorUserId = request.userId,
            barIds = request.event.barIdsList,
            participantIds = listOf(request.userId),
            status = EventServiceProto.EventStatusDto.EventStatusDto_Created
        )

        grpcAssert.assertProtoEqualsIgnoringId(
            expected = expectedEvent,
            actual = response.event,
            additionalFieldsToIgnore = arrayOf("passcode_")
        )
    }

    @Test
    @DatabaseSetup("/controller/event/create/happy_path/multiple_bars/before.xml")
    @ExpectedDatabase(
        value = "/controller/event/create/happy_path/multiple_bars/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `create event - happy path with multiple bars - create event with given parameters`() {
        // arrange
        val request = getCreateEventRequest(setOf("BAR-00", "BAR-01"))

        // act
        val response = eventServiceGrpc.create(request)

        // assert
        val expectedEvent = getEventDto(
            name = request.event.name,
            startDate = request.event.startDate,
            authorUserId = request.userId,
            barIds = request.event.barIdsList,
            participantIds = listOf(request.userId),
            status = EventServiceProto.EventStatusDto.EventStatusDto_Created
        )

        grpcAssert.assertProtoEqualsIgnoringId(
            expected = expectedEvent,
            actual = response.event,
            additionalFieldsToIgnore = arrayOf("passcode_")
        )
    }

    @Test
    @DatabaseSetup("/controller/event/create/happy_path/no_bars/before.xml")
    @ExpectedDatabase(
        value = "/controller/event/create/happy_path/no_bars/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `create event - happy path without bars - create event with given parameters`() {
        // arrange
        val request = getCreateEventRequest(emptySet())

        // act
        val response = eventServiceGrpc.create(request)

        // assert
        val expectedEvent = getEventDto(
            name = request.event.name,
            startDate = request.event.startDate,
            authorUserId = request.userId,
            barIds = request.event.barIdsList,
            participantIds = listOf(request.userId),
            status = EventServiceProto.EventStatusDto.EventStatusDto_Created
        )

        grpcAssert.assertProtoEqualsIgnoringId(
            expected = expectedEvent,
            actual = response.event,
            additionalFieldsToIgnore = arrayOf("passcode_")
        )
    }

    @Test
    @DatabaseSetup("/controller/event/create/happy_path/pass_bar_id_twice/before.xml")
    @ExpectedDatabase(
        value = "/controller/event/create/happy_path/pass_bar_id_twice/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `create event - pass similar bar id twice - create event with given parameters without duplicates`() {
        // arrange
        val request = getCreateEventRequest(listOf("BAR-01", "BAR-01"))

        // act
        val response = eventServiceGrpc.create(request)

        // assert
        val expectedEvent = getEventDto(
            name = request.event.name,
            startDate = request.event.startDate,
            authorUserId = request.userId,
            barIds = request.event.barIdsList.distinct(),
            participantIds = listOf(request.userId),
            status = EventServiceProto.EventStatusDto.EventStatusDto_Created
        )

        grpcAssert.assertProtoEqualsIgnoringId(
            expected = expectedEvent,
            actual = response.event,
            additionalFieldsToIgnore = arrayOf("passcode_")
        )
    }

    @Test
    @DatabaseSetup("/controller/event/create/bar_not_found/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event/create/bar_not_found/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `create event - bar ids not found - error`() {
        // arrange
        val request = getCreateEventRequest(setOf("BAR-01", "BAR-02"))

        // act & assert
        grpcAssert.assertStatusException(
            message = "Bars with ids ${request.event.barIdsList.sorted()} were not found"
        ) {
            eventServiceGrpc.create(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event/get_by_id/happy_path/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event/get_by_id/happy_path/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `get by id - happy path - return event with requested id`() {
        // arrange
        val request = getGetByIdRequest()

        // act
        val response = eventServiceGrpc.getById(request)

        // assert
        val expectedEvent = getEventDto(
            id = request.eventId,
            name = "ITMOtion Night",
            startDate = Instant.parse("2025-10-10T11:11:11.111Z").toTimestamp(),
            authorUserId = "testUser",
            barIds = listOf("BAR-00", "BAR-01"),
            participantIds = listOf("testUser", "kslacker"),
            status = EventServiceProto.EventStatusDto.EventStatusDto_InProgress,
            passcode = "M34021"
        )

        grpcAssert.assertProtoEquals(expectedEvent, response.event)
    }

    @Test
    @DatabaseSetup("/controller/event/get_by_id/event_not_found/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event/get_by_id/event_not_found/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `get by id - event not found - error`() {
        // arrange
        val request = getGetByIdRequest()

        // act & assert
        grpcAssert.assertStatusException(
            message = "Event ${request.eventId} not found"
        ) {
            eventServiceGrpc.getById(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event/get_by_passcode/happy_path/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event/get_by_passcode/happy_path/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `get by passcode - happy path - return event with requested passcode`() {
        // arrange
        val request = getGetByPasscodeRequest()

        // act
        val response = eventServiceGrpc.getByPasscode(request)

        // assert
        val expectedEvent = getEventDto(
            id = "EVENT-01",
            name = "ITMOtion Night",
            startDate = Instant.parse("2025-10-10T11:11:11.111Z").toTimestamp(),
            authorUserId = "testUser",
            barIds = listOf("BAR-00", "BAR-01"),
            participantIds = listOf("testUser", "kslacker"),
            status = EventServiceProto.EventStatusDto.EventStatusDto_InProgress,
            passcode = request.passcode
        )

        grpcAssert.assertProtoEquals(expectedEvent, response.event)
    }

    @Test
    @DatabaseSetup("/controller/event/get_by_passcode/event_not_found/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event/get_by_passcode/event_not_found/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `get by passcode - event not found - return default instance`() {
        // arrange
        val request = getGetByPasscodeRequest()

        // act
        val response = eventServiceGrpc.getByPasscode(request)

        // assert
        val expectedEvent = EventServiceProto.EventDto.getDefaultInstance()
        grpcAssert.assertProtoEquals(expectedEvent, response.event)
    }

    @Test
    @DatabaseSetup("/controller/event/get_by_user_id/happy_path/user_with_events/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event/get_by_user_id/happy_path/user_with_events/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `get by user id - user has events - return all events user participate in, in any order`() {
        // arrange
        val request = getGetByUserIdRequest("kslacker")

        // act
        val response = eventServiceGrpc.getByUserId(request)

        // assert
        val expectedEvents = listOf(
            getEventDto(
                id = "EVENT-01",
                name = "ITMOtion Night",
                startDate = Instant.parse("2025-10-10T11:11:11.111Z").toTimestamp(),
                authorUserId = "testUser",
                barIds = listOf("BAR-00", "BAR-01"),
                participantIds = listOf("testUser", "kslacker"),
                status = EventServiceProto.EventStatusDto.EventStatusDto_InProgress,
                passcode = "M34021"
            ),
            getEventDto(
                id = "EVENT-02",
                name = "Новый год",
                startDate = Instant.parse("2025-01-01T00:00:00.000Z").toTimestamp(),
                authorUserId = "suren",
                barIds = listOf("BAR-00"),
                participantIds = listOf("kslacker"),
                status = EventServiceProto.EventStatusDto.EventStatusDto_Created,
                passcode = "DED_MOROZ"
            )
        )
        grpcAssert.assertProtoCollectionEqualsIgnoringOrder(expectedEvents, response.eventsList)
    }

    @Test
    @DatabaseSetup("/controller/event/get_by_user_id/happy_path/user_without_events/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event/get_by_user_id/happy_path/user_without_events/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `get by user id - user has no events - return empty list`() {
        // arrange
        val request = getGetByUserIdRequest("kslacker")

        // act
        val response = eventServiceGrpc.getByUserId(request)

        // assert
        val expectedEvents = emptyList<EventServiceProto.EventDto>()
        grpcAssert.assertProtoCollectionEqualsExactly(expectedEvents, response.eventsList)
    }

    @Test
    @DatabaseSetup("/controller/event/add_user/happy_path/before.xml")
    @ExpectedDatabase(
        value = "/controller/event/add_user/happy_path/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add user to event - happy path - add user to event participants`() {
        // arrange
        val request = getAddUserToEventRequest(eventId = "EVENT-02", userId = "lopa10ko")

        // act
        val response = eventServiceGrpc.addUserToEvent(request)

        // arrange
        val expectedResponse = EventServiceProto.AddUserToEventCommandResponse.getDefaultInstance()
        grpcAssert.assertProtoEquals(expectedResponse, response)
    }

    @Test
    @DatabaseSetup("/controller/event/add_user/event_not_found/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event/add_user/event_not_found/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add user to event - event not found - error`() {
        // arrange
        val request = getAddUserToEventRequest(eventId = "EVENT-02", userId = "lopa10ko")

        // act & assert
        grpcAssert.assertStatusException(
            message = "Event ${request.eventId} not found"
        ) {
            eventServiceGrpc.addUserToEvent(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event/add_user/user_is_already_added/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event/add_user/user_is_already_added/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `add user to event - user is already participant - error`() {
        // arrange
        val request = getAddUserToEventRequest(eventId = "EVENT-01", userId = "kslacker")

        // act & assert
        grpcAssert.assertStatusException(
            message = "User ${request.userId} in event ${request.eventId} already exists"
        ) {
            eventServiceGrpc.addUserToEvent(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event/remove_user/happy_path/before.xml")
    @ExpectedDatabase(
        value = "/controller/event/remove_user/happy_path/after.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `remove user from event - happy path - remove user from event participants`() {
        // arrange
        val request = getRemoveUserFromEventRequest(eventId = "EVENT-01", userId = "kslacker")

        // act
        val response = eventServiceGrpc.removeUserFromEvent(request)

        // assert
        val expectedResponse = EventServiceProto.RemoveUserFromEventCommandResponse.getDefaultInstance()
        grpcAssert.assertProtoEquals(expectedResponse, response)
    }

    @Test
    @DatabaseSetup("/controller/event/remove_user/event_not_found/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event/remove_user/event_not_found/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `remove user from event - event not found - error`() {
        // arrange
        val request = getRemoveUserFromEventRequest(eventId = "EVENT-02", userId = "kslacker")

        // act & assert
        grpcAssert.assertStatusException(
            message = "Event ${request.eventId} not found"
        ) {
            eventServiceGrpc.removeUserFromEvent(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event/remove_user/remove_event_author/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event/remove_user/remove_event_author/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `remove user from event - remove author from participants - error`() {
        // arrange
        val request = getRemoveUserFromEventRequest(eventId = "EVENT-01", userId = "testUser")

        grpcAssert.assertStatusException(
            message = "Can't remove author ${request.userId} of event ${request.eventId}"
        ) {
            eventServiceGrpc.removeUserFromEvent(request)
        }
    }

    @Test
    @DatabaseSetup("/controller/event/remove_user/user_is_not_participant/immutable.xml")
    @ExpectedDatabase(
        value = "/controller/event/remove_user/user_is_not_participant/immutable.xml",
        assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED
    )
    fun `remove user from event - user is not participant - error`() {
        // arrange
        val request = getRemoveUserFromEventRequest(eventId = "EVENT-01", userId = "lopa10ko")

        // act & assert
        grpcAssert.assertStatusException(
            message = "User ${request.userId} not found in event ${request.eventId}"
        ) {
            eventServiceGrpc.removeUserFromEvent(request)
        }
    }

    private fun getCreateEventRequest(barIds: Collection<String>): EventServiceProto.CreateCommand {
        val createEventDto = EventServiceProto.CreateCommandEventDto.newBuilder()
            .setName(EVENT_NAME)
            .setStartDate(EVENT_START_DATE)
            .addAllBarIds(barIds)
            .build()

        return EventServiceProto.CreateCommand.newBuilder()
            .setEvent(createEventDto)
            .setUserId(USER_ID)
            .build()
    }

    private fun getGetByIdRequest(): EventServiceProto.GetByIdQuery {
        return EventServiceProto.GetByIdQuery.newBuilder()
            .setEventId(EVENT_ID)
            .build()
    }

    private fun getGetByPasscodeRequest(): EventServiceProto.GetByPasscodeQuery {
        return EventServiceProto.GetByPasscodeQuery.newBuilder()
            .setPasscode(EVENT_PASSCODE)
            .build()
    }

    private fun getGetByUserIdRequest(userId: String): EventServiceProto.GetByUserIdQuery {
        return EventServiceProto.GetByUserIdQuery.newBuilder()
            .setUserId(userId)
            .build()
    }

    private fun getAddUserToEventRequest(eventId: String, userId: String): EventServiceProto.AddUserToEventCommand {
        return EventServiceProto.AddUserToEventCommand.newBuilder()
            .setEventId(eventId)
            .setUserId(userId)
            .build()
    }

    private fun getRemoveUserFromEventRequest(eventId: String, userId: String): EventServiceProto.RemoveUserFromEventCommand {
        return EventServiceProto.RemoveUserFromEventCommand.newBuilder()
            .setEventId(eventId)
            .setUserId(userId)
            .build()
    }

    private fun getEventDto(
        name: String,
        startDate: Timestamp,
        authorUserId: String,
        barIds: List<String>,
        participantIds: List<String>,
        status: EventServiceProto.EventStatusDto,
        id: String = "",
        passcode: String = ""
    ): EventServiceProto.EventDto {
        return EventServiceProto.EventDto.newBuilder()
            .setId(id)
            .setName(name)
            .setStartDate(startDate)
            .setAuthorUserId(authorUserId)
            .addAllBarIds(barIds)
            .addAllParticipantIds(participantIds)
            .setStatus(status)
            .setPasscode(passcode)
            .build()
    }
}
