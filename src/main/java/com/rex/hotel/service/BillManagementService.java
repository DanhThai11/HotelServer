package com.rex.hotel.service;

import com.rex.hotel.dto.request.BillItemRequest;
import com.rex.hotel.dto.request.BillRequest;
import com.rex.hotel.dto.request.BillServiceRequest;
import com.rex.hotel.dto.response.BillResponse;
import com.rex.hotel.enums.BillStatus;
import com.rex.hotel.enums.BookingStatus;
import com.rex.hotel.exception.AppException;
import com.rex.hotel.exception.ErrorCode;
import com.rex.hotel.mapper.BillMapper;
import com.rex.hotel.model.*;
import com.rex.hotel.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BillManagementService {
    private final BillRepository billRepository;
    private final BookingRepository bookingRepository;
    private final BillItemRepository billItemRepository;
    private final BillServiceRepository billServiceRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final BillMapper billMapper;
    private final ServiceRepository serviceRepository;
    private final InventoryItemRepository itemRepository;

    public List<BillResponse> getAllBills() {
        return billRepository.findAll().stream()
                .map(billMapper::toBillResponse)
                .toList();
    }

    public BillResponse getBillById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_EXISTED));
        return billMapper.toBillResponse(bill);
    }

    public BillResponse getBillByBookingId(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));
        return billMapper.toBillResponse(billRepository.findByBooking(booking));
    }

    @Transactional
    public BillResponse createBill(BillRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        Bill bill = new Bill();
        bill.setBooking(booking);
        bill.setRoomCharge(request.getRoomCharge());
        bill.setStatus(BillStatus.PENDING);
        bill.setCreatedAt(LocalDateTime.now());

        // Xử lý danh sách BillItem nếu có
        if (request.getItems() != null) {
            List<BillItem> billItems = request.getItems().stream()
                    .map(itemRequest -> {
                        InventoryItem inventoryItem = inventoryItemRepository.findById(itemRequest.getInventoryItemId())
                                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND));

                        BillItem billItem = new BillItem();
                        billItem.setBill(bill);
                        billItem.setItem(inventoryItem);
                        billItem.setQuantity(itemRequest.getQuantity());
                        billItem.setPrice(itemRequest.getPrice()); // 🔥 Lưu đúng giá lúc đặt
                        return billItem;
                    }).collect(Collectors.toList());

            bill.setItems(billItems);
        }

        // Xử lý danh sách BillService nếu có
        if (request.getServices() != null) {
            List<BillService> billServices = request.getServices().stream()
                    .map(serviceRequest -> {
                        ServiceHotel serviceHotelEntity = serviceRepository.findById(serviceRequest.getServiceId())
                                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));

                        BillService billService = new BillService();
                        billService.setBill(bill);
                        billService.setService(serviceHotelEntity);
                        billService.setQuantity(serviceRequest.getQuantity());
                        billService.setPrice(serviceRequest.getPrice()); // 🔥 Lưu đúng giá lúc đặt
                        return billService;
                    }).collect(Collectors.toList());

            bill.setServices(billServices);
        }

        // 🔥 Cập nhật tổng tiền hóa đơn trước khi lưu
        updateTotalAmount(bill);

        return billMapper.toBillResponse(billRepository.save(bill));
    }

    @Transactional
    public BillResponse updateBill(Long id, BillRequest request) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_EXISTED));

        if (bill.getStatus() == BillStatus.PAID) {
            throw new AppException(ErrorCode.BILL_ALREADY_PAID);
        }

        bill.setRoomCharge(request.getRoomCharge());

        // Tính lại tổng tiền
        updateTotalAmount(bill);

        return billMapper.toBillResponse(billRepository.save(bill));
    }

    @Transactional
    public BillResponse addBillItem(Long billId, BillItemRequest request) {
        Bill bill = getBillEntityOrThrow(billId);

        InventoryItem item = inventoryItemRepository.findById(request.getInventoryItemId())
                .orElseThrow(() -> new AppException(ErrorCode.INVENTORY_NOT_EXISTED));

        if (item.getQuantity() < request.getQuantity()) {
            throw new AppException(ErrorCode.NOT_ENOUGH_STOCK);
        }

        BillItem billItem = new BillItem();
        billItem.setBill(bill);
        billItem.setItem(item);
        billItem.setQuantity(request.getQuantity());
        billItem.setPrice(request.getPrice());

        item.setQuantity(item.getQuantity() - request.getQuantity());
        inventoryItemRepository.save(item);

        bill.getItems().add(billItem);
        updateTotalAmount(bill);

        return billMapper.toBillResponse(billRepository.save(bill));
    }

    @Transactional
    public BillResponse addBillService(Long billId, BillServiceRequest request) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_EXISTED));

        if (bill.getStatus() == BillStatus.PAID) {
            throw new AppException(ErrorCode.BILL_ALREADY_PAID);
        }

        ServiceHotel serviceHotel = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_EXISTED));

        BillService billService = new BillService();
        billService.setBill(bill);
        billService.setService(serviceHotel);
        billService.setQuantity(request.getQuantity());
        billService.setPrice(request.getPrice());

        bill.getServices().add(billService);
        billServiceRepository.save(billService);

        updateTotalAmount(bill);

        return billMapper.toBillResponse(billRepository.save(bill));
    }

    @Transactional
    public BillResponse removeBillItem(Long billId, Long itemId) {
        Bill bill = getBillEntityOrThrow(billId);

        BillItem item = bill.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.BILL_ITEM_NOT_EXISTED));

        InventoryItem inventoryItem = item.getItem();
        inventoryItem.setQuantity(inventoryItem.getQuantity() + item.getQuantity());
        inventoryItemRepository.save(inventoryItem);

        bill.getItems().remove(item);
        billItemRepository.delete(item);
        updateTotalAmount(bill);

        return billMapper.toBillResponse(billRepository.save(bill));
    }

    @Transactional
    public BillResponse removeBillService(Long billId, Long serviceId) {
        Bill bill = getBillEntityOrThrow(billId);

        BillService service = bill.getServices().stream()
                .filter(s -> s.getId().equals(serviceId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.BILL_SERVICE_NOT_EXISTED));

        bill.getServices().remove(service);
        billServiceRepository.delete(service);
        updateTotalAmount(bill);

        return billMapper.toBillResponse(billRepository.save(bill));
    }

    @Transactional
    public BillResponse payBill(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_FOUND));

        if (bill.getStatus() == BillStatus.PAID) {
            throw new AppException(ErrorCode.BILL_ALREADY_PAID);
        }

        bill.setStatus(BillStatus.PAID);
        bill.setPaidAt(LocalDateTime.now());

        // Cập nhật trạng thái Booking sang CONFIRMED khi Bill được thanh toán
        Booking booking = bill.getBooking();
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        return billMapper.toBillResponse(billRepository.save(bill));
    }

    public Double calculateRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        return billRepository.calculateRevenueForPeriod(startDate, endDate);
    }

    public List<BillResponse> getOverdueBills() {
        return billRepository.findOverdueBills().stream()
                .map(billMapper::toBillResponse)
                .toList();
    }

    private void updateTotalAmount(Bill bill) {
        BigDecimal total = BigDecimal.ZERO;

        // Tiền phòng (niêm yết)
        if (bill.getRoomCharge() != null) {
            total = total.add(bill.getRoomCharge());
        }

        // Tiền items (món ăn, đồ dùng)
        if (bill.getItems() != null) {
            BigDecimal itemsTotal = bill.getItems().stream()
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            total = total.add(itemsTotal);
        }

        // Tiền services (dịch vụ thêm)
        if (bill.getServices() != null) {
            BigDecimal servicesTotal = bill.getServices().stream()
                    .map(BillService::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            total = total.add(servicesTotal);
        }

        bill.setTotalAmount(total);
    }

    private Bill getBillEntityOrThrow(Long billId) {
        return billRepository.findById(billId)
                .orElseThrow(() -> new AppException(ErrorCode.BILL_NOT_EXISTED));
    }

    @Transactional // Đảm bảo việc tạo Bill là một phần của transaction
    public Bill createBillForBooking(Booking booking) {
        Bill bill = new Bill(); // Cần import Bill entity
        bill.setBooking(booking);
        bill.setRoomCharge(booking.getTotalAmount()); // Set roomCharge bằng totalAmount của Booking
        bill.setTotalAmount(booking.getTotalAmount()); // Set totalAmount ban đầu bằng totalAmount của Booking
        bill.setStatus(BillStatus.PENDING); // Hoặc BillStatus.UNPAID tùy enum của bạn
        bill.setCreatedAt(LocalDateTime.now()); // createdAt đã có @PrePersist, có thể bỏ dòng này

        // Nếu có các trường khác trong Bill entity cần set mặc định khi tạo tự động, set ở đây

        return billRepository.save(bill); // Lưu và trả về Bill đã lưu
    }
} 