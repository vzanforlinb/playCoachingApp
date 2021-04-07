package controllers;

import dtos.ItemDto;
import dtos.UserInfoDto;
import mappers.ItemMapper;
import models.ItemModel;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import repositories.ItemsRepository;
import repositories.UsersRepository;
import services.ItemsService;
import services.UsersService;

import java.util.stream.Collectors;

public class UsersController extends Controller {


    public ItemsService itemsService;
    public UsersService usersService;
    public UsersRepository usersRepository;
    public ItemsRepository itemsRepository;
    public ItemMapper itemMapper;

    // TODO: Implementar injección de dependencias
    public UsersController() {
        this.itemsService = new ItemsService();
        this.usersService = new UsersService();
        this.usersRepository = new UsersRepository();
        this.itemsRepository = new ItemsRepository();
        this.itemMapper = new ItemMapper();
    }
    // TODO: agregar service (model)
    public F.Promise<Result> getUserInfo(long userId) {
        return usersRepository.get(userId)
            .flatMap(userResponseDto -> itemsRepository
                .get(userId, userResponseDto.siteId)
                .map(itemsResponseDto -> {
                    UserInfoDto userInfoDto = new UserInfoDto();
                    userInfoDto.id = userResponseDto.id;
                    userInfoDto.firstName = userResponseDto.firstName;
                    userInfoDto.lastName = userResponseDto.lastName;
                    userInfoDto.nickname = userResponseDto.nickname;
                    userInfoDto.permalink = userResponseDto.permalink;
                    userInfoDto.userType = userResponseDto.userType;
                    userInfoDto.items = itemsResponseDto.results.stream()
                        .map(itemResponseDto -> itemMapper.buildFromDto(itemResponseDto))
                        .collect(Collectors.toList());

                    return userInfoDto;
                }))
            .map(userInfoDto -> Results.ok(Json.toJson(userInfoDto)));
    }


    public F.Promise<Result> getUserInfo2(long userId) {
        // TODO: obtener datos de usuario e items de usuario en forma paralela  (opcional en forma paralela)
        // TODO: devolver un UserInfoDto con la información obtenida
        return usersService.getUser(userId)
            .flatMap(userModel -> itemsService.getUserItems(userId)
                .map(itemModels -> {
                    UserInfoDto userInfoDto = new UserInfoDto();

                    userInfoDto.id = userId;
                    userInfoDto.name = userModel.name;
                    userInfoDto.items = itemModels.stream()
                        .map(itemModel -> itemMapper2(itemModel))
                        .collect(Collectors.toList());

                    return userInfoDto;
                }))
            .map(userInfoDto -> Results.ok(Json.toJson(userInfoDto)));
    }

    private ItemDto itemMapper2(ItemModel itemModel) {
        ItemDto itemDto = new ItemDto();
        itemDto.id = String.valueOf(itemModel.id);
        itemDto.title = itemModel.title;

        return itemDto;
    }
}
