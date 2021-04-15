package controllers;

import akka.dispatch.ExecutionContexts;
import dtos.ItemDto;
import dtos.UserInfoDto;
import mappers.ItemMapper;
import models.CategoryModel;
import models.ItemModel;
import models.UserModel;
import play.Logger;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import repositories.ItemsRepository;
import repositories.UsersRepository;
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
    private UsersRepository usersRepository;
    private ItemsRepository itemsRepository;
    private ItemMapper itemMapper;
  //  private Logger.ALogger logger;

    @Inject
    public UsersController(ItemsService itemsService,
                           UsersService usersService,
                           CategoriesService categoriesService,
                           UsersRepository usersRepository,
                           ItemsRepository itemsRepository,
                           ItemMapper itemMapper//,
                          // Logger.ALogger logger
    ) {

        this.itemsService = itemsService;
        this.usersService = usersService;
        this.categoriesService = categoriesService;
        this.usersRepository = usersRepository;
        this.itemsRepository = itemsRepository;
        this.itemMapper = itemMapper;
       // this.logger = logger;
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

    public F.Promise<Result> testParallel(String siteId, long userId) {
     //   logger.info("testParallel init: " + Thread.currentThread().getId());
        Logger.info("testParallel init: " + Thread.currentThread().getId());

        F.Promise<List<CategoryModel>> siteCategoriesPromise = categoriesService.getSiteCategories(siteId);
        F.Promise<UserModel> userPromise = usersService.getUser(userId);
        ExecutorService computationExecutorService = Executors.newFixedThreadPool(1); // thread pool = ExecutorService
        ExecutorService ioExecutorService = Executors.newFixedThreadPool(9999);
        ExecutionContext executionContext = ExecutionContexts.fromExecutorService(computationExecutorService); // ExecutionContext = ExecutorService
        return F.Promise.sequence(Arrays.asList(siteCategoriesPromise, userPromise), executionContext)
            .map(objects -> {
                List<CategoryModel> siteCategories = (List<CategoryModel>) objects.get(0);
                UserModel user = (UserModel) objects.get(1);
              //  logger.info("testParallel end: " + Thread.currentThread().getId());
                Logger.info("testParallel end: " + Thread.currentThread().getId());

                return Results.ok("");
            });
    }

    private ItemDto itemMapper2(ItemModel itemModel) {
        ItemDto itemDto = new ItemDto();
        itemDto.id = String.valueOf(itemModel.id);
        itemDto.title = itemModel.title;

        return itemDto;
    }
}
