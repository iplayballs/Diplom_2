package data;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import requests.RestApiBuilder;

import java.util.*;

import static config.UrlConstants.GET_INGREDIENTS;
import static io.restassured.RestAssured.given;

public class OrderDataIngredients extends RestApiBuilder {

    private final RestApiBuilder restApiBuilder = new RestApiBuilder();

    private static HashMap<String, String> ban = new HashMap<>();

    private static HashMap<String, String> sauce = new HashMap<>();

    private static HashMap<String, String> filling = new HashMap<>();

    public void fillIngredientsMaps() {

        Response response;
        response = given()
                .spec(restApiBuilder.requestSpecificationWithoutAccessToken())
                .get(GET_INGREDIENTS);
        //создаем массив всех объектов ответа
        List<Map<String, Object>> dataList = response.jsonPath().getList("data");

        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> item = dataList.get(i);
            //формируем массивы каждого по типу ингридиентов
            if (item.toString().toLowerCase().contains("булка")) {
                ban.put((String) item.get("_id"), (String) item.get("name"));
            } else if (item.toString().toLowerCase().contains("соус")) {
                sauce.put((String) item.get("_id"), (String) item.get("name"));
            } else {
                filling.put((String) item.get("_id"), (String) item.get("name"));
            }
        }
    }


    //формируем случайный хеш/id ингридиентов из булка, соус, начинка
    public static List<String> getIngridientsRandomId() {

        List<String> banItem = new ArrayList<>(ban.keySet());
        List<String> sauceItem = new ArrayList<>(sauce.keySet());
        List<String> fillingItem = new ArrayList<>(filling.keySet());

        Random random = new Random();

        int randomBanItem = random.nextInt(banItem.size());
        int randomSauceIndex = random.nextInt(sauceItem.size());
        int randomFillingIndex = random.nextInt(fillingItem.size());

        return Arrays.asList(
                banItem.get(randomBanItem),
                sauceItem.get(randomSauceIndex),
                fillingItem.get(randomFillingIndex)
        );
    }

    //получаем name по хеш/id
    public static List<String> getIngredientsName(List<String> idIngredients) {

        List<String> idIngredientsName = new ArrayList<>();

        for (int i = 0; i < idIngredients.size(); i++) {

            String ingredientId = idIngredients.get(i);

            if (ban.containsKey(ingredientId)) {
                idIngredientsName.add(ban.get(ingredientId));
            } else if (sauce.containsKey(ingredientId)) {
                idIngredientsName.add(sauce.get(ingredientId));
            } else {
                idIngredientsName.add(filling.get(ingredientId));
            }
        }
            return idIngredientsName;
    }
}

