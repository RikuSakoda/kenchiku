package com.seproject.buildmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.seproject.buildmanager.entity.MstCustomer;

public interface MstCustomerRepository extends JpaRepository<MstCustomer, Integer> {

  // @EntityGraph(value = "MstCustomer.withAllAssociations", type =
  // EntityGraph.EntityGraphType.FETCH)
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstCustomer> findAll();



  @Modifying
  @Transactional
  @Query("UPDATE MstCustomer c SET c.status = CASE WHEN c.status = 1 THEN 0 ELSE 1 END WHERE c.id = :custId")
  void toggleStatus(@Param("custId") Integer custId);

  @Query(
      value = "SELECT c.id, c.cust_kind, c.cust_corp_name, c.cust_corp_name_kana, c.cust_department_name, c.cust_l_name, c.cust_f_name, c.cust_l_name_kana, c.cust_f_name_kana, c.cust_zip, c.cust_prefecture, c.cust_address1, c.cust_address2,"
          + " c.cust_tel, c.cust_mobile, c.cust_mail, c.cust_mail_flg, c.status, c.created_at, c.updated_at FROM"
          + " mst_customer c WHERE CASE WHEN :custKind = '' THEN TRUE ELSE c.cust_kind = :custKind END "
          + "AND CASE WHEN :corpName = '' THEN TRUE ELSE c.cust_corp_name LIKE :corpName END"
          + " AND CASE WHEN :corpKana = '' THEN TRUE ELSE c.cust_corp_name_kana LIKE :corpKana END"
          + " AND CASE WHEN :department = '' THEN TRUE ELSE c.cust_department_name LIKE :department END "
          + "AND CASE WHEN :customerName = '' THEN TRUE ELSE concat(c.cust_f_name,c.cust_l_name) LIKE :customerName END"
          + " AND CASE WHEN :customerNameKana = '' THEN TRUE ELSE concat(c.cust_l_name_kana,c.cust_f_name_kana) LIKE :customerNameKana END "
          + "AND CASE WHEN :zip = '' THEN TRUE ELSE c.cust_zip LIKE :zip END"
          + " AND CASE WHEN :prefecture = '' THEN TRUE ELSE c.cust_prefecture = :prefecture END "
          + " AND CASE WHEN :address1 = '' THEN TRUE ELSE c.cust_address1 LIKE :address1 END "
          + "AND CASE WHEN :address2 = '' THEN TRUE ELSE c.cust_address2 LIKE :address2 END "
          + "AND CASE WHEN :tel = '' THEN TRUE ELSE c.cust_tel LIKE :tel END"
          + " AND CASE WHEN :mobile = '' THEN TRUE ELSE c.cust_mobile LIKE :mobile END"
          + " AND CASE WHEN :mail = '' THEN TRUE ELSE c.cust_mail LIKE :mail END "
          + "AND CASE WHEN :status = '' THEN TRUE ELSE c.status = :status END"
          + " AND CASE WHEN :created_at = '' THEN TRUE ELSE c.created_at LIKE :created_at END"
          + " AND CASE WHEN :updated_at = '' THEN TRUE ELSE c.updated_at LIKE :updated_at END",
      nativeQuery = true)
  public List<MstCustomer> search(@Param("custKind") String cust_kind,
      @Param("corpName") String corpName, @Param("corpKana") String corpKana,
      @Param("department") String department, @Param("customerName") String customerName,
      @Param("customerNameKana") String customerNameKana, @Param("zip") String zip,
      @Param("prefecture") String prefecture, @Param("address1") String address1,
      @Param("address2") String address2, @Param("tel") String tel, @Param("mobile") String mobile,
      @Param("mail") String mail, @Param("status") String status,
      @Param("created_at") String createdAt, @Param("updated_at") String updatedAt);



}
