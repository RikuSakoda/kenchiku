package com.seproject.buildmanager.entity;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * mst_authテーブルのエンティティクラスです。
 * 
 * <p>
 * このクラスは、権限情報を保持するデータベーステーブルを表現します。 各フィールドはテーブルのカラムに対応しています。
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
@Data
@NoArgsConstructor
@Table(name = "mst_auth")
public class MstAuth {

  /**
   * ユニークな識別子。
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id; // ID

  /**
   * 権限タイプの識別子。
   */
  @Column(name = "mst_auth_type_id")
  private Integer mstAuthTypeId; // 権限ID

  /**
   * 権限名。 必須項目。
   */
  @Column(name = "name", nullable = false)
  private String name; // 権限名

  /**
   * ステータス。 必須項目。
   */
  @Column(name = "status", nullable = false)
  private Integer status; // ステータス

  /**
   * 作成日時。
   */
  @Column(name = "created_at")
  private LocalDateTime createdAt; // 登録日

  /**
   * 更新日時。
   */
  @Column(name = "updated_at")
  private LocalDateTime updatedAt; // 更新日

  /**
   * 更新したユーザーの識別子。
   */
  @Column(name = "updated_mst_user_id")
  private Integer updatedMstUserId; // 更新者ID

  /**
   * 権限に関連付けられたユーザーのリスト。
   */
  @OneToMany(mappedBy = "mstAuth")
  private List<MstUser> mstUsers;
}
