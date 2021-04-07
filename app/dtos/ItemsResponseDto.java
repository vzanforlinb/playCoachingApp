package dtos;

import java.util.List;

public class ItemsResponseDto {
    public List<ItemResponseDto> results;

    public static class ItemResponseDto {
        public String id;
        public String title;
    }
}

