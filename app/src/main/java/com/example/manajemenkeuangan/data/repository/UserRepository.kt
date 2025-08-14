package com.example.manajemenkeuangan.data.repository

import com.example.manajemenkeuangan.data.db.UserDao
import com.example.manajemenkeuangan.data.user.User
import kotlinx.coroutines.flow.firstOrNull

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email).firstOrNull()
    }
}