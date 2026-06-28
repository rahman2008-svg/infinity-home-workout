package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfile)

    @Query("SELECT * FROM weight_logs ORDER BY timestamp ASC")
    fun getWeightLogs(): Flow<List<WeightLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightLog(log: WeightLog)

    @Query("DELETE FROM weight_logs WHERE id = :id")
    suspend fun deleteWeightLog(id: Int)

    @Query("SELECT * FROM workout_history ORDER BY timestamp DESC")
    fun getWorkoutHistory(): Flow<List<WorkoutHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutHistory(history: WorkoutHistory)

    @Query("SELECT * FROM custom_workouts ORDER BY id DESC")
    fun getCustomWorkouts(): Flow<List<CustomWorkout>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomWorkout(workout: CustomWorkout)

    @Query("DELETE FROM custom_workouts WHERE id = :id")
    suspend fun deleteCustomWorkout(id: Int)
}

@Database(
    entities = [UserProfile::class, WeightLog::class, WorkoutHistory::class, CustomWorkout::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "infinity_workout_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
