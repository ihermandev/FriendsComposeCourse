package com.ihermandev.sharedtest.infrastructure

import i.herman.infrastructure.Clock

class ControllableClock(
    private val timestamp: Long
): Clock {

    override fun now(): Long {
        return timestamp
    }
}