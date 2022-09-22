package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 하나의 장바구니에 여러개의 상품을 담을 수 있음 - 다대일 관계
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)  // 하나의 상품은 여러 장바구니의 장바구니 상품으로 담길 수 있음 - 다대일 관계
    @JoinColumn(name = "item_id")
    private Item item;  // 장바구니에 담을 상품의 정보를 알아야 하므로 상품 엔티티 매핑

    private int count;  // 같은 상품을 장바구니에 몇 개 담을지 저장


    public static CartItem createItem(Cart cart, Item item, int count){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    // 장바구니에 담겨있던 상품을 추가로 장바구니에 담을 때 사용할 메소드
    public void addCount(int count){
        this.count += count;
    }

    // 현재 장바구니에 담겨있는 수량을 변경하는 메소드
    public void updateCount(int count){
        this.count = count;
    }
}
