package reqresApiWithPojo;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.stream.Collectors;
import static io.restassured.RestAssured.given;

public class ReqresTest {
    private final static String URL = "https://reqres.in/";

    @Test
    public void checkAvatarAndIdTest() {
        List<UserData> users = given()
                .when()
                .contentType(ContentType.JSON)
                .get(URL + "reqresApiWithPojo/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);
        // Для каждого юзера проверяем, что avatar содержит id
        users.stream().forEach(x-> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));

        // Проверяем, что для каждого пользователя e-mail оканчивается на @reqres.in
        Assert.assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));

        // Альтернативнй способ проверить avatar
        // Помещаем в список все аватары
        List<String> avatars = users.stream().map(UserData::getAvatar).collect(Collectors.toList());
        // Помещаем в список все id
        List<String> ids = users.stream().map(x->x.getId().toString()).collect(Collectors.toList());

        // Пробегаем по элементам двух списков, проверяя вхождение в аватар строки id
        for (int i = 0; i < avatars.size(); i++) {
            Assert.assertTrue(avatars.get(i).contains(ids.get(i)));
        }
    }
}
