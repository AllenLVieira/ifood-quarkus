package br.com.allen.ifood.registration.resource;

import br.com.allen.ifood.registration.RegistrationTestLifecycleManager;
import br.com.allen.ifood.registration.model.Restaurant;
import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.approvaltests.Approvals;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(RegistrationTestLifecycleManager.class)
@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
class RestaurantResourceTest {

    @Test
    @DataSet("restaurants-first-scenario-1.yml")
    void getAllRestaurants() {
        String result = given()
                .when().get("/restaurants")
                .then()
                .statusCode(200)
                .extract().asString();
        Approvals.verifyJson(result);
    }

    // Corrigir retorno 415
    private RequestSpecification given() {
        return RestAssured.given().contentType(ContentType.JSON);
    }

    @Test
    @DataSet("restaurants-first-scenario-1.yml")
    public void updateRestaurant() {
        Restaurant dto = new Restaurant();
        dto.name = "newName";
        Long parameterValue = 1L;
        String result = given()
                .with().pathParam("restaurantId", parameterValue)
                .body(dto)
                .when().put("/restaurants/{restaurantId}")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode())
                .extract().asString();
        Assert.assertEquals("", result);
        Restaurant findById = Restaurant.findById(parameterValue);
        Assert.assertEquals(dto.name, findById.name);

    }
}