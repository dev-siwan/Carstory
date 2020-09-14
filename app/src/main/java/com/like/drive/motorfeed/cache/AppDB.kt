package com.like.drive.motorfeed.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.like.drive.motorfeed.cache.AppDB.Companion.DB_VERSION
import com.like.drive.motorfeed.cache.common.Converters
import com.like.drive.motorfeed.cache.dao.motor.MotorTypeDao
import com.like.drive.motorfeed.cache.dao.notification.NotificationDao
import com.like.drive.motorfeed.cache.entity.MotorTypeEntity
import com.like.drive.motorfeed.cache.entity.NotificationEntity

/*RoomDB 설정*/
@Database(
    entities = [MotorTypeEntity::class,NotificationEntity::class],
    version = DB_VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDB : RoomDatabase() {

    abstract fun motorTypeDao(): MotorTypeDao
    abstract fun notificationDao(): NotificationDao

    /*싱글턴 패턴*/
    companion object {
        const val DB_VERSION = 1
        private const val DB_NAME = "MotorFeed.db"

        @Volatile
        private var INSTANCE: AppDB? = null

        fun getInstance(context: Context): AppDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context).also { INSTANCE = it }
            }

        private fun build(context: Context) =
            Room.databaseBuilder(
                context.applicationContext, AppDB::class.java,
                DB_NAME
            ).addMigrations(MIGRATION_1_TO_2)
                .build()

        /*마이그레이션*/
        private val MIGRATION_1_TO_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

            }
        }
    }
}