package services;

import dtos.CategoryResponseDto;
import models.CategoryModel;
import play.Logger;
import play.libs.F;
import repositories.CategoriesRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class CategoriesService {

    private CategoriesRepository categoriesRepository;

    @Inject
    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public F.Promise<List<CategoryModel>> getSiteCategories(String siteId) {
        Logger.info("getCategories init: " + Thread.currentThread().getId());
        return categoriesRepository.get(siteId)
            .map(categoryResponseDtos -> {
                    Logger.info("getCategories end: " + Thread.currentThread().getId());
                    return categoryResponseDtos
                        .stream()
                        .map(this::mapModel)
                        .collect(Collectors.toList());
                }
            );
    }

    private CategoryModel mapModel(CategoryResponseDto categoryResponseDto) {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.id = categoryResponseDto.id;
        categoryModel.name = categoryResponseDto.name;
        return categoryModel;
    }
}
