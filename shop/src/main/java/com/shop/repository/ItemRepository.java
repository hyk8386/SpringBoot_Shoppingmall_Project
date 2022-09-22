package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>,
        QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    // itemNm(상품명)으로 데이터 조회 - find+(엔티티이름)+By+변수이름
    List<Item> findByItemNm(String itemNm); // 매개변수로 검색할 때 사용할 상품명 변수를 넘겨줌

    // OR 조건 처리
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    // LessThan 조건 처리 - 파라미터로 넘어온 price 변수보다 값이 작은 상품 데이터를 조회
    List<Item> findByPriceLessThan(Integer price);

    // OrderBy로 정렬
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    // @Query를 이용한 검색 처리
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    // @Query-nativeQuery 속성 : 기존의 데이터베이스에서 사용하던 쿼리를 그대로 사용해야 할 때 > 특정 데이터베이스에 종속되는 쿼리문을 사용하기 때문에 데이터베이스에 대해 독립적이라는 장점을 잃어버림
    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByDetailByNative(@Param("itemDetail") String itemDetail);



}

