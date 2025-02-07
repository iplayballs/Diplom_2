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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class GetOrdersUserTests extends BaseTestAfterDeleteUser {

    private static final int checkMaxOrdersPerPage = 50;
    private static final int madeOrders = 51;

    static OrderDataIngredients orderDataIngredients = new OrderDataIngredients();

    @BeforeClass
    public static void createIngredientsList(){
        orderDataIngredients.fillIngredientsMaps();
    }

    @DisplayName("Получение списка заказов клиента с авторизацией")
    @Description("Проверяем код ответа создания заказа 200 и содержание body ответа количество заказов в списке не более" + checkMaxOrdersPerPage + " и total/totalToday совпадают со сделанным количеством заказов пользователем")
    @Test
    public void testGetOrderListWithAccessToken() {

        userData = new UserData();
        userApi = new UserApi();
        OrderApi orderApi = new OrderApi();
        OrderData orderData = new OrderData();

        userData.setName(UserDataGenerate.generateName());
        userData.setEmail(UserDataGenerate.generateEmail());
        userData.setPassword(UserDataGenerate.generatePassword());
        orderData.setIngredients(OrderDataIngredients.getIngridientsRandomId());

        userApi.registerUser(userData);
        for (int i = 0; i < madeOrders; i++) {
            orderApi.createOrderWithAccessToken(userData, orderData);
        }

        Response response = orderApi.getOrderListWithAccessToken(userData);

        assertThat("Код ответа должен быть 200, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_OK));
        assertThat("Число заказов total должно быть " + madeOrders + " пришло " + response.jsonPath().getInt("total"), response.jsonPath().getInt("total"), equalTo(madeOrders));
        assertThat("Число заказов totalToday должно быть " + madeOrders + " пришло " + response.jsonPath().getInt("totalToday"), response.jsonPath().getInt("totalToday"), equalTo(madeOrders));
        assertThat("Список orders должен соджержать " + checkMaxOrdersPerPage + " объектов, пришло " + response.jsonPath().getList("orders").size(), response.jsonPath().getList("orders").size(), equalTo(checkMaxOrdersPerPage));
    }

    @DisplayName("Получение списка заказов клиента без авторизацией")
    @Description("Проверяем код ответа создания заказа 401 и содержание body ответа \"message\": \"You should be authorised\"")
    @Test
    public void testGetOrderListWithoutAccessToken() {

        OrderApi orderApi = new OrderApi();

        Response response = orderApi.getOrderListWithoutAccessToken();

        assertThat("Код ответа должен быть 401, пришел " + response.getStatusCode(), response.getStatusCode(), equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat("В теле ответа должно прийти \"message\": \"You should be authorised\", пришло \"message\": " + response.jsonPath().getString("message"), response.jsonPath().getString("message"), equalTo("You should be authorised"));
    }
}


