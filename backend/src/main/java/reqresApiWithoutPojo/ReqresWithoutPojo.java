package reqresApiWithoutPojo;
import Specification.Specifications;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresWithoutPojo {

    @Test
    public void checkAvatarsTest() {
        String URL = "https://reqres.in/";

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec(200));

        Response response = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .body("page", equalTo(2)) // Делаем проверку на то, что поле page имеет значение 2
                .body("data.id", notNullValue()) // Проверка поля на непустоту
                .body("data.email", notNullValue())
                .body("data.first_name", notNullValue())
                .body("data.last_name", notNullValue())
                .body("data.avatar", notNullValue())
                .extract().response();

        // Превращаем ответ response в json
        JsonPath jsonPath = response.jsonPath();

        // Извлекаем из json список с емейлами, список с id, список с avatar
        List<String> emails = jsonPath.get("data.email");
        List<Integer> ids = jsonPath.get("data.id");
        List<String> avatars = jsonPath.get("data.avatar");

        for (int i = 0; i < emails.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i).toString()));
        }

        Assert.assertTrue(emails.stream().allMatch(x -> x.endsWith("@reqres.in")));
    }
}
