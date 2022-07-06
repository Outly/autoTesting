package reqresApiWithoutPojo;
import Specification.Specifications;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresWithoutPojo {
    String URL = "https://reqres.in/";

    @Test
    public void checkAvatarsTest() {

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

    @Test
    public void successRegWithoutPojoTest() {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec(200));

        // Создаём словарь для переноса в него данных из json
        Map<String, String> user = new HashMap<>();
        user.put("email", "eve.holt@reqres.in");
        user.put("password", "pistol");

        // Создание POST запроса без создания экземпляра класса Response
        given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .body("id", equalTo(4))
                .body("token", equalTo("QpwL5tke4Pnpja7X4"));

        // Делаем тоже самое, но с созданием response
        Response response = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        Assert.assertEquals(Optional.of(4),Optional.of(jsonPath.get("id")));
        Assert.assertEquals("QpwL5tke4Pnpja7X4", jsonPath.get("token"));
    }

    @Test
    public void unSuccessRegWithoutPojoTest() {

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec(400));

        Map<String, String> userLoozer = new HashMap<>();
        userLoozer.put("email", "sydney@fife");

        Response response = given()
                .body(userLoozer)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().response();

        JsonPath jsonPath = response.jsonPath();

        Assert.assertEquals(jsonPath.get("error"), "Missing password");
    }
}
