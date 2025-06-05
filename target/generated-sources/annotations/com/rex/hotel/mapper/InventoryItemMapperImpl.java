package com.rex.hotel.mapper;

import com.rex.hotel.dto.request.ItemRequest;
import com.rex.hotel.model.InventoryItem;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-28T23:53:01+0700",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class InventoryItemMapperImpl implements InventoryItemMapper {

    @Override
    public InventoryItem toEntity(ItemRequest request) {
        if ( request == null ) {
            return null;
        }

        InventoryItem.InventoryItemBuilder inventoryItem = InventoryItem.builder();

        inventoryItem.name( request.getName() );
        inventoryItem.code( request.getCode() );
        inventoryItem.category( request.getCategory() );
        inventoryItem.quantity( request.getQuantity() );
        inventoryItem.unit( request.getUnit() );
        inventoryItem.minQuantity( request.getMinQuantity() );
        inventoryItem.price( request.getPrice() );
        inventoryItem.supplier( request.getSupplier() );
        inventoryItem.description( request.getDescription() );

        return inventoryItem.build();
    }

    @Override
    public void updateInventoryItemFromRequest(ItemRequest request, InventoryItem inventoryItem) {
        if ( request == null ) {
            return;
        }

        inventoryItem.setName( request.getName() );
        inventoryItem.setCode( request.getCode() );
        inventoryItem.setCategory( request.getCategory() );
        inventoryItem.setQuantity( request.getQuantity() );
        inventoryItem.setUnit( request.getUnit() );
        inventoryItem.setMinQuantity( request.getMinQuantity() );
        inventoryItem.setPrice( request.getPrice() );
        inventoryItem.setSupplier( request.getSupplier() );
        inventoryItem.setDescription( request.getDescription() );
    }
}
