package controllers;

import play.libs.F;
import play.libs.Json;
import play.mvc.*;
import repositories.ItemsRepository;
import services.ItemsService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemsController extends Controller {

    private ItemsService itemsService;
    private ItemsRepository itemsRepository;

    @Inject
    public ItemsController(ItemsService itemsService, ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
        this.itemsService = itemsService;
    }

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

    public F.Promise<Result> test() {
        return itemsRepository.createTable()
            .flatMap(aVoid -> itemsService.createItem()
                .flatMap(itemId -> itemsService.getItem(itemId)
                    .map(itemModel -> Results.ok(Json.toJson(itemModel))))
            );
    }

}