package org.wordpress.android.e2e.pages;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;

import org.wordpress.android.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.wordpress.android.support.WPSupportUtils.clickOn;
import static org.wordpress.android.support.WPSupportUtils.isElementDisplayed;
import static org.wordpress.android.support.WPSupportUtils.populateTextField;
import static org.wordpress.android.support.WPSupportUtils.waitForElementToBeDisplayed;
import static org.wordpress.android.support.WPSupportUtils.withIndex;

public class EditorPage {
    private static ViewInteraction publishButton = onView(withId(R.id.menu_save_post));
    private static ViewInteraction editor = onView(withId(R.id.aztec));
    private static ViewInteraction titleField = onView(allOf(withId(R.id.title),
            withHint("Title")));
    private static ViewInteraction publishConfirmation = onView(allOf(
            withText("Post published"), isDescendantOfA(withId(R.id.snackbar))));
    private static ViewInteraction addMediaButton = onView(withId(R.id.media_button_container));
    private static ViewInteraction allowMediaAccessButton = onView(allOf(withId(R.id.button),
            withText("Allow")));
    private static ViewInteraction confirmButton = onView(withId(R.id.mnu_confirm_selection));

    public EditorPage() {
        editor.check(matches(isDisplayed()));
    }

    public void enterTitle(String postTitle) {
        titleField.perform(typeText(postTitle), ViewActions.closeSoftKeyboard());
    }

    public void enterContent(String postContent) {
        editor.perform(typeText(postContent), ViewActions.closeSoftKeyboard());
    }

    // Image needs a little time to be uploaded after entering the image
    public void enterImage() {
        // Click on add media button
        clickOn(addMediaButton);

        if (isElementDisplayed(allowMediaAccessButton)) {
            // Click on Allow button
            clickOn(allowMediaAccessButton);
        }

        // Click on a random image
        onView(withIndex(withId(R.id.image_thumbnail), 0)).perform(click());

        // Click the confirm button
        clickOn(confirmButton);

        if (isElementDisplayed(onView(withText("LEAVE OFF")))) {
            // Accept alert for media access
            clickOn(onView(withText("LEAVE OFF")).inRoot(isDialog()));
        }
    }

    public void openSettings() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        clickOn(onView(withText("Post settings")));
    }

    public void addACategory(String category) {
        clickOn(onView(withId(R.id.post_categories_container)));
        clickOn(onView(withText(category)));
        pressBack();
    }

    public void addATag(String tag) {
        clickOn(onView(withId(R.id.post_tags_container)));
        ViewInteraction tagsField = onView(withId(R.id.tags_edit_text));
        populateTextField(tagsField, tag);
        pressBack();
    }

    public void setFeaturedImage() {
        clickOn(onView(withId(R.id.post_add_featured_image_button)));
        clickOn(onView(withId(R.id.icon_wpmedia)));
        onView(withIndex(withId(R.id.media_grid_item_image), 0)).perform(click());
    }

    public boolean publishPost() {
        clickOn(publishButton);
        clickOn(onView(withText("PUBLISH NOW")));
        waitForElementToBeDisplayed(publishConfirmation);
        return isElementDisplayed(publishConfirmation);
    }
}
