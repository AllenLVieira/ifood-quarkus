package br.com.allen.ifood.registration.resource;

import br.com.allen.ifood.registration.model.Food;
import br.com.allen.ifood.registration.model.Restaurant;
import org.eclipse.microprofile.openapi.annotations.tags.Tags;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/restaurants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tags(refs = {"restaurant"})
public class RestaurantResource {

    @GET
    public List<Restaurant> getAllRestaurants() {
        return Restaurant.listAll();
    }

    @POST
    @Transactional
    public Response addNewRestaurant(Restaurant restaurant) {
        restaurant.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void updateRestaurant(@PathParam("id") Long id, Restaurant restaurant) {
        Optional<Restaurant> actualRestaurant = Restaurant.findByIdOptional(id);
        if (actualRestaurant.isEmpty()) {
            throw new NotFoundException();
        }
        Restaurant updatedRestaurant = actualRestaurant.get();
        updatedRestaurant.name = restaurant.name;
        updatedRestaurant.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void deleteRestaurant(@PathParam("id") Long id) {
        Optional<Restaurant> actualRestaurant = Restaurant.findByIdOptional(id);
        actualRestaurant.ifPresentOrElse(Restaurant::delete, () -> {
            throw new NotFoundException();
        });
    }

    @GET
    @Path("{restaurantId}/foods")
    @Tags(refs = {"food"})
    public List<Restaurant> getFoodsByRestaurantId(@PathParam("restaurantId") Long restaurantId) {
        Optional<Restaurant> actualRestaurant = Restaurant.findByIdOptional(restaurantId);
        if (actualRestaurant.isEmpty()) {
            throw new NotFoundException(String.format("Restaurante de código %s não existe.", restaurantId));
        }
        return Food.list("restaurant", actualRestaurant.get());
    }

    @POST
    @Path("{restaurantId}/foods")
    @Tags(refs = {"food"})
    @Transactional
    public Response addNewFood(@PathParam("restaurantId") Long restaurantId, Food food) {
        Optional<Restaurant> actualRestaurant = Restaurant.findByIdOptional(restaurantId);
        if (actualRestaurant.isEmpty()) {
            throw new NotFoundException(String.format("Restaurante de código %s não existe.", restaurantId));
        }
        Food newFood = new Food();
        newFood.name = food.name;
        newFood.description = food.description;
        newFood.price = food.price;
        newFood.restaurant = actualRestaurant.get();
        newFood.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{restaurantId}/foods/{foodId}")
    @Tags(refs = {"food"})
    @Transactional
    public void updateFood(@PathParam("restaurantId") Long restaurantId,
                           @PathParam("foodId") Long foodId,
                           Food food) {
        Optional<Restaurant> actualRestaurant = Restaurant.findByIdOptional(restaurantId);
        if (actualRestaurant.isEmpty()) {
            throw new NotFoundException(String.format("Restaurante de código %s não existe.", restaurantId));
        }
        Optional<Food> actualFood = Food.findByIdOptional(foodId);
        if (actualFood.isEmpty()) {
            throw new NotFoundException(String.format("Prato de código %s não existe.", foodId));
        }
        Food newFood = actualFood.get();
        newFood.price = food.price;
        newFood.persist();
    }

    @DELETE
    @Path("{restaurantId}/foods/{foodId}")
    @Tags(refs = {"food"})
    @Transactional
    public void deleteFood(@PathParam("restaurantId") Long restaurantId,
                           @PathParam("foodId") Long foodId) {
        Optional<Restaurant> actualRestaurant = Restaurant.findByIdOptional(restaurantId);
        if (actualRestaurant.isEmpty()) {
            throw new NotFoundException(String.format("Restaurante de código %s não existe.", restaurantId));
        }
        Optional<Food> actualFood = Food.findByIdOptional(foodId);
        actualFood.ifPresentOrElse(Food::delete, () -> {
            throw new NotFoundException(String.format("Prato de código %s não existe.", foodId));
        });
    }
}