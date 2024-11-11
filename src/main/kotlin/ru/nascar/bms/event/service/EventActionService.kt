package ru.nascar.bms.event.service

interface EventActionService {
    fun start(eventId: String, userId: String)
    fun addReceipt(eventId: String, barId: String, userId: String, receiptData: ByteArray)
    fun addReview(eventId: String, barId: String, userId: String, score: Int, reviewText: String)
    fun finish(eventId: String, userId: String)
}