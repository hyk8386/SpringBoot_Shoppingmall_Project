package com.shop.entity;


import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // 정렬할 때 사용하는 order 키워드가 있기 때문에 orders로 지정
@Getter
@Setter
public class Order extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 한 명의 회원은 여러 번 주문을 할 수 있으므로 주문 엔티티 기준에서 다대일 단방향 매핑
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;    // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    // 주문 상태

    // 관계의 주인 설정 - OrderItem에 있는 Order에 의해 관리 됨 / 부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이 / 고아 객체 제거 - 주문 엔티티(부모)에서 주문상품 엔티티(자식)를 삭제했을 때 orderItem 엔티티 삭제
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();


    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member);    // 상품을 주문한 회원 정보 세팅
        for (OrderItem orderItem : orderItemList){  // 장바구니 페이지에서 여러 개의 주문 상품을 담을 수 있도록 리스트형태로 파라미터 값을 받으며 주문 객체에 orderItem 객체를 추가
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);    // 주문 상태를 ORDER 상태로 세팅
        order.setOrderDate(LocalDateTime.now());    // 현재 시간을 주문 시간으로 세팅
        return order;
    }

    public int getTotalPrice(){     // 총 주문 금액을 구하는 메소드
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;

        for (OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

}
