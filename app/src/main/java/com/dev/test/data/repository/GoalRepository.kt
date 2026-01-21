package com.dev.test.data.repository

import com.dev.test.data.local.dao.GoalDao
import com.dev.test.data.local.entities.GoalEntity
import javax.inject.Inject

class GoalRepository  @Inject constructor(
        private val goalDao: GoalDao
    ) {
        suspend fun insertGoal(goal: GoalEntity) {
            goalDao.insertGoal(goal)
        }
    }
