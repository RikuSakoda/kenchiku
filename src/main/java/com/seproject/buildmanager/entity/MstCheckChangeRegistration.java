package com.seproject.buildmanager.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * チェックグループ登録を表すエンティティクラスです。
 * 
 * <p>
 * このクラスは、チェックグループに関する情報を保持します。
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
@Table(name = "mst_check_group")
@Data
@NoArgsConstructor

public class MstCheckChangeRegistration {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;// ID

  @Column(name = "check_group_name")
  private String checkGroupName; // グループ名称

  @Column(name = "status")
  private Integer status; // ステータス

  @Column(name = "created_at")
  private LocalDateTime createdAt; // 登録日

  @Column(name = "updated_at")
  private LocalDateTime updatedAt; // 更新日

  @Column(name = "updated_mst_user_id")
  private Integer updatedMstUserId; // 最終更新者

  /** チェック項目の詳細 */
  @JoinColumn(name = "check_detail", referencedColumnName = "check_detail", nullable = true)
  private String checkDetail;// チェック項目


}
