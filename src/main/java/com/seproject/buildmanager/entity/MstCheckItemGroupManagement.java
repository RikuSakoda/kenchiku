package com.seproject.buildmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * チェック項目グループを表すエンティティクラスです。
 * 
 * <p>
 * このクラスは、チェック項目グループに関する情報を保持します。
 * 
 * <p>
 * 変更履歴：
 * <ul>
 * <li>2024/10/31 - 初版作成</li>
 * </ul>
 * 
 * @since 1.0
 * @version 1.0
 */
@Entity
@Table(name = "mst_check_item_group_management")
@Data
@NoArgsConstructor
public class MstCheckItemGroupManagement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;// id

  @Column(name = "group_id")
  private Integer groupId;// グループid

  @Column(name = "check_id")
  private Integer checkId;// チェックid

  @Column(name = "customer_id")
  private Integer customerId;// 顧客id

  @Column(name = "group_name")
  private String groupName;// グループの名前

  @Column(name = "check_item")
  private String checkItem;// チェック項目のidを保管

  @Column(name = "customer_name")
  private String customerName;// 顧客名
}
