package mappers;

import dtos.ItemDto;
import dtos.ItemsResponseDto;

public class ItemMapper {

    public ItemDto buildFromDto(ItemsResponseDto.ItemResponseDto itemResponseDto) {
        ItemDto itemDto = new ItemDto();
        itemDto.id = itemResponseDto.id;
        itemDto.title = itemResponseDto.title;

        return itemDto;
    }
}
