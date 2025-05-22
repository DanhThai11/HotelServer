package com.rex.hotel.service;

import com.rex.hotel.dto.request.ItemRequest;
import com.rex.hotel.dto.response.InventoryItemResponse;
import com.rex.hotel.model.InventoryItem;
import com.rex.hotel.repository.InventoryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryItemService {

    private final InventoryItemRepository itemRepository;

    @Transactional
    public InventoryItemResponse createItem(ItemRequest request) {
        InventoryItem item = new InventoryItem();
        item.setName(request.getName());
        item.setCode(request.getCode());
        item.setUnit(request.getUnit());
        item.setMinQuantity(request.getMinQuantity());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategory(request.getCategory());
        item.setQuantity(request.getQuantity());
        item.setSupplier(request.getSupplier());
        InventoryItem savedItem = itemRepository.save(item);
        return new InventoryItemResponse(savedItem.getId(), savedItem.getName(), savedItem.getPrice(), savedItem.getCategory());
    }
}
