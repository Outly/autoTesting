package reqresApiWithPojo;
import Specification.Specifications;
import org.junit.Assert;
import org.junit.Test;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static io.restassured.RestAssured.given;

public class ReqresTestWithSpec {
    private final static String URL = "https://reqres.in/";

    @Test
    public void checkAvatarAndIdTest() {

        // Устанавливаем спецификацию
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec(200));

        // Создаём список для получения в него ответа от сервера
        List<UserData> users = given()
                .when()
                .get("api/users?page=2") // Используем метод GET
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

    @Test
    public void successRegTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec(200));

        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        SuccessReg expected = new SuccessReg(id, token);

        Register user = new Register("eve.holt@reqres.in", "pistol");

        // Создаём экземпляр класса successReg для получения в него ответа от сервера
        SuccessReg successReg = given()
                .body(user) // Передаём класс в теле запроса POST
                .when()
                .post("api/register") // Передаём POST запрос
                .then().log().all()
                .extract().as(SuccessReg.class); // Результат запроса передаём в класс SuccessReg

        // Проверки на непустоту ответа
        Assert.assertNotNull(successReg.getId());
        Assert.assertNotNull(successReg.getToken());

        // Сравниваем полученные данные с заготовкой (необходимо переопределить equals и hashcode)
        Assert.assertTrue(expected.equals(successReg));

        // Альтернативная проверка полученных данных
        Assert.assertEquals(Optional.of(id), Optional.of(successReg.getId())); // Почему-то не работает без optional
        Assert.assertEquals(token, successReg.getToken());
    }

    @Test
    public void unSuccessRegTest() {
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec(400));

        String expectedPassword = "Missing password";

        // Ожидаем ошибку регистрации с кодом 400 с отсылкой кустой строки пароля
        Register userLooser = new Register("sydney@fife", "");

        UnSuccessReg serverAnswer = given()
                .body(userLooser)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(UnSuccessReg.class);

        Assert.assertEquals(expectedPassword, serverAnswer.getError());
    }

    @Test
    public void sortColorsTest() {
        // Данный тест проверяет, что ответ от сервера выдаёт сущности в отсортированном по годам порядке

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec(200));

        List<ColorsData> colorsDataList = given()
                .when()
                .get("api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", ColorsData.class);

        List<Integer> years = new ArrayList<>();
        for (ColorsData currentColor: colorsDataList) {
            years.add(currentColor.getYear());
        }

        // Альтернативный способ извлечения списка с годами из коллекции
        List<Integer> years1 = colorsDataList.stream().map(ColorsData::getYear).collect(Collectors.toList());

        System.out.println(years);

        List<Integer> yearsSort = years.stream().sorted().collect(Collectors.toList());

        System.out.println(yearsSort);

        Assert.assertEquals(yearsSort, years);
    }

    @Test
    public void deleteUserTest() {
        // Данный тест отправляет DELETE запрос, проверяет соответствие ожидаемого статуса 204

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec(204));

        given()
                .when()
                .delete("api/users/2")
                .then().log().all();
    }

    @Test
    public void updateUserAndCheckDateTest() {
        // Тест посылает PUT запрос на обновление пользователя, проверяя дату с сервера с текущей датой на компе

        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec(200));

        UserForUpdate user = new UserForUpdate("morpheus", "zion resident");

        UserForUpdateWithDate userWithDate = given()
                .body(user)
                .when()
                .put("api/users/2")
                .then().log().all()
                .extract().as(UserForUpdateWithDate.class);

        String serverTime = userWithDate.getUpdatedAt();
        serverTime = serverTime.substring(0, serverTime.length() - 8);
        System.out.println(serverTime);

        String machineTime = Clock.systemUTC().instant().toString();
        machineTime = machineTime.substring(0, machineTime.length() - 8);
        System.out.println(machineTime);

        Assert.assertEquals(machineTime, serverTime);
    }
}
