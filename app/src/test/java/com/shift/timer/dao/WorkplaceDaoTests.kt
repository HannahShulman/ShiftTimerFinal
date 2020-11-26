package com.shift.timer.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shift.timer.db.AppDB
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkplaceDaoTests {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var db: AppDB
    lateinit var dao: WorkplaceDao

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDB::class.java).allowMainThreadQueries()
            .build()
        dao = db.workplaceDao()
    }
}