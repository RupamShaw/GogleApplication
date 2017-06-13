package com.jagdiv.android.gogleapplication;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FirebaseDataActivityTest2 {

    @Rule
    public ActivityTestRule<FirebaseDataActivity> mActivityTestRule = new ActivityTestRule<>(FirebaseDataActivity.class);

    @Test
    public void firebaseDataActivityTest2() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.msgtxt), withText("e1loJTl7ZM0:APA91bGSBd2-BPn9LrkMEvJMr0bcEWzMb7pg9vQQrx5PKUmpNV-2OB5z57MU8_D0UlMFjBcQli7-ntTaNJbyYeNQL4NOX3BHjBZ0EvwKmU3AOMBxr2eLAqQu4s-L20Rksw55egOGmXtd"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("e1loJTl7ZM0:APA91bGSBd2-BPn9LrkMEvJMr0bcEWzMb7pg9vQQrx5PKUmpNV-2OB5z57MU8_D0UlMFjBcQli7-ntTaNJbyYeNQL4NOX3BHjBZ0EvwKmU3AOMBxr2eLAqQu4s-L20Rksw55egOGmXtd")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
