package com.officine.losto.model;

import com.officine.losto.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;

import java.util.*;

public interface OrdersRepo extends JpaRepository<Orders, Long> {

    /**
     * OR semantics: matches if any non-empty criterion matches (same as derived query with String dates).
     * Date column is cast to string for partial text match on {@code orderDate} parameter.
     */
    @Query("""
            select o from Orders o where
            (coalesce(:number, '') = '' or lower(o.number) like lower(concat('%', :number, '%')))
            or (coalesce(:orderDate, '') = '' or cast(o.orderDate as string) like concat('%', :orderDate, '%'))
            or (coalesce(:description, '') = '' or lower(o.description) like lower(concat('%', :description, '%')))
            """)
    List<Orders> findByNumberContainingOrOrderDateContainingOrDescriptionContaining(
            @Param("number") String number,
            @Param("orderDate") String orderDate,
            @Param("description") String description);

    Orders findByNumber(String number);
}
