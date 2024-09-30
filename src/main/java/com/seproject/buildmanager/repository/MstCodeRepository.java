package com.seproject.buildmanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.seproject.buildmanager.entity.MstCode;

public interface MstCodeRepository extends JpaRepository<MstCode, Integer> {
  @EntityGraph(value = "MstUser.withAllAssociations", type = EntityGraph.EntityGraphType.FETCH)
  public List<MstCode> findAll();

  @Query(
      value = "SELECT id, code_kind, code_branch_num, code_name, status, created_at, updated_at, updated_mst_user_id, code_bit FROM mst_code WHERE code_kind = ?1",
      nativeQuery = true)
  public List<MstCode> findByCodeKind(Integer codeKind);

  @Query(
      value = "SELECT id, code_kind, code_branch_num, code_name, status, created_at, updated_at, updated_mst_user_id, code_bit "
          + "FROM mst_code " + "WHERE code_name = :codeName ",
      nativeQuery = true)
  public Optional<MstCode> findByCodeName(@Param("codeName") String codeName);

  @Query(
      value = "SELECT id, code_kind, code_branch_num, code_name, status, created_at, updated_at, updated_mst_user_id, code_bit "
          + "FROM mst_code " + "WHERE code_kind = :codeKind AND code_branch_num = :codeBranchNum",
      nativeQuery = true)
  public Optional<MstCode> findByCodeKindAndBranchNum(@Param("codeKind") Integer codeKind,
      @Param("codeBranchNum") Integer codeBranchNum);

  @Query(
      value = "SELECT id, code_kind, code_branch_num, code_name, status, created_at, updated_at, updated_mst_user_id, code_bit "
          + "FROM mst_code " + "WHERE code_kind = :codeKind AND code_name = :codeName",
      nativeQuery = true)
  public Optional<MstCode> findByCodeKindAndName(@Param("codeKind") Integer codeKind,
      @Param("codeName") String codeName);

  @Query(
      value = "SELECT id, code_kind, code_branch_num, code_name, status, created_at, updated_at, updated_mst_user_id, code_bit "
          + "FROM mst_code "
          + "WHERE code_kind = :codeKind AND (ISNULL(:codeBit) || :codeBit & POW(2, code_branch_num) != 0)",
      nativeQuery = true)
  public List<MstCode> findCodeBitByBranchNum(@Param("codeKind") Integer codeKind,
      @Param("codeBit") Integer codeBit);

  @Query(
      value = "SELECT id, code_kind, code_branch_num, code_name, status, created_at, updated_at, updated_mst_user_id, code_bit FROM mst_code WHERE code_kind = ?1 AND"
          + " NOT code_branch_num = 1",
      nativeQuery = true)
  public List<MstCode> findByCodeKindStatus(Integer codeKind);

}
