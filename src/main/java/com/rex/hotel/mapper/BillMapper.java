package com.rex.hotel.mapper;

import com.rex.hotel.dto.response.BillItemResponse;
import com.rex.hotel.dto.response.BillResponse;
import com.rex.hotel.dto.response.BillServiceResponse;
import com.rex.hotel.model.Bill;
import com.rex.hotel.model.BillItem;
import com.rex.hotel.model.BillService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BillMapper {

    @Mapping(source = "booking.id", target = "bookingId")
    BillResponse toBillResponse(Bill bill);

    List<BillItemResponse> toBillItemResponses(List<BillItem> billItems);

    List<BillServiceResponse> toBillServiceResponses(List<BillService> billServices);

    @Mapping(source = "item.name", target = "itemName") // 🔥 Lấy tên từ entity Item
    BillItemResponse toBillItemResponse(BillItem billItem);

    @Mapping(source = "service.name", target = "serviceName") // 🔥 Lấy tên từ entity Service
    BillServiceResponse toBillServiceResponse(BillService billService);
}
