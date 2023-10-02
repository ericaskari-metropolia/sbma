package com.sbma.linkup.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sbma.linkup.user.User
import com.sbma.linkup.user.UserDao
import com.sbma.linkup.usercard.UserCard
import com.sbma.linkup.usercard.UserCardDao
import com.sbma.linkup.userconnection.UserConnection
import com.sbma.linkup.userconnection.UserConnectionDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, UserCard::class, UserConnection::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userCardDao(): UserCardDao
    abstract fun userConnectionDao(): UserConnectionDao


    companion object {
        private var DB_NAME: String = "linkup"

        @Volatile
        private var Instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback())
                    .build()
                    .also { Instance = it }
            }
        }


        /**
         * Database initializer
         */
        private class AppDatabaseCallback() : Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                Instance?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        Instance!!.populateDatabase()
                    }
                }
            }
        }
    }

    /**
     * Database initializer
     */
    suspend fun populateDatabase() {
        println("one time populating the database")
    }

}
