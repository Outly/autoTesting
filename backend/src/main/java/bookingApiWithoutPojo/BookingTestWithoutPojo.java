package bookingApiWithoutPojo;
import Specification.Specifications;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BookingTestWithoutPojo {

    private final static String URL = "https://restful-booker.herokuapp.com/";

    @Test
    public void createBookingAndGetByIdTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec(200));

        Map<String, String> dates = new HashMap<>();
        dates.put("checkin", "2018-01-01");
        dates.put("checkout", "2019-01-01");

        Map<String, Object> booking = new HashMap<>();
        booking.put("firstname", "Jim");
        booking.put("lastname", "Brown");
        booking.put("totalprice", 111);
        booking.put("depositpaid", true);
        booking.put("bookingdates", dates);
        booking.put("additionalneeds", "Breakfast");

        Response responsePost = given()
                .body(booking)
                .when()
                .post("booking")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = responsePost.jsonPath();
        Integer id = jsonPath.get("bookingid");
        System.out.println(id);

        Response responseGet = given()
                .when()
                .get("booking/" +  id.toString())
                .then().log().all()
                .extract().response();

        JsonPath jsonPath1 = responseGet.jsonPath();

        Map<String, Object> resp1 = jsonPath.get("booking");
        Map<String, Object> resp2 = jsonPath1.get("$");

        System.out.println(resp1);
        System.out.println(resp2);

        Assert.assertEquals(resp1, resp2);
    }
}
