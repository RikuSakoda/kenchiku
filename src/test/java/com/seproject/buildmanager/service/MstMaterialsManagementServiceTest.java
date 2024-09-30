package com.seproject.buildmanager.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.seproject.buildmanager.entity.MstMaterialsManagement;
import com.seproject.buildmanager.form.MstMaterialsManagementForm;
import com.seproject.buildmanager.repository.MstMaterialsManagementRepository;

class MstMaterialsManagementServiceTest {

  @Mock
  private MstMaterialsManagementRepository mstMaterialsRepository;

  @InjectMocks
  private MstMaterialsManagementService mstMaterialsService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void 新規登録用のフォームを作成する() {

    MstMaterialsManagementForm result = new MstMaterialsManagementForm();
    assertNotNull(result);
  }

  @Test
  public void testSaveMaterialsRegister() {


    when(mstMaterialsRepository.save(any(MstMaterialsManagement.class))).thenReturn(new MstMaterialsManagement());
    MstMaterialsManagement result = mstMaterialsService.saveMaterialsRegister("資材メーカー名");
    assertNotNull(result);
    verify(mstMaterialsRepository, times(1)).save(any(MstMaterialsManagement.class));
  }

  @Test
  public void 資材メーカの情報を取得() {
    List<MstMaterialsManagement> result = mstMaterialsRepository.findAll();
    assertNotNull(result);
  }

  @Test
  public void testSaveMaterialsUpdate() {
    MstMaterialsManagement a = mstMaterialsRepository.findById(1).orElse(new MstMaterialsManagement());
    MstMaterialsManagement tmp = new MstMaterialsManagement();
    tmp.setId(1);
    tmp.setMaterialsName("資材メーカー");
    tmp.setCreatedAt(a.getCreatedAt());
    tmp.setUpdatedAt(LocalDateTime.now());
    tmp.setUpdatedMstUserId(1);
    // tmp.setUpdate_user_id(this.commonService.getLoginUserId());// userのidがわからないため保留
    when(mstMaterialsRepository.save(any(MstMaterialsManagement.class))).thenReturn(new MstMaterialsManagement());
    MstMaterialsManagement result = mstMaterialsService.saveMaterialsRegister("資材メーカー名");
    assertNotNull(result);
    verify(mstMaterialsRepository, times(1)).save(any(MstMaterialsManagement.class));
  }

}
