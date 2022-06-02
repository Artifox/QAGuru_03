import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;

public class FormFillingTests {

    @BeforeAll
    static void beforeAll() {
        Configuration.baseUrl = "https://demoqa.com";
        Configuration.browserSize = "1920x1080";
        Configuration.browserPosition = "0x0";
        Configuration.holdBrowserOpen = true;
    }

    @Test
    public void fillFormTest() {
        String firstName = "Mr",
                lastName = "Smith",
                email = "mr-smith@mail.gl",
                gender = "Male",
                phoneNumber = "2835541120",
                birthMonth = "October",
                birthYear = "1993",
                birthDate = "23",
                subject = "Maths",
                hobby = "Sports",
                pictureClassPath = "Screenshot_94.png",
                currentAddress = "Current address",
                state = "Haryana",
                city = "Karnal";

        open("/automation-practice-form");
        //check page is opened
        $(".practice-form-wrapper").shouldHave(text("Student Registration Form"));
        executeJavaScript("$('footer').remove()");
        executeJavaScript("$('#fixedban').remove()");

        $("#firstName").setValue(firstName);
        $("#lastName").setValue(lastName);
        $("#userEmail").setValue(email);
        $("#genterWrapper").$(byText(gender)).click(); //wrong: $(byText(gender)).click() or $("input#gender-radio-1+label").click();
        $("#userNumber").setValue(phoneNumber);
        $("#dateOfBirthInput").click();
        $(".react-datepicker__month-select").selectOption(birthMonth);
        $(".react-datepicker__year-select").selectOption(birthYear);
        //TODO: check how to fill a calendar properly
        $(".react-datepicker__day--0" + birthDate +":not(.react-datepicker__day--outside-month)").click();// wrong: $("div[aria-label*='October 23rd']").click();
        $("#subjectsInput").sendKeys(subject);      //sendKeys() is used as workaround for this buggy form
        $("#subjectsInput").sendKeys(Keys.TAB);     //It's better to use sendValue()
        //TODO: It's better to find radio buttons via text
        $("#hobbiesWrapper").$(byText("Sports")).click(); //wrong:$(byText(hobby)).click() or $("input#hobbies-checkbox-1+label").click();
        $("#uploadPicture").uploadFromClasspath(pictureClassPath);
        $("#currentAddress").setValue(currentAddress);
        $("#state").click();
        $(byText(state)).click();
        $("#city").click();
        $(byText(city)).click();
        $("#submit").click();

        //check that page was opened
        $("#example-modal-sizes-title-lg").shouldHave(Condition.exactText("Thanks for submitting the form"));

        //write each assertion from new line
        /*$(".table-responsive").$(byText("Student Name")).parent()
                .shouldHave(Condition.text(firstName + " " + lastName));*/

        checkTable("Student Name", firstName + " " + lastName);
        checkTable("Student Email", email);
        checkTable("Gender", gender);
        checkTable("Mobile", phoneNumber);
        checkTable("Date of Birth", birthDate + " " + birthMonth + "," + birthYear);
        checkTable("Subjects", subject);
        checkTable("Hobbies", hobby);
        checkTable("Picture", pictureClassPath);
        checkTable("Address", currentAddress);
        checkTable("State and City", state + " " + city);

        $("#closeLargeModal").click();
    }

    private void checkTable(String key, String value) {
        $(".table-responsive").$(byText(key))
                .parent().shouldHave(text(value));
    }
}
