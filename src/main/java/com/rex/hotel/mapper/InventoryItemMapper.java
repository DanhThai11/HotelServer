package com.rex.hotel.mapper;

import com.rex.hotel.dto.request.ItemRequest;
import com.rex.hotel.model.InventoryItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InventoryItemMapper {

    InventoryItem toEntity(ItemRequest request);

    void updateInventoryItemFromRequest(ItemRequest request, @MappingTarget InventoryItem inventoryItem);
}
