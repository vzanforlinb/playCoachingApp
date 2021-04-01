package controllers;

import dtos.ItemDto;
import dtos.UserInfoDto;
import models.ItemModel;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.ItemsService;
import services.UsersService;

import java.util.stream.Collectors;

public class UsersController extends Controller {


    public ItemsService itemsService;
    public UsersService usersService;

    // TODO: Implementar injección de dependencias
    public UsersController() {
        this.itemsService = new ItemsService();
        this.usersService = new UsersService();
    }

    public F.Promise<Result> getUserInfo(long userId) {
        // TODO: obtener datos de usuario e items de usuario en forma paralela  (opcional en forma paralela)
        // TODO: devolver un UserInfoDto con la información obtenida
        return usersService.getUser(userId)
            .flatMap(userModel -> itemsService.getUserItems(userId)
                .map(itemModels -> {
                    UserInfoDto userInfoDto = new UserInfoDto();

                    userInfoDto.id = userId;
                    userInfoDto.name = userModel.name;
                    userInfoDto.items = itemModels.stream()
                        .map(itemModel -> itemMapper(itemModel))
                        .collect(Collectors.toList());

                    return userInfoDto;
                }))
            .map(userInfoDto -> Results.ok(Json.toJson(userInfoDto)));
    }

    private ItemDto itemMapper(ItemModel itemModel) {
        ItemDto itemDto = new ItemDto();
        itemDto.id = itemModel.id;
        itemDto.title = itemModel.title;

        return itemDto;
    }
}
