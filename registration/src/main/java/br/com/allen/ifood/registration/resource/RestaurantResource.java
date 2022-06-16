package br.com.allen.ifood.registration.resource;

import br.com.allen.ifood.registration.model.Restaurant;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/restaurants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
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
}