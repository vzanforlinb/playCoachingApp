package services;

import dtos.UserResponseDto;
import models.UserModel;
import play.Logger;
import play.libs.F;
import repositories.UsersRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UsersService {

    private UsersRepository usersRepository;
    private Logger.ALogger logger;

    @Inject
    public UsersService(UsersRepository usersRepository, Logger.ALogger logger) {
        this.usersRepository = usersRepository;
        this.logger = logger;
    }

    public F.Promise<UserModel> getUser(long id) {
        logger.info("getUser init: " + Thread.currentThread().getId());

        return usersRepository.get(id).map(userResponseDto -> {
            logger.info("getUser end: " + Thread.currentThread().getId());
            return mapModel(userResponseDto);
        });
    }

    private UserModel mapModel(UserResponseDto userResponseDto) {
        UserModel userModel = new UserModel();
        userModel.id = userResponseDto.id;
        userModel.siteId = userResponseDto.siteId;
        userModel.name = String.format("%s %s", userResponseDto.firstName, userResponseDto.lastName);
        userModel.firstName = userResponseDto.firstName;
        userModel.lastName = userResponseDto.lastName;
        userModel.nickname = userResponseDto.nickname;
        userModel.permalink = userResponseDto.permalink;
        userModel.userType = userResponseDto.userType;
        return userModel;
    }
}
