package controllers;

import play.*;
import play.libs.F;
import play.libs.Json;
import play.mvc.*;
import services.ItemService;

public class Items extends Controller {

    public ItemService itemService;

    public F.Promise<Result> get(Long id) {

        return itemService.get(id)
            .map(itemModel -> Results.ok(Json.toJson(itemModel)));
    }

    public F.Promise<Result> post() {
        return F.Promise.pure(Results.ok("Post: It works!"));
    }

    public F.Promise<Result> put(Long id) {
        return F.Promise.pure(Results.ok("Put: It works!"));
    }

    public F.Promise<Result> delete(Long id) {
        return F.Promise.pure(Results.ok("Delete: It works!"));
    }

}