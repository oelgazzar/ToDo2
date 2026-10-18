package com.example.todo.data

import androidx.compose.runtime.mutableStateOf
import androidx.room3.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todo.models.Category
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: TaskDao

    @Before
    fun setupDatabase() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java)
            .build()

        dao = db.taskDao()
    }

    @After
    fun clean() {
        db.close()
    }

    @Test
    fun insertTask_readIt_thenDelete() = runTest {
        val task = TaskEntity(
            title = "Test Task",
            category = Category.WORK,
            completed = false
        )

        dao.upsertTask(task)

        var tasks = dao.getAllTasks().first()

        assertEquals(1, tasks.count())

        val fetchedTask = tasks.first()
        assertEquals("Test Task", fetchedTask.title)

        dao.deleteTask(fetchedTask)

        tasks = dao.getAllTasks().first()
        assertEquals(0, tasks.count())
    }

    @Test
    fun insertTask_readIt_thenUpdate() = runTest {
        val task = TaskEntity(
            title = "Test Task",
            category = Category.WORK,
            completed = false)

        dao.upsertTask(task)

        var tasks = dao.getAllTasks().first()

        assertEquals(1, tasks.count())



        val fetchedTask = tasks.first()
        assertEquals("Test Task", fetchedTask.title)

        dao.upsertTask(fetchedTask.copy(title = "Updated Task"))

        tasks = dao.getAllTasks().first()

        assertEquals(1, tasks.count())

        val updatedTask = tasks.first()
        assertEquals("Updated Task", updatedTask.title)
    }

    @Test
    fun insertTask_getById() = runTest {
        val task = TaskEntity(
            id = 324L,
            title = "Test Task",
            category = Category.WORK,
            completed = false)

        dao.upsertTask(task)

        val fetchedTask = dao.getTask(324L).first()

        assertEquals("Test Task", fetchedTask?.title)
    }

    @Test
    fun getByNonExistingId_returnsNull() = runTest {
        val fetchedTask = dao.getTask(324L).first()
        assertEquals(null, fetchedTask)
    }
}