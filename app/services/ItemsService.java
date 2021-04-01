package services;

import models.ItemModel;
import play.libs.F;

import java.util.ArrayList;
import java.util.List;

public class ItemsService {

    public F.Promise<ItemModel> get(long id) {
        return F.Promise.pure(new ItemModel(id, "nombre"));
    }

    public F.Promise<List<ItemModel>> getUserItems(long userId) {
        // TODO: Ir a la API de items y traer todos los items del user
        return F.Promise.pure(new ArrayList<>());
    }
}