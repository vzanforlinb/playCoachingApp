package controllers;

import play.libs.F;
import play.libs.Json;
import play.mvc.*;
import services.ItemsService;

public class ItemsController extends Controller {

    public ItemsService itemsService;

    public F.Promise<Result> get(long id) {

        return itemsService.get(id)
            .map(itemModel -> Results.ok(Json.toJson(itemModel)));
    }

    public F.Promise<Result> post() {
        return F.Promise.pure(Results.ok("Post: It works!"));
    }

    public F.Promise<Result> put(long id) {
        return F.Promise.pure(Results.ok("Put: It works!"));
    }

    public F.Promise<Result> delete(long id) {
        return F.Promise.pure(Results.ok("Delete: It works!"));
    }

}