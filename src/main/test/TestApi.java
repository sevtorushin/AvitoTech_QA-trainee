import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import pojo.Advert;
import pojo.Statistics;
import util.HttpClientUtil;
import util.Status;

import java.net.http.HttpResponse;
import java.util.List;

public class TestApi {

    private static HttpClientUtil clientUtil;
    private static String host = "https://qa-internship.avito.com";
    private static String advertPath = "/api/1/item";
    private static Integer sellerId = 1111119;
    /**
     * This seller's listing must be empty before running test "CheckAmount"
     */
    private static Integer emptySellerId = 123458;
    private static String getSellerAdvertsPath;

    private static ObjectMapper mapper;
    private static Advert advert;

    @BeforeAll
    static void beforeAll() {
        clientUtil = new HttpClientUtil();
        getSellerAdvertsPath = "/api/1/" + emptySellerId + "/item";
        mapper = new ObjectMapper();
        advert = new Advert("Телефон", 1000, sellerId, new Statistics(0, 0, 0));
    }

    /**
     * Проверка сохранения валидного объявления
     */
    @Test
    @Tag(value = "Create")
    void checkAdvertCreation() {
        String url = host + advertPath;
        try {
            HttpResponse<String> postHttpResponse = clientUtil.doPost(url, mapper.writeValueAsString(advert));
            Assertions.assertEquals(200, postHttpResponse.statusCode());
            Status responseStatus = mapper.readValue(postHttpResponse.body(), Status.class);
            Assertions.assertTrue(responseStatus.getStatus().contains("Сохранили объявление"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Проверка получения существующего объявления по его ID
     */
    @Test
    @Tag(value = "Retrieve")
    void checkRetrievingOfCreatedAdvert() {
        String url = host + advertPath;
        try {
            HttpResponse<String> postHttpResponse = clientUtil.doPost(url, mapper.writeValueAsString(advert));
            Assumptions.assumeTrue(postHttpResponse.statusCode()==200);
            String createdId = mapper.readValue(postHttpResponse.body(), Status.class).getId();
            String getFromIdUrl = host + advertPath + "/" + createdId;
            HttpResponse<String> httpResponse = clientUtil.doGet(getFromIdUrl);
            Assertions.assertEquals(200, httpResponse.statusCode());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Проверка соответствия сохраненного объявления с объектом, который был до отправки
     */
    @Test
    @Tag(value = "Comparison")
    void checkCreatedAdvert() {
        String url = host + advertPath;
        try {
            HttpResponse<String> postHttpResponse = clientUtil.doPost(url, mapper.writeValueAsString(advert));
            Assumptions.assumeTrue(postHttpResponse.statusCode()==200);
            String createdId = mapper.readValue(postHttpResponse.body(), Status.class).getId();
            String getFromIdUrl = host + advertPath + "/" + createdId;
            HttpResponse<String> httpResponse = clientUtil.doGet(getFromIdUrl);
            Assumptions.assumeTrue(httpResponse.statusCode()==200);
            List<Advert> adverts = mapper.readValue(httpResponse.body(), new TypeReference<>() {
            });
            Assertions.assertEquals(advert, adverts.getFirst());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Проверка получения списка сохраненных объявлений для продавца, предварительно не имеющего объявлений
     */
    @Test
    @DisplayName(value = "CheckAmount")
    void checkAmountCreatedAdvertsForSpecifiedSeller() {
        Advert advert1 = new Advert("Телефон", 1000, emptySellerId, new Statistics());
        Advert advert2 = new Advert("Телефон", 5000, emptySellerId, new Statistics());
        String url = host + advertPath;
        try {
            HttpResponse<String> postHttpResponse1 = clientUtil.doPost(url, mapper.writeValueAsString(advert1));
            Assumptions.assumeTrue(postHttpResponse1.statusCode()==200);
            HttpResponse<String> postHttpResponse2 = clientUtil.doPost(url, mapper.writeValueAsString(advert2));
            Assumptions.assumeTrue(postHttpResponse2.statusCode()==200);
            HttpResponse<String> getHttpResponse = clientUtil.doGet(host + getSellerAdvertsPath);
            List<Advert> adverts = mapper.readValue(getHttpResponse.body(), new TypeReference<>() {
            });
            Assertions.assertEquals(2, adverts.size());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Проверка ответа сервера при запросе несуществующего объявления по его ID
     */
    @Test
    @Tag(value = "CheckMissingId")
    void checkMissingId() {
        String advertId = "123";
        String jsonResponseBody = "{\"result\":{\"message\":\"item 123 not found\",\"messages\":null},\"status\":\"404\"}\n";
        String getFromIdUrl = host + advertPath + "/" + advertId;
        HttpResponse<String> httpResponse = clientUtil.doGet(getFromIdUrl);
        Assertions.assertEquals(jsonResponseBody, httpResponse.body());
        Assertions.assertEquals(404, httpResponse.statusCode());
    }
}
