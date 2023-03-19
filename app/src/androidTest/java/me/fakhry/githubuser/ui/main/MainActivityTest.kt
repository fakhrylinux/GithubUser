package me.fakhry.githubuser.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import me.fakhry.githubuser.R
import me.fakhry.githubuser.ui.ListUserAdapter
import org.junit.Before
import org.junit.Test

class MainActivityTest {

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun assertGetDetailUserActivity() {
        onView(withId(R.id.rv_list_user)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_list_user)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ListUserAdapter.ViewHolder>(
                0,
                click()
            )
        )
    }
}