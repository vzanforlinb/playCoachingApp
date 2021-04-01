package services;

import models.UserModel;
import play.libs.F;

public class UsersService {

    public F.Promise<UserModel> getUser(long id) {
        // TODO: Ir a la API de users y traer la info del user
        return F.Promise.pure(new UserModel(id, "viki"));
    }
}
