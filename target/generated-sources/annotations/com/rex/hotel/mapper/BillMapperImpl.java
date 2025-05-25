package com.rex.hotel.mapper;

import com.rex.hotel.dto.response.BillItemResponse;
import com.rex.hotel.dto.response.BillResponse;
import com.rex.hotel.dto.response.BillServiceResponse;
import com.rex.hotel.model.Bill;
import com.rex.hotel.model.BillItem;
import com.rex.hotel.model.BillService;
import com.rex.hotel.model.Booking;
import com.rex.hotel.model.InventoryItem;
import com.rex.hotel.model.ServiceHotel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-25T04:09:08+0700",
    comments = "version: 1.5.2.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class BillMapperImpl implements BillMapper {

    @Override
    public BillResponse toBillResponse(Bill bill) {
        if ( bill == null ) {
            return null;
        }

        BillResponse.BillResponseBuilder billResponse = BillResponse.builder();

        billResponse.bookingId( billBookingId( bill ) );
        billResponse.createdAt( bill.getCreatedAt() );
        billResponse.id( bill.getId() );
        billResponse.items( toBillItemResponses( bill.getItems() ) );
        billResponse.paidAt( bill.getPaidAt() );
        billResponse.roomCharge( bill.getRoomCharge() );
        billResponse.services( toBillServiceResponses( bill.getServices() ) );
        billResponse.status( bill.getStatus() );
        billResponse.totalAmount( bill.getTotalAmount() );

        return billResponse.build();
    }

    @Override
    public List<BillItemResponse> toBillItemResponses(List<BillItem> billItems) {
        if ( billItems == null ) {
            return null;
        }

        List<BillItemResponse> list = new ArrayList<BillItemResponse>( billItems.size() );
        for ( BillItem billItem : billItems ) {
            list.add( toBillItemResponse( billItem ) );
        }

        return list;
    }

    @Override
    public List<BillServiceResponse> toBillServiceResponses(List<BillService> billServices) {
        if ( billServices == null ) {
            return null;
        }

        List<BillServiceResponse> list = new ArrayList<BillServiceResponse>( billServices.size() );
        for ( BillService billService : billServices ) {
            list.add( toBillServiceResponse( billService ) );
        }

        return list;
    }

    @Override
    public BillItemResponse toBillItemResponse(BillItem billItem) {
        if ( billItem == null ) {
            return null;
        }

        BillItemResponse.BillItemResponseBuilder billItemResponse = BillItemResponse.builder();

        billItemResponse.itemName( billItemItemName( billItem ) );
        billItemResponse.id( billItem.getId() );
        billItemResponse.price( billItem.getPrice() );
        billItemResponse.quantity( billItem.getQuantity() );

        return billItemResponse.build();
    }

    @Override
    public BillServiceResponse toBillServiceResponse(BillService billService) {
        if ( billService == null ) {
            return null;
        }

        BillServiceResponse.BillServiceResponseBuilder billServiceResponse = BillServiceResponse.builder();

        billServiceResponse.serviceName( billServiceServiceName( billService ) );
        billServiceResponse.id( billService.getId() );
        billServiceResponse.price( billService.getPrice() );
        billServiceResponse.quantity( billService.getQuantity() );

        return billServiceResponse.build();
    }

    private Long billBookingId(Bill bill) {
        if ( bill == null ) {
            return null;
        }
        Booking booking = bill.getBooking();
        if ( booking == null ) {
            return null;
        }
        Long id = booking.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String billItemItemName(BillItem billItem) {
        if ( billItem == null ) {
            return null;
        }
        InventoryItem item = billItem.getItem();
        if ( item == null ) {
            return null;
        }
        String name = item.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String billServiceServiceName(BillService billService) {
        if ( billService == null ) {
            return null;
        }
        ServiceHotel service = billService.getService();
        if ( service == null ) {
            return null;
        }
        String name = service.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
