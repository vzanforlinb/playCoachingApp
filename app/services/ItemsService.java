package services;

import mappers.ItemMapper;
import models.ItemModel;
import models.ItemRModel;
import play.db.jpa.JPA;
import play.libs.F;
import repositories.ItemsRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ItemsService {

    private ItemsRepository itemsRepository;
    private ItemMapper itemMapper;

    @Inject
    public ItemsService(ItemsRepository itemsRepository, ItemMapper itemMapper) {
        this.itemsRepository = itemsRepository;
        this.itemMapper = itemMapper;
    }

    public F.Promise<ItemModel> get(long id) {
        return F.Promise.pure(new ItemModel());
    }

    public F.Promise<List<ItemRModel>> getUserItems(long userId, String siteId) {
        return itemsRepository.get(userId, siteId).map(itemsResponseDto -> {
            List<ItemRModel> itemModelList = itemsResponseDto.results.stream()
                .map(itemResponseDto -> itemMapper.buildFromDto(itemResponseDto)).collect(Collectors.toList());
            return itemModelList;
        });
    }

    public F.Promise<Long> createItem() {
        try {
            return F.Promise.promise(() -> {
                // TODO: Aplicar repository pattern (usar JPA en capa de infr)
                ItemModel item1 = new ItemModel();
                item1.title = "articulo 1";
                item1.price = 200D;
                item1.quantity = 10;

                return JPA.withTransaction(() -> {
                    EntityManager entityManager = JPA.em();
                    entityManager.persist(item1);
                    entityManager.flush();
                    return item1.id;
                });
            });
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public F.Promise<ItemModel> getItem(long id) {
        try {
            return F.Promise.promise(() -> {

                return JPA.withTransaction(() -> JPA.em()
                    .find(ItemModel.class, id));
            });
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

    }


}