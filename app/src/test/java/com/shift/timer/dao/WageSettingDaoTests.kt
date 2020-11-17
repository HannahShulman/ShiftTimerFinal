package com.shift.timer.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.shift.timer.db.AppDB
import com.shift.timer.model.CurrentWorkplace
import com.shift.timer.model.WageSetting
import com.shift.timer.model.Workplace
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RunWith(AndroidJUnit4::class)
class WageSettingDaoTests {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var db: AppDB
    lateinit var dao: WageSettingDao

    val firstWorkplace = Workplace(-1, "workplace 1")
    val secondWorkplace = Workplace(1, "workplace 2")
    val currentWorkplace = CurrentWorkplace(-1)
    val wageSetting = WageSetting(workplaceId = -1, wage = 200)
    val secondWageSetting = WageSetting(workplaceId = 1, wage = 300)
    //tryout


    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDB::class.java).allowMainThreadQueries()
            .build()
        dao = db.wageSettingDao()
    }

    @Test
    fun `insertWageSetting saves setting`() {
        runBlockingTest {
            dao.insertWageSetting(wageSetting)
            assertThat(dao.getWorkplaceById(-1).wage).isEqualTo(200)
        }
    }

    @Test
    fun `currentWorkplaceHourlyPayment return correct value`() {
        runBlockingTest {
            db.workplaceDao().insertWorkplace(firstWorkplace)
            db.workplaceDao().insertCurrentWorkplace(currentWorkplace)
            dao.insertWageSetting(wageSetting)

            val payment = dao.currentWorkplaceHourlyPayment().asLiveData()
            payment.observeForTesting {
                assertThat(payment.value).isEqualTo(200)
            }
        }
    }

    @Test
    fun `currentWorkplaceHourlyPayment is updated when current workplace is chaned`() {
        runBlockingTest {
            db.workplaceDao().insertWorkplace(firstWorkplace)
            db.workplaceDao().insertWorkplace(secondWorkplace)
            db.workplaceDao().insertCurrentWorkplace(currentWorkplace)
            dao.insertWageSetting(wageSetting)
            dao.insertWageSetting(secondWageSetting)

            val payment = dao.currentWorkplaceHourlyPayment().asLiveData()
            payment.observeForTesting {
                assertThat(payment.value).isEqualTo(200)
            }
            db.workplaceDao().setCurrentWorkplace(1)
            payment.observeForTesting {
                assertThat(payment.value).isEqualTo(300)
            }
        }
    }
}

fun <T> LiveData<T>.observeForTesting(block: () -> Unit) {
    val observer = Observer<T> { }
    try {
        observeForever(observer)
        block()
    } finally {
        removeObserver(observer)
    }
}