package com.seproject.buildmanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.seproject.buildmanager.entity.MstUser;

/**
 * mst_userテーブルに関連するDB操作を行うリポジトリインターフェースです。
 * 
 * <p>
 * このインターフェースは、ユーザーのCRUD操作およびカスタムクエリを提供します。
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
public interface MstUserRepository extends JpaRepository<MstUser, Integer> {

  /**
   * 全てのユーザを取得します。
   * 
   * @return 全ユーザのリスト
   */
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  List<MstUser> findAll();

  /**
   * 全てのユーザを権限情報と共に取得します。
   * 
   * @return 権限情報を含む全ユーザのリスト
   */
  @Query("SELECT u FROM MstUser u JOIN FETCH u.mstAuth")
  List<MstUser> findAllUsersWithAuthName();

  /**
   * ログインコードを指定してユーザとその権限を取得します。
   * 
   * <p>
   * このメソッドは、ユーザがログインする際に使用されます。
   * 
   * @param loginCd ログインコード
   * @return 指定されたログインコードに一致するユーザのOptionalオブジェクト
   */
  @Query(value = "SELECT " + "    u.id AS id," + "    u.login_cd," + "    u.password,"
      + "    u.mst_auth_id," + "    GROUP_CONCAT(mas.role_name) AS role," + "    u.l_name,"
      + "    u.f_name," + "    u.l_name_kana," + "    u.f_name_kana," + "    u.tel,"
      + "    u.email," + "    u.status," + "    u.created_at," + "    u.updated_at,"
      + "    u.updated_mst_user_id " + "FROM" + "    mst_user u" + "        LEFT JOIN"
      + "    mst_auth a ON u.mst_auth_id = a.id" + "        LEFT JOIN"
      + "    mst_auth_type mat ON a.mst_auth_type_id = mat.id" + "        LEFT JOIN"
      + "    mst_auth_screen mas ON mat.id = mas.mst_auth_type_id " + "WHERE"
      + "    u.login_cd = :loginCd AND a.status = 1 AND u.status = 1 "
      + "GROUP BY u.id , u.login_cd , u.password , u.l_name , u.f_name , u.l_name_kana , u.f_name_kana , u.tel , u.email , u.status , u.created_at , u.updated_at , u.updated_mst_user_id",
      nativeQuery = true)
  Optional<MstUser> findByLoginCd(@Param("loginCd") String loginCd);

  /**
   * ユーザのステータスを有効・無効に切り替えます。
   * 
   * <p>
   * このメソッドは、指定されたユーザIDに対してステータスをトグルします。
   * 
   * @param userId ステータスを切り替えるユーザのID
   */
  @Modifying
  @Transactional
  @Query("UPDATE MstUser u SET u.status = CASE WHEN u.status = 1 THEN 0 ELSE 1 END WHERE u.id = :userId")
  void toggleStatus(@Param("userId") Integer userId);

  /**
   * 指定したログインコードが既に登録済みか確認します
   * 
   * <p>
   * このメソッドは、指定されたログインコードに対して存在判定を行います。
   * 
   * @param loginCd
   * @return ログインコードが登録済みか否か
   */
  boolean existsByLoginCd(String loginCd);

  /**
   * 指定したメールアドレスが既に登録済みか確認します
   * 
   * <p>
   * このメソッドは、指定されたメールアドレスに対して存在判定を行います。
   * 
   * @param email
   * @return メールアドレスが登録済みか否か
   */
  boolean existsByEmail(String email);


  // 追記
  @Query(value = "SELECT " + "    u.id AS id," + "    u.login_cd," + "    u.password,"
      + "    u.mst_auth_id," + "    GROUP_CONCAT(mas.role_name) AS role," + "    u.l_name,"
      + "    u.f_name," + "    u.l_name_kana," + "    u.f_name_kana," + "    u.tel,"
      + "    u.email," + "    u.status," + "    u.created_at," + "    u.updated_at,"
      + "    u.updated_mst_user_id " + "FROM" + "    mst_user u" + "        LEFT JOIN"
      + "    mst_auth a ON u.mst_auth_id = a.id" + "        LEFT JOIN"
      + "    mst_auth_type mat ON a.mst_auth_type_id = mat.id" + "        LEFT JOIN"
      + "    mst_auth_screen mas ON mat.id = mas.mst_auth_type_id " + "WHERE"
      + "    u.login_cd = :loginCd AND a.status = 1 AND u.status = 1 "
      + "GROUP BY u.id , u.login_cd , u.password , u.l_name , u.f_name , u.l_name_kana , u.f_name_kana , u.tel , u.email , u.status , u.created_at , u.updated_at , u.updated_mst_user_id",
      nativeQuery = true)
  MstUser findByLogin(@Param("loginCd") String loginCd);

  @Query(
      value = "SELECT u.id AS id, u.login_cd, u.password, u.mst_auth_id, GROUP_CONCAT(mas.role_name) AS role, u.l_name,u.f_name, u.l_name_kana, u.f_name_kana, u.tel, u.email, u.status, u.created_at, u.updated_at, u.updated_mst_user_id FROM"
          + " mst_user u LEFT JOIN mst_auth a ON u.mst_auth_id = a.id LEFT JOIN mst_auth_type mat ON a.mst_auth_type_id = mat.id LEFT JOIN mst_auth_screen mas ON mat.id = mas.mst_auth_type_id"
          + " WHERE CASE WHEN :loginCd = '' THEN TRUE ELSE u.login_cd = :loginCd END"
          + " AND CASE WHEN :role = '' THEN TRUE ELSE role LIKE :role END"
          + " AND CASE WHEN :userName = '' THEN TRUE ELSE concat(u.l_name,u.f_name) LIKE :userName END"
          + " AND CASE WHEN :userNameKana = '' THEN TRUE ELSE concat(u.l_name_kana,u.f_name_kana) LIKE :userNameKana END"
          + " AND CASE WHEN :tel = '' THEN TRUE ELSE u.tel LIKE :tel END"
          + " AND CASE WHEN :email = '' THEN TRUE ELSE u.email LIKE :email END"
          + " AND CASE WHEN :status = '' THEN TRUE ELSE u.status = :status END"
          + " AND CASE WHEN :createdAt = '' THEn TRUE ELSE u.created_at LIKE :createdAt END"
          + " AND CASE WHEN :updatedAt = '' THEN TRUE ELSE u.updated_at LIKE :updatedAt END"
          + " GROUP BY u.id , u.login_cd , u.password , u.l_name , u.f_name , u.l_name_kana , u.f_name_kana , u.tel , u.email , u.status , u.created_at , u.updated_at , u.updated_mst_user_id",
      nativeQuery = true)
  public List<MstUser> search(@Param("loginCd") String loginCd, @Param("role") String role,
      @Param("userName") String userName, @Param("userNameKana") String userNameKana,
      @Param("tel") String tel, @Param("email") String email, @Param("status") String status,
      @Param("createdAt") String createdAt, @Param("updatedAt") String updatedAt);
}
