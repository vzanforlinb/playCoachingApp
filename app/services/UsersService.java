package services;

import dtos.UserResponseDto;
import models.UserModel;
import play.Logger;
import play.libs.F;
import repositories.UsersRepository;

public class UsersService {

    private UsersRepository usersRepository;

    // TODO: inyecciones
    public UsersService() {
        this.usersRepository = new UsersRepository();
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
