package com.rex.hotel.mapper;

import com.rex.hotel.dto.request.ItemRequest;
import com.rex.hotel.model.InventoryItem;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-06T00:23:02+0700",
    comments = "version: 1.5.2.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class InventoryItemMapperImpl implements InventoryItemMapper {

    @Override
    public InventoryItem toEntity(ItemRequest request) {
        if ( request == null ) {
            return null;
        }

        InventoryItem.InventoryItemBuilder inventoryItem = InventoryItem.builder();

        inventoryItem.category( request.getCategory() );
        inventoryItem.code( request.getCode() );
        inventoryItem.description( request.getDescription() );
        inventoryItem.minQuantity( request.getMinQuantity() );
        inventoryItem.name( request.getName() );
        inventoryItem.price( request.getPrice() );
        inventoryItem.quantity( request.getQuantity() );
        inventoryItem.supplier( request.getSupplier() );
        inventoryItem.unit( request.getUnit() );

        return inventoryItem.build();
    }

    @Override
    public void updateInventoryItemFromRequest(ItemRequest request, InventoryItem inventoryItem) {
        if ( request == null ) {
            return;
        }

        inventoryItem.setCategory( request.getCategory() );
        inventoryItem.setCode( request.getCode() );
        inventoryItem.setDescription( request.getDescription() );
        inventoryItem.setMinQuantity( request.getMinQuantity() );
        inventoryItem.setName( request.getName() );
        inventoryItem.setPrice( request.getPrice() );
        inventoryItem.setQuantity( request.getQuantity() );
        inventoryItem.setSupplier( request.getSupplier() );
        inventoryItem.setUnit( request.getUnit() );
    }
}
