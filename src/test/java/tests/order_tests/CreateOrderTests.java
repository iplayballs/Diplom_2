package tests.order_tests;

import config.BaseTestAfterDeleteUser;
import data.OrderData;
import data.OrderDataIngredients;
import data.UserData;
import data.UserDataGenerate;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import requests.OrderApi;
import requests.UserApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class CreateOrderTests extends BaseTestAfterDeleteUser {

    static OrderDataIngredients orderDataIngredients = new OrderDataIngredients();

    @BeforeClass
    public static void createIngredientsList(){
        orderDataIngredients.fillIngredientsMaps();
    }

    @DisplayName("Создание заказа с валидными ингридиентами и авторизацией")
    @Description("Проверяем код ответа создания заказа 200 и содержание body ответа")
    @Test
    public void testOrderWithAccessToken(){

        userData = new UserData();
        OrderData orderData = new OrderData();

        userData.setName(UserDataGenerate.generateName());
        userData.setEmail(UserDataGenerate.generateEmail());
        userData.setPassword(UserDataGenerate.generatePassword());
        orderData.setIngredients(OrderDataIngredients.getIngridientsRandomId());

        userApi.registerUser(userData);

        Response response = orderApi.createOrderWithAccessToken(userData, orderData);

        assertThat("Код ответа должен быть 200, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_OK));
        assertThat("В списке ингридиентов должно быть " + orderData.getIngredients() +  ", пришло " + response.jsonPath().getList("order.ingredients").size(), response.jsonPath().getList("order.ingredients").size(), equalTo(orderData.getIngredients().size()));
        assertThat("Названия ингридиентов в ответе совпадают с названиями ингридиенто в запросе", response.jsonPath().getList("order.ingredients.name"), equalTo(OrderDataIngredients.getIngredientsName(orderData.getIngredients())));
        assertThat("Название заказа не должно быть пустым", response.jsonPath().getString("order.name"), notNullValue());
        assertThat("Имя пользователя должно быть, " + userData.getName() + "пришло " + response.jsonPath().getString("order.owner.name"), response.jsonPath().getString("order.owner.name"), equalTo(userData.getName()));
        assertThat("Email пользователя должен быть, " + userData.getEmail().toLowerCase() + "пришёл " + response.jsonPath().getString("order.owner.email"), response.jsonPath().getString("order.owner.email"), equalTo(userData.getEmail().toLowerCase(Locale.ROOT)));
    }

    @DisplayName("Создание заказа с валидными ингридиентами без авторизации")
    @Description("Проверяем код ответа создания заказа 401 и содержание body ответа \"message\": \"You should be authorised\"")
    @Test
    public void testOrderWithoutAccessToken(){

        OrderData orderData = new OrderData();

        orderData.setIngredients(OrderDataIngredients.getIngridientsRandomId());

        Response response = orderApi.createOrderWithoutAccessToken(orderData);

        assertThat("Код ответа должен быть 401, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat("В теле ответа должно прийти \"message\": \"You should be authorised\", пришло \"message\": " + response.jsonPath().getString("message"), response.jsonPath().getString("message"), equalTo("You should be authorised"));
    }

    @DisplayName("Создание заказа без ингридиентов и авторизацией")
    @Description("Проверяем код ответа создания заказа 400 и содержание body ответа  \"message\": \"Ingredient ids must be provided\"\"")
    @Test
    public void testOrderWithAccessTokenAndNullIngredients(){

        userData = new UserData();
        OrderData orderData = new OrderData();

        userData.setName(UserDataGenerate.generateName());
        userData.setEmail(UserDataGenerate.generateEmail());
        userData.setPassword(UserDataGenerate.generatePassword());

        userApi.registerUser(userData);

        Response response = orderApi.createOrderWithAccessToken(userData, orderData);

        assertThat("Код ответа должен быть 400, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat("В теле ответа должно прийти \"message\": \"Ingredient ids must be provided\", пришло \"message\": " + response.jsonPath().getString("message"), response.jsonPath().getString("message"), equalTo("Ingredient ids must be provided"));
    }

    @DisplayName("Создание заказа без ингридиентов и авторизацией")
    @Description("Проверяем код ответа создания заказа 500")
    @Test
    public void testOrderWithAccessTokenAndFakeIngredients(){

        userData = new UserData();
        OrderData orderData = new OrderData();

        userData.setName(UserDataGenerate.generateName());
        userData.setEmail(UserDataGenerate.generateEmail());
        userData.setPassword(UserDataGenerate.generatePassword());

        List<String> ingredientsList = new ArrayList<>();
        ingredientsList.add("0000");
        orderData.setIngredients(ingredientsList);

        userApi.registerUser(userData);

        Response response = orderApi.createOrderWithAccessToken(userData, orderData);

        assertThat("Код ответа должен быть 500, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_INTERNAL_SERVER_ERROR));
    }
}
