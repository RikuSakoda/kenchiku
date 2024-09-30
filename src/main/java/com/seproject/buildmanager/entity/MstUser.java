package com.seproject.buildmanager.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * mst_userテーブルのエンティティクラスです。
 * 
 * <p>
 * このクラスは、ユーザーに関連する情報を保持するデータベーステーブルを表現します。 各フィールドはテーブルのカラムに対応しています。
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
@NamedEntityGraph(name = "MstUser.withAllAssociations", includeAllAttributes = true)
public class MstUser {

  /**
   * ユニークな識別子。
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * ログインコード。 必須項目。
   */
  @Column(name = "login_cd", nullable = false)
  private String loginCd;

  /**
   * パスワード。 必須項目。
   */
  @Column(name = "password", nullable = false)
  private String password;

  /**
   * 権限ID。 必須項目。
   */
  @Column(name = "mst_auth_id", insertable = true, updatable = true, nullable = false)
  private Integer mstAuthId;

  /**
   * 権限情報。
   */
  @ManyToOne
  @JoinColumn(name = "mst_auth_id", referencedColumnName = "id", insertable = false,
      updatable = false)
  private MstAuth mstAuth;

  /**
   * 姓。
   */
  @Column(name = "l_name")
  private String lName;

  /**
   * 名。
   */
  @Column(name = "f_name")
  private String fName;

  /**
   * 姓のカナ。
   */
  @Column(name = "l_name_kana")
  private String lNameKana;

  /**
   * 名のカナ。
   */
  @Column(name = "f_name_kana")
  private String fNameKana;

  /**
   * 電話番号。
   */
  @Column(name = "tel")
  private String tel;

  /**
   * メールアドレス。
   */
  @Column(name = "email")
  private String email;

  /**
   * ステータス。
   */
  @Column(name = "status")
  private Integer status;

  /**
   * 作成日時。
   */
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  /**
   * 更新日時。
   */
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  /**
   * 更新したユーザーの識別子。
   */
  @Column(name = "updated_mst_user_id")
  private Integer updatedMstUserId;

  /**
   * ロール名。
   */
  @Column(name = "role")
  private String role;

  /**
   * エンティティの新規作成時に作成日時と更新日時を設定します。
   */
  @PrePersist
  protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  /**
   * エンティティの更新時に更新日時を設定します。
   */
  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
