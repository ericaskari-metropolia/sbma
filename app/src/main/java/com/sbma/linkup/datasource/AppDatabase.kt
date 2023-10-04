package com.sbma.linkup.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sbma.linkup.card.Card
import com.sbma.linkup.card.CardDao
import com.sbma.linkup.tag.Tag
import com.sbma.linkup.tag.TagDao
import com.sbma.linkup.user.User
import com.sbma.linkup.user.UserDao
import com.sbma.linkup.userconnection.ConnectionCard
import com.sbma.linkup.userconnection.ConnectionCardDao
import com.sbma.linkup.userconnection.UserConnection
import com.sbma.linkup.userconnection.UserConnectionDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Card::class, UserConnection::class, Tag::class, ConnectionCard::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    // TODO: should be renamed to cardDao
    abstract fun userCardDao(): CardDao

    // TODO: should be renamed to connectionDao
    abstract fun userConnectionDao(): UserConnectionDao
    abstract fun connectionCardDao(): ConnectionCardDao
    abstract fun tagDao(): TagDao


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
