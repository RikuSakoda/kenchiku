package com.seproject.buildmanager.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * リフォーム・修繕箇所チェック項目を表すエンティティクラスです。
 * 
 * <p>
 * このクラスは、リフォームや修繕のチェック項目に関する情報を保持します。
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
@Table(name = "mst_check")
@Data
@NoArgsConstructor
@NamedEntityGraph(name = "MstCheck.withAllAssociations", includeAllAttributes = true)
public class MstCheck {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, insertable = false, updatable = false)
  private Integer id; // ID

  @Column(name = "check_detail", nullable = false)
  private String checkDetail; // リフォーム・修繕箇所チェック項目

  @Column(name = "status")
  private Integer status; // ステータス

  @Column(name = "created_at")
  private LocalDateTime createdAt; // 登録日

  @Column(name = "updated_at")
  private LocalDateTime updatedAt; // 更新日

  @Column(name = "updated_mst_user_id")
  private Integer updatedMstUserId; // 更新者ID

  @JoinColumn(name = "group_id", referencedColumnName = "group_id", nullable = true)
  private Integer groupId;// グループid
  @JoinColumn(name = "check_id", referencedColumnName = "check_id", nullable = true,
      insertable = false, updatable = false)
  private Integer Id;// チェックid
  @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = true)
  private Integer customerId;// 顧客id


}
