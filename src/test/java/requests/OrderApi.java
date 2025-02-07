package requests;

import data.OrderData;
import data.OrderDataIngredients;
import data.UserData;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.BeforeClass;

import static config.UrlConstants.CREATE_ORDER;
import static config.UrlConstants.GET_USER_ORDERS;
import static io.restassured.RestAssured.given;

public class OrderApi extends RestApiBuilder{

    Response response;
    private final RestApiBuilder restApiBuilder = new RestApiBuilder();

    @Step("Создание заказа авторизованным клиентом с передачей accessToken")
    public Response createOrderWithAccessToken(UserData userData, OrderData orderData){

        response =
                given()
                        .spec(restApiBuilder.requestSpecificationWithAccessToken(userData))
                        .body(orderData)
                        .post(CREATE_ORDER);

        return response;
    }

    @Step("Создание заказа неавторизованным клиентом без передачи accessToken")
    public Response createOrderWithoutAccessToken(OrderData orderData){

        response =
                given()
                        .spec(restApiBuilder.requestSpecificationWithoutAccessToken())
                        .body(orderData)
                        .post(CREATE_ORDER);

        return response;
    }

    @Step("Получения списка заказов клиента с авторизацией и передачей accessToken")
    public Response getOrderListWithAccessToken(UserData userData){

        response =
                given()
                        .spec(restApiBuilder.requestSpecificationWithAccessToken(userData))
                        .get(GET_USER_ORDERS);

        return response;
    }

    @Step("Получения списка заказов клиента без авторизации и передачи accessToken")
    public Response getOrderListWithoutAccessToken(){

        response =
                given()
                        .spec(restApiBuilder.requestSpecificationWithoutAccessToken())
                        .get(GET_USER_ORDERS);

        return response;
    }
}
