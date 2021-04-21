package mappers;

import dtos.ItemsResponseDto;
import models.ItemRModel;

public class ItemMapper {

    public ItemRModel buildFromDto(ItemsResponseDto.ItemResponseDto itemResponseDto) {
        ItemRModel itemModel = new ItemRModel();
        itemModel.id = itemResponseDto.id;
        itemModel.title = itemResponseDto.title;

        return itemModel;
    }
}
