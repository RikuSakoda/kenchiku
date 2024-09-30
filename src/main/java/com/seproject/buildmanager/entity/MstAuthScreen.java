package com.seproject.buildmanager.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * mst_auth_screenテーブルのエンティティクラスです。
 * 
 * <p>このクラスは、認証画面に関連する情報を保持するデータベーステーブルを表現します。
 * 各フィールドはテーブルのカラムに対応しています。
 * 
 * <p>変更履歴：
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
@Table(name = "mst_auth_screen")
public class MstAuthScreen {

    /**
     * ユニークな識別子。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 認証タイプの識別子。
     */
    @Column(name = "mst_auth_type_id")
    private Integer mstAuthTypeId;

    /**
     * ロール名。
     * 必須項目。
     */
    @Column(name = "role_name", nullable = false)
    private String roleName;

    /**
     * 備考。
     * 必須項目。
     */
    @Column(name = "remarks", nullable = false)
    private String remarks;

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
}
