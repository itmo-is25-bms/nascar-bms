package ru.nascar.bms.utils

import io.grpc.Status
import io.grpc.StatusRuntimeException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows

class GrpcAssertions {
    companion object {
        private val IGNORED_FIELDS = arrayOf("memoizedIsInitialized", "memoizedSize", "memoizedHashCode")
        private val IGNORED_FIELDS_WITH_ID = IGNORED_FIELDS + "id_"
    }

    fun <T> assertProtoEquals(expected: T, actual: T, additionalFieldsToIgnore: Array<String> = emptyArray()) {
        assertEquals(expected, actual, IGNORED_FIELDS + additionalFieldsToIgnore)
    }

    fun <T> assertProtoEqualsIgnoringId(expected: T, actual: T, additionalFieldsToIgnore: Array<String> = emptyArray()) {
        assertEquals(expected, actual, IGNORED_FIELDS_WITH_ID + additionalFieldsToIgnore)
    }

    private fun <T> assertEquals(expected: T, actual: T, ignoredFields: Array<String>) {
        assertThat(actual).usingRecursiveComparison()
            .ignoringFields(*ignoredFields)
            .ignoringCollectionOrder()
            .isEqualTo(expected)
    }

    fun <T> assertProtoCollectionEqualsExactly(expected: Collection<T>, actual: Collection<T>) {
        assertThat(actual)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields(*IGNORED_FIELDS)
            .containsExactlyElementsOf(expected as Iterable<Nothing>)
    }

    fun <T> assertProtoCollectionEqualsIgnoringOrder(expected: Collection<T>, actual: Collection<T>) {
        assertThat(actual)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields(*IGNORED_FIELDS)
            .containsExactlyInAnyOrderElementsOf(expected as Iterable<Nothing>)
    }

    fun assertStatusException(
        code: Status.Code = Status.Code.ABORTED,
        message: String? = null,
        executable: () -> Unit
    ) {
        val statusException = assertThrows<StatusRuntimeException> {
            executable()
        }

        assertThat(statusException.status.code).isEqualTo(code)
        message?.let {
            assertThat(statusException.status.description).isEqualTo(it)
        }
    }
}
