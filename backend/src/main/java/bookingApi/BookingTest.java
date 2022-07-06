package bookingApi;
import Specification.Specifications;
import org.junit.Assert;
import org.junit.Test;
import static io.restassured.RestAssured.given;

public class BookingTest {

    private final static String URL = "https://restful-booker.herokuapp.com/";

    @Test
    public void createBookingAndGetByIdTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec(200));

        BookingDates bookingDates = new BookingDates("2018-01-01", "2019-01-01");

        RequestBooking requestBooking = new RequestBooking(
                "Jim",
                "Brown",
                111,
                true,
                bookingDates,
                "Breakfast");

        ResponseBooking responseBooking = given()
                .body(requestBooking)
                .when()
                .post("booking")
                .then().log().all()
                .extract().as(ResponseBooking.class);

        System.out.println(responseBooking);

        Integer id = responseBooking.getBookingid();
        System.out.println(id);

        RequestBooking bookingById = given()
                .when()
                .get("booking/" +  id.toString())
                .then().log().all()
                .extract().as(RequestBooking.class);

        System.out.println(bookingById);

        Assert.assertEquals(requestBooking, bookingById);

    }
}
