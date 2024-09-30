package com.seproject.buildmanager.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.seproject.buildmanager.entity.MstCode;
import com.seproject.buildmanager.entity.MstOwnerManagement;
import com.seproject.buildmanager.form.MstOwnerManagementForm;
import com.seproject.buildmanager.repository.MstCodeRepository;
import com.seproject.buildmanager.repository.MstOwnerManagementRepository;

public class MstOwnerManagementServiceTest {

  @Mock
  private MstOwnerManagementRepository mstOwnerRepository;

  @Mock
  private MstCodeRepository mstCodeRepository;

  @Mock
  private CommonService commonService;

  @InjectMocks
  private MstOwnerManagementService mstOwnerService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void すべてのオーナーを取得する() {
    List<MstOwnerManagement> ownerList = new ArrayList<>();
    ownerList.add(new MstOwnerManagement());
    when(mstOwnerRepository.findAll()).thenReturn(ownerList);

    List<MstOwnerManagement> result = mstOwnerService.getAllOwner();
    assertEquals(1, result.size());
  }

  @Test
  public void 顧客名を含めたすべてのオーナーを取得する() {
    List<MstOwnerManagement> ownerList = new ArrayList<>();
    ownerList.add(new MstOwnerManagement());
    when(mstOwnerRepository.findAllWithClientName()).thenReturn(ownerList);

    List<MstOwnerManagement> result = mstOwnerService.getAllOwnerWithCustmerName();
    assertEquals(1, result.size());
  }

  @Test
  public void 新規登録用のフォーム作成する() {
    MstOwnerManagementForm result = mstOwnerService.showOwnerForm();
    assertNotNull(result);
  }

  @Test
  public void 顧客名入り情報の取得する() {
    List<MstOwnerManagementForm> result = mstOwnerService.viewOwnerForm();
    assertNotNull(result);
  }

  @Test
  public void 表示用にエンティティからとってきたデータをフォームに格納する1() {
    MstOwnerManagement owner = new MstOwnerManagement();
    owner.setId(1);
    owner.setClientId(1);
    owner.setClientName("株式会社エス・イー・プロジェクト");
    owner.setIndividual(1);
    owner.setCorporation("株式会社エス・イー・プロジェクト");
    owner.setCorporationKana("カブシキガイシャエス・イー・プロジェクト");
    owner.setDepartment("開発部");
    owner.setLName("test");
    owner.setLNameKana("テスト");
    owner.setFName("owner");
    owner.setFNameKana("オーナー");
    owner.setPostCode("000-0000");
    owner.setPrefectures(3);
    owner.setAddress("aaaa");
    owner.setBuildingName("テストビル");
    owner.setPhone("000-0000-0000");
    owner.setMobilePhone("000-0000-0000");
    owner.setEmail("test@example.com");
    owner.setStatus(1);

    MstOwnerManagementForm result = mstOwnerService.findByIdConvertOwnerForm(owner.getId());
    assertNotNull(result);
  }


  @Test
  public void 表示用にエンティティからとってきたデータをフォームに格納する2() {
    MstOwnerManagement owner = new MstOwnerManagement();
    owner.setId(2);
    owner.setClientId(2);
    owner.setClientName("株式会社");
    owner.setIndividual(3);
    owner.setCorporation("株式会社");
    owner.setCorporationKana("カブシキガイシャ");
    owner.setDepartment("開発部");
    owner.setLName("test");
    owner.setLNameKana("テスト");
    owner.setFName("owner");
    owner.setFNameKana("オーナー");
    owner.setPostCode("123-4567");
    owner.setPrefectures(5);
    owner.setAddress("bbb");
    owner.setBuildingName("テストビル");
    owner.setPhone("090-0000-0000");
    owner.setMobilePhone("080-0000-0000");
    owner.setEmail("test@example.com");
    owner.setStatus(0);

    MstOwnerManagementForm result = mstOwnerService.findByIdConvertOwnerForm(owner.getId());
    assertNotNull(result);
  }


  @Test
  public void 新規オーナーを保存する() {
    MstOwnerManagementForm ownerForm = new MstOwnerManagementForm();
    ownerForm.setId("1");
    ownerForm.setClient("株式会社エス・イー・プロジェクト");
    ownerForm.setClientId("1");
    ownerForm.setIndividual("1");
    ownerForm.setCorporation("株式会社エス・イー・プロジェクト");
    ownerForm.setCorporationKana("カブシキガイシャエス・イー・プロジェクト");
    ownerForm.setDepartment("開発部");
    ownerForm.setLName("test");
    ownerForm.setLNameKana("テスト");
    ownerForm.setFName("ownerForm");
    ownerForm.setFNameKana("オーナーフォーム");
    ownerForm.setPostCode("000-0000");
    ownerForm.setPrefectures("3");
    ownerForm.setAddress("aaaa");
    ownerForm.setBuildingName("テストビル");
    ownerForm.setPhone("000-0000-0000");
    ownerForm.setMobilePhone("000-0000-0000");
    ownerForm.setEmail("test@example.com");
    ownerForm.setStatus("1");

    MstCode mstCode = new MstCode();
    mstCode.setId(1);
    mstCode.setCodeKind(3);
    mstCode.setCodeBranchNum(1);
    mstCode.setCodeName("北海道");
    mstCode.setStatus(1);

    when(mstOwnerRepository.save(any(MstOwnerManagement.class))).thenReturn(new MstOwnerManagement());
    // when(mstCodeRepository.findByCodeName("1")).thenReturn(Optional.of(mstCode));
    // when(commonService.getLoginUserId()).thenReturn(1);

    MstOwnerManagement result = mstOwnerService.saveOwnerRegister(ownerForm);
    assertNotNull(result);
    verify(mstOwnerRepository, times(1)).save(any(MstOwnerManagement.class));
  }

  @Test
  public void 無効なステータスを保存する() {
    MstOwnerManagementForm ownerForm = new MstOwnerManagementForm();
    ownerForm.setId("1");
    ownerForm.setClient("株式会社エス・イー・プロジェクト");
    ownerForm.setClientId("1");
    ownerForm.setIndividual("1");
    ownerForm.setCorporation("株式会社エス・イー・プロジェクト");
    ownerForm.setCorporationKana("カブシキガイシャエス・イー・プロジェクト");
    ownerForm.setDepartment("開発部");
    ownerForm.setLName("test");
    ownerForm.setLNameKana("テスト");
    ownerForm.setFName("ownerForm");
    ownerForm.setFNameKana("オーナーフォーム");
    ownerForm.setPostCode("000-0000");
    ownerForm.setPrefectures("3");
    ownerForm.setAddress("aaaa");
    ownerForm.setBuildingName("テストビル");
    ownerForm.setPhone("000-0000-0000");
    ownerForm.setMobilePhone("000-0000-0000");
    ownerForm.setEmail("test@example.com");
    ownerForm.setStatus("invalidNumber");// 例外発生

    MstCode mstCode = new MstCode();
    mstCode.setId(1);
    mstCode.setCodeKind(3);
    mstCode.setCodeBranchNum(1);
    mstCode.setCodeName("北海道");
    mstCode.setStatus(1);

    when(mstOwnerRepository.save(any(MstOwnerManagement.class))).thenReturn(new MstOwnerManagement());
    // when(mstCodeRepository.findByCodeName("1")).thenReturn(Optional.of(mstCode));
    // when(commonService.getLoginUserId()).thenReturn(1);

    MstOwnerManagement result = mstOwnerService.saveOwnerRegister(ownerForm);
    assertNotNull(result);
    verify(mstOwnerRepository, times(1)).save(any(MstOwnerManagement.class));
  }

  @Test
  public void id検索をし情報を取得する() {
    MstOwnerManagement owner = new MstOwnerManagement();
    owner.setId(1);
    when(mstOwnerRepository.findById(1)).thenReturn(Optional.of(owner));

    MstOwnerManagement result = mstOwnerService.findOwnerById(1);
    assertNotNull(result);
  }

  @Test
  public void 既存のオーナーを更新する() {
    MstOwnerManagementForm ownerForm = new MstOwnerManagementForm();
    ownerForm.setId("1");
    ownerForm.setClient("株式会社エス・イー・プロジェクト");
    ownerForm.setClientId("1");
    ownerForm.setIndividual("1");
    ownerForm.setCorporation("株式会社エス・イー・プロジェクト");
    ownerForm.setCorporationKana("カブシキガイシャエス・イー・プロジェクト");
    ownerForm.setDepartment("開発部");
    ownerForm.setLName("test");
    ownerForm.setLNameKana("テスト");
    ownerForm.setFName("ownerForm");
    ownerForm.setFNameKana("オーナーフォーム");
    ownerForm.setPostCode("000-0000");
    ownerForm.setPrefectures("3");
    ownerForm.setAddress("aaaa");
    ownerForm.setBuildingName("テストビル");
    ownerForm.setPhone("000-0000-0000");
    ownerForm.setMobilePhone("000-0000-0000");
    ownerForm.setEmail("test@example.com");
    ownerForm.setStatus("1");

    MstCode mstCode = new MstCode();
    mstCode.setId(1);
    mstCode.setCodeKind(3);
    mstCode.setCodeBranchNum(1);
    mstCode.setCodeName("北海道");
    mstCode.setStatus(1);

    when(mstOwnerRepository.save(any(MstOwnerManagement.class))).thenReturn(new MstOwnerManagement());
    when(mstCodeRepository.findByCodeName("1")).thenReturn(Optional.of(mstCode));
    when(commonService.getLoginUserId()).thenReturn(1);

    MstOwnerManagement result = mstOwnerService.saveOwnerUpdate(ownerForm);
    assertNotNull(result);
    verify(mstOwnerRepository, times(1)).save(any(MstOwnerManagement.class));
  }

  @Test
  public void 無効なステータスに更新する() {
    MstOwnerManagementForm ownerForm = new MstOwnerManagementForm();
    ownerForm.setId("1");// 例外発生
    ownerForm.setClient("株式会社エス・イー・プロジェクト");
    ownerForm.setClientId("1");
    ownerForm.setIndividual("1");
    ownerForm.setCorporation("株式会社エス・イー・プロジェクト");
    ownerForm.setCorporationKana("カブシキガイシャエス・イー・プロジェクト");
    ownerForm.setDepartment("開発部");
    ownerForm.setLName("test");
    ownerForm.setLNameKana("テスト");
    ownerForm.setFName("ownerForm");
    ownerForm.setFNameKana("オーナーフォーム");
    ownerForm.setPostCode("000-0000");
    ownerForm.setPrefectures("3");
    ownerForm.setAddress("aaaa");
    ownerForm.setBuildingName("テストビル");
    ownerForm.setPhone("090-0000-0000");
    ownerForm.setMobilePhone("080-0000-0000");
    ownerForm.setEmail("test@example.com");
    ownerForm.setStatus("てすと");// 例外発生

    MstCode mstCode = new MstCode();
    mstCode.setId(1);
    mstCode.setCodeKind(3);
    mstCode.setCodeBranchNum(1);
    mstCode.setCodeName("北海道");
    mstCode.setStatus(1);

    when(mstOwnerRepository.save(any(MstOwnerManagement.class))).thenReturn(new MstOwnerManagement());
    when(mstCodeRepository.findByCodeName("1")).thenReturn(Optional.of(mstCode));
    when(commonService.getLoginUserId()).thenReturn(1);

    MstOwnerManagement result = mstOwnerService.saveOwnerUpdate(ownerForm);
    assertNotNull(result);
    verify(mstOwnerRepository, times(1)).save(any(MstOwnerManagement.class));
  }

}
