package com.shishusneh.app.repository

import android.content.Context
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.shishusneh.app.data.dao.UserDao
import com.shishusneh.app.data.entity.UserEntity
import com.shishusneh.app.utils.PasswordUtils
import com.shishusneh.app.utils.Validators
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionDataStore by preferencesDataStore(name = "session_prefs")

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userDao: UserDao
) {
    private val currentUserKey = longPreferencesKey("current_user_id")

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentUserId: Flow<Long?> = context.sessionDataStore.data.flatMapLatest { prefs ->
        val id = prefs[currentUserKey]
        flow {
            if (id != null) {
                val userExists = userDao.getById(id) != null
                if (userExists) {
                    emit(id)
                } else {
                    // Stale session detected, clear it
                    logout()
                    emit(null)
                }
            } else {
                emit(null)
            }
        }
    }

    suspend fun signup(fullName: String, email: String, password: String): Result<Unit> = runCatching {
        require(fullName.isNotBlank()) { "Name is required" }
        require(Validators.isValidEmail(email)) { "Enter a valid email" }
        require(Validators.isStrongPassword(password)) { "Password must be at least 6 characters" }
        check(userDao.getByEmail(email.trim()) == null) { "Account already exists" }

        val id = userDao.insert(
            UserEntity(
                fullName = fullName.trim(),
                email = email.trim(),
                passwordHash = PasswordUtils.hash(password)
            )
        )
        setSession(id)
    }

    suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        require(Validators.isValidEmail(email)) { "Enter a valid email" }
        val user = userDao.getByEmail(email.trim()) ?: error("No account found")
        check(user.passwordHash == PasswordUtils.hash(password)) { "Incorrect password" }
        setSession(user.id)
    }

    suspend fun logout() {
        context.sessionDataStore.edit { it.remove(currentUserKey) }
    }

    suspend fun setSession(userId: Long) {
        context.sessionDataStore.edit { it[currentUserKey] = userId }
    }
}
