import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import pojo.Advert;
import pojo.Statistics;
import util.HttpClientUtil;

import java.net.http.HttpResponse;
import java.util.stream.Stream;

public class BugsTest {

    private static HttpClientUtil clientUtil;
    private static String host = "https://qa-internship.avito.com";
    private static String advertPath = "/api/1/item";
    private static Integer sellerId = 1111119;

    private static ObjectMapper mapper;

    @BeforeAll
    static void beforeAll() {
        clientUtil = new HttpClientUtil();
        mapper = new ObjectMapper();
    }

    static Stream<Arguments> methodAdvertProvider() {
        Integer negativeNumber = -1;
        return Stream.of(
                Arguments.arguments(new Advert("", 1000, sellerId, new Statistics(0,0,0))),
                Arguments.arguments(new Advert(" ", 1000, sellerId, new Statistics(0,0,0))),
                Arguments.arguments(new Advert("Телефон", negativeNumber, sellerId, new Statistics(0,0,0))),
                Arguments.arguments(new Advert("Телефон", 1000, negativeNumber, new Statistics(0,0,0))),
                Arguments.arguments(new Advert("Телефон", 1000, sellerId, new Statistics(negativeNumber,0,0))),
                Arguments.arguments(new Advert("Телефон", 1000, sellerId, new Statistics(0,negativeNumber,0))),
                Arguments.arguments(new Advert("Телефон", 1000, sellerId, new Statistics(0,0,negativeNumber))),
                Arguments.arguments(new Advert(null, 1000, sellerId, new Statistics(0,0,0))),
                Arguments.arguments(new Advert("Телефон", null, sellerId, new Statistics(0,0,0))),
                Arguments.arguments(new Advert("Телефон", 1000, null, new Statistics(0,0,0))),
                Arguments.arguments(new Advert("Телефон", 1000, sellerId, new Statistics(null,0,0))),
                Arguments.arguments(new Advert("Телефон", 1000, sellerId, new Statistics(0,null,0))),
                Arguments.arguments(new Advert("Телефон", 1000, sellerId, new Statistics(0,0,null))),
                Arguments.arguments(new Advert("Телефон", 1000, sellerId, null)),
                Arguments.arguments((Object) null)
        );
    }

    /**
     * Тесты с проверкой HTTP ответа сервера при отправке невалидных значений полей объекта
     * @apiNote Тесты проваливаются, так как API некорректно обрабатывает невалидные поля объекта.
     * Ответ сервера на подобную коллекцию тестовых данных должен быть с кодом "400",
     * однако сервер отдает коды "200" и "500". Описание в BUGS.md
     */
    @ParameterizedTest
    @MethodSource("methodAdvertProvider")
    @Tag(value = "SavingInvalidData")
    void checkResponseWithInvalidRequestData(Advert advert) {
        String url = host + advertPath;
        try {
            HttpResponse<String> postHttpResponse = clientUtil.doPost(url, mapper.writeValueAsString(advert));
            Assertions.assertEquals(400, postHttpResponse.statusCode(), () -> "Unexpected response code: " + postHttpResponse.statusCode());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Проверка обработки запроса на выдачу всех объявлений несуществующего ID продавца
     */
    @Test
    @Tag(value = "GetFromInvalidSellerID")
    void checkRetrievingAllAdvertsFromInvalidSellerId() {
        String invalidSellerId = "sfg3nd5f4hn";
        HttpResponse<String> getHttpResponse = clientUtil.doGet(host + "/api/1/" + invalidSellerId + "/item");
        Assertions.assertEquals(400, getHttpResponse.statusCode());
    }

    /**
     * Проверка корректности обработки числовых полей объекта
     */
    @ParameterizedTest
    @Tag(value = "CheckOtherNumber")
    @ValueSource(strings = {"10000000000000000000000", "\"sfg3nd5f4hn\"", "1000.5"})
    void checkNumericData(String inputNumber) {
        String url = host + advertPath;
        String json = "{\n" +
                "    \"name\": \"Телефон\",\n" +
                "    \"price\": " + inputNumber + ",\n" +
                "    \"sellerId\": " + sellerId + ",\n" +
                "    \"statistics\": {\n" +
                "        \"contacts\": 0,\n" +
                "        \"likes\": 0,\n" +
                "        \"viewCount\": 0\n" +
                "    }\n" +
                "}";
        HttpResponse<String> httpResponse = clientUtil.doPost(url, json);
        Assertions.assertEquals(400, httpResponse.statusCode());
    }
}
