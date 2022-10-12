import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

class AuthTest {

    @BeforeEach
    public void setup() {
        open("http://localhost:9999/");
    }

    @Test
    public void shouldAuthActiveUser() {
        var registeredUser = UserRegistration.getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[id='root']").shouldHave(Condition.text("Личный кабинет"));
    }

    @Test
    public void shouldNotAuthIfNotRegisteredUser() {
        var notRegisteredUser = UserRegistration.getUser("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotAuthIfBlockedUser() {
        var blockedUser = UserRegistration.getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));

    }

    @Test
    void shouldNotAuthIfWrongLogin() {
        var registeredUser = UserRegistration.getRegisteredUser("active");
        var wrongLogin = UserRegistration.generateLogin();
        $("[data-test-id='login'] input").setValue((wrongLogin));
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldNotAuthIfWrongPassword() {
        var registeredUser = UserRegistration.getRegisteredUser("active");
        var wrongPassword = UserRegistration.generatePassword();
        $("[data-test-id='login'] input").setValue((registeredUser.getLogin()));
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("[data-test-id='action-login']").click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldWarningIfEmptyPassword() {
        var registeredUser = UserRegistration.getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue((registeredUser.getLogin()));
        $("[data-test-id='password'] input").setValue("");
        $("[data-test-id='action-login']").click();
        $("[data-test-id='password'] .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldWarningIfEmptyLogin() {
        var registeredUser = UserRegistration.getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue((""));
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id='login'] .input__sub").shouldHave(Condition.text("Поле обязательно для заполнения"));
    }
}