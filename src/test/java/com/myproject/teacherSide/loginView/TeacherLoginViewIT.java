package com.myproject.teacherSide.loginView;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@UsePlaywright
public class TeacherLoginViewIT {

    Page page;

    @BeforeEach
    public void setup() {
        page = Playwright.create().chromium().launch().newContext().newPage();
        page.setDefaultTimeout(30000);
        page.navigate("http://localhost:8080/teacher/login");
    }

    @Test
    public void initialStateOfTeacherLoginView() throws Exception {
        // Given the user is on the page TeacherLoginView
        // Checked By Executing JS 'Array.from(document.querySelectorAll('vaadin-login-overlay')).length > 0'

        // Then the user should see a login form with role 'form' and the following fields
        Locator form = page.getByRole(AriaRole.FORM);
        PlaywrightAssertions.assertThat(form).isVisible();

        // And the user should see a text field with role 'textbox', label 'Email', and containing 'input' with name 'username'
        Locator emailField = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email"));
        PlaywrightAssertions.assertThat(emailField).isVisible();

        // And the user should see a password field with role 'textbox', label 'Password', and containing 'input' with name 'password'
        Locator passwordField = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password"));
        PlaywrightAssertions.assertThat(passwordField).isVisible();

        // And the user should see a button with role 'button' and label 'Log in'
        Locator loginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log in"));
        PlaywrightAssertions.assertThat(loginButton).isVisible();

        // And the user should see a button with role 'button' and label 'Forgot password'
        Locator forgotPasswordButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Forgot password"));
        PlaywrightAssertions.assertThat(forgotPasswordButton).isVisible();

        // And the user should see a link with tag name 'a', href 'teacher/signup', and text "Don't have an account? Sign up"
        Locator signUpLink = page.locator("a[href='teacher/signup']");
        PlaywrightAssertions.assertThat(signUpLink).isVisible();

        // And the new user should see a button with role 'button' and label 'I am Teacher'
        Locator iAmTeacherButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("I am Teacher"));
        PlaywrightAssertions.assertThat(iAmTeacherButton).isVisible();
    }

    @Test
    public void userClicksOnTheLogInButtonWithEmptyFields() throws Exception {
        // Given the user is on the page TeacherLoginView
        // Checked By Executing JS 'Array.from(document.querySelectorAll('vaadin-login-overlay')).length > 0'

        // When the user clicks on the button with role 'button' and label 'Log in'
        Locator loginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log in"));
        loginButton.click();

        // Then a validation error message should appear
        Locator loginForm = page.locator("vaadin-login-form[invalid]");
        PlaywrightAssertions.assertThat(loginForm).isVisible();
    }

    @Test
    public void userClicksOnTheDontHaveAnAccountSignUpAnchor() throws Exception {
        // Given the user is on the page TeacherLoginView
        // Checked By Executing JS 'Array.from(document.querySelectorAll('vaadin-login-overlay')).length > 0'

        // When the user clicks on the link with tag name 'a' with href 'teacher/signup' and text "Don't have an account? Sign up"
        Locator signUpLink = page.locator("a[href='teacher/signup']");
        signUpLink.click();

        // Then the user should navigate to 'teacher/signup'
        PlaywrightAssertions.assertThat(page).hasURL("http://localhost:8080/teacher/signup");
    }

    @Test
    public void userClicksOnTheIAmTeacherButton() throws Exception {
        // Given the user is on the page TeacherLoginView
        // Checked By Executing JS 'Array.from(document.querySelectorAll('vaadin-login-overlay')).length > 0'

        // When the user clicks on the button with role 'button' and label 'I am Teacher'
        Locator iAmTeacherButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("I am Teacher"));
        iAmTeacherButton.click();

        // Then the user should navigate to 'student/login'
        PlaywrightAssertions.assertThat(page).hasURL("http://localhost:8080/student/login");
    }
}