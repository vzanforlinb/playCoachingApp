package controllers;

import akka.dispatch.ExecutionContexts;
import dtos.ItemDto;
import dtos.UserInfoDto;
import models.CategoryModel;
import models.ItemRModel;
import models.UserCategoriesModel;
import models.UserInfoModel;
import models.UserModel;
import play.Logger;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import scala.concurrent.ExecutionContext;
import services.CategoriesService;
import services.ItemsService;
import services.UsersService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Singleton
public class UsersController extends Controller {

    private ItemsService itemsService;
    private UsersService usersService;
    private CategoriesService categoriesService;
    private Logger.ALogger logger;

    @Inject
    public UsersController(ItemsService itemsService,
                           UsersService usersService,
                           CategoriesService categoriesService,
                           Logger.ALogger logger) {
        this.itemsService = itemsService;
        this.usersService = usersService;
        this.categoriesService = categoriesService;

        this.logger = logger;
    }

    public F.Promise<Result> getUserInfo(long userId) {
        return usersService.getUser(userId)
            .flatMap(userModel -> itemsService
                .getUserItems(userId, userModel.siteId)
                .map(itemModelList -> {
                    UserInfoModel userInfoModel = new UserInfoModel();
                    userInfoModel.id = userModel.id;
                    userInfoModel.firstName = userModel.firstName;
                    userInfoModel.lastName = userModel.lastName;
                    userInfoModel.nickname = userModel.nickname;
                    userInfoModel.permalink = userModel.permalink;
                    userInfoModel.userType = userModel.userType;
                    userInfoModel.items = itemModelList;
                    return userInfoModel;
                }))
            .map(userInfoDto -> Results.ok(Json.toJson(userInfoDto)));
    }


    public F.Promise<Result> getUserInfo2(long userId) {
        return usersService.getUser(userId)
            .flatMap(userModel -> itemsService.getUserItems(userId, "MLA")
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

    public F.Promise<Result> testParallel(String siteId, long userId) {
        logger.info("testParallel init: " + Thread.currentThread().getId());

        F.Promise<List<CategoryModel>> siteCategoriesPromise = categoriesService.getSiteCategories(siteId);
        F.Promise<UserModel> userPromise = usersService.getUser(userId);
        ExecutorService computationExecutorService = Executors.newFixedThreadPool(1); // thread pool = ExecutorService        ExecutorService executor = Executors.newFixedThreadPool(10); -> creates a thread pool with 10 threads: https://howtodoinjava.com/java/multi-threading/executor-service-example/
        ExecutorService ioExecutorService = Executors.newFixedThreadPool(9999);
        ExecutionContext executionContext = ExecutionContexts.fromExecutorService(computationExecutorService); // ExecutionContext = ExecutorService


        return F.Promise.sequence(Arrays.asList(siteCategoriesPromise, userPromise), executionContext)
            .map(objects -> {
                List<CategoryModel> siteCategories = (List<CategoryModel>) objects.get(0);
                UserModel user = (UserModel) objects.get(1);

                UserCategoriesModel userCategoriesModel = new UserCategoriesModel();
                userCategoriesModel.id = user.id;
                userCategoriesModel.name = user.name;
                userCategoriesModel.nickname = user.nickname;
                userCategoriesModel.permalink = user.permalink;
                userCategoriesModel.userType = user.userType;
                userCategoriesModel.siteCategories = siteCategories;

                logger.info("testParallel end: " + Thread.currentThread().getId());

                return Results.ok(Json.toJson(userCategoriesModel));
            });
    }

    private ItemDto itemMapper2(ItemRModel itemModel) {
        ItemDto itemDto = new ItemDto();
        itemDto.id = String.valueOf(itemModel.id);
        itemDto.title = itemModel.title;

        return itemDto;
    }
}
