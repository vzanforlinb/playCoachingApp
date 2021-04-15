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

    @Inject
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public F.Promise<UserModel> getUser(long id) {
        Logger.info("getUser init: " + Thread.currentThread().getId());

        return usersRepository.get(id).map(userResponseDto -> {
            Logger.info("getUser end: " + Thread.currentThread().getId());
            return mapModel(userResponseDto);
        });
    }

    private UserModel mapModel(UserResponseDto userResponseDto) {
        UserModel userModel = new UserModel();
        userModel.id = userResponseDto.id;
        userModel.name = String.format("%s %s", userResponseDto.firstName, userResponseDto.lastName);
        return userModel;
    }
}
