package com.seproject.buildmanager.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import com.seproject.buildmanager.entity.MstUser;
import com.seproject.buildmanager.form.MstUserForm;
import com.seproject.buildmanager.form.ResetPasswordForm;
import com.seproject.buildmanager.repository.MstUserRepository;

public class MstUserServiceTest {

  @Mock
  private MstUserRepository mstUserRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private CommonService commonService;

  @InjectMocks
  private MstUserService mstUserService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void 全てのユーザを取得する() {
    List<MstUser> userList = new ArrayList<>();
    userList.add(new MstUser());
    when(mstUserRepository.findAll()).thenReturn(userList);

    List<MstUser> result = mstUserService.getAllUsers();
    assertEquals(1, result.size());
  }

  @Test
  void 権限情報を含む全てのユーザを取得する() {
    List<MstUser> userList = new ArrayList<>();
    userList.add(new MstUser());
    when(mstUserRepository.findAllUsersWithAuthName()).thenReturn(userList);

    List<MstUser> result = mstUserService.findAllUsersWithAuthName();
    assertEquals(1, result.size());
  }

  @Test
  void 新規登録用のフォームを作成する() {
    MstUserForm form = mstUserService.showUserForm();
    assertNotNull(form);
  }

  @Test
  void 新規ユーザを保存する() {
    MstUserForm form = new MstUserForm();
    form.setLoginCd("loginCd");
    form.setPassword("password");
    form.setEmail("email@example.com");
    form.setMstAuthId(1);
    form.setLName("LName");
    form.setFName("FName");
    form.setLNameKana("LNameKana");
    form.setFNameKana("FNameKana");
    form.setTel("1234567890");

    when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
    when(commonService.getLoginUserId()).thenReturn(1);
    when(mstUserRepository.save(any(MstUser.class))).thenReturn(new MstUser());

    MstUser result = mstUserService.saveUser(form);
    assertNotNull(result);
    verify(mstUserRepository, times(1)).save(any(MstUser.class));
  }

  @Test
  void 既存のユーザを更新する() {
    MstUser existingUser = new MstUser();
    existingUser.setId(1);
    when(mstUserRepository.findById(1)).thenReturn(Optional.of(existingUser));
    when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
    when(commonService.getLoginUserId()).thenReturn(1);
    when(mstUserRepository.save(any(MstUser.class))).thenReturn(existingUser);

    MstUserForm form = new MstUserForm();
    form.setId(1);
    form.setLoginCd("loginCd");
    form.setPassword("password");
    form.setEmail("email@example.com");
    form.setMstAuthId(1);
    form.setLName("LName");
    form.setFName("FName");
    form.setLNameKana("LNameKana");
    form.setFNameKana("FNameKana");
    form.setTel("1234567890");

    MstUser result = mstUserService.saveUser(form);
    assertNotNull(result);
    verify(mstUserRepository, times(1)).save(any(MstUser.class));
  }

  @Test
  void 特定のユーザIDのユーザを取得する() {
    MstUser user = new MstUser();
    user.setId(1);
    when(mstUserRepository.findById(1)).thenReturn(Optional.of(user));

    MstUser result = mstUserService.getUserById(1);
    assertEquals(1, result.getId());
  }

  @Test
  void パスワードリセット用のフォームを作成する() {
    ResetPasswordForm form = mstUserService.resetPasswordForm();
    assertNotNull(form);
  }

  @Test
  void パスワードリセットとユーザ情報を更新する() {
    MstUser existingUser = new MstUser();
    existingUser.setId(1);
    when(mstUserRepository.findById(1)).thenReturn(Optional.of(existingUser));
    when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
    when(commonService.getLoginUserId()).thenReturn(1);
    when(mstUserRepository.save(any(MstUser.class))).thenReturn(existingUser);

    ResetPasswordForm resetForm = new ResetPasswordForm();
    resetForm.setPassword("newPassword");

    MstUser mstUser = new MstUser();
    mstUser.setId(1);
    mstUser.setLoginCd("loginCd");
    mstUser.setMstAuthId(1);
    mstUser.setLName("LName");
    mstUser.setFName("FName");
    mstUser.setLNameKana("LNameKana");
    mstUser.setFNameKana("FNameKana");
    mstUser.setTel("1234567890");
    mstUser.setEmail("email@example.com");
    mstUser.setStatus(1);

    MstUser result = mstUserService.update(resetForm);
    assertNotNull(result);
    verify(mstUserRepository, times(1)).save(any(MstUser.class));
  }

  @Test
  void 無効なステータスでユーザを保存する() {
    MstUserForm form = new MstUserForm();
    form.setLoginCd("loginCd");
    form.setPassword("password");
    form.setEmail("email@example.com");
    form.setMstAuthId(1);
    form.setLName("LName");
    form.setFName("FName");
    form.setLNameKana("LNameKana");
    form.setFNameKana("FNameKana");
    form.setTel("1234567890");
    form.setStatus("invalidNumber"); // ここで例外が発生する

    when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
    when(commonService.getLoginUserId()).thenReturn(1);
    when(mstUserRepository.save(any(MstUser.class))).thenReturn(new MstUser());

    MstUser result = mstUserService.saveUser(form);
    assertNotNull(result);
    verify(mstUserRepository, times(1)).save(any(MstUser.class));
  }

  @Test
  void ユーザ保存時に例外が発生する() {
    MstUserForm form = new MstUserForm();
    form.setLoginCd("loginCd");
    form.setPassword("password");
    form.setEmail("email@example.com");
    form.setMstAuthId(1);
    form.setLName("LName");
    form.setFName("FName");
    form.setLNameKana("LNameKana");
    form.setFNameKana("FNameKana");
    form.setTel("1234567890");

    when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
    when(commonService.getLoginUserId()).thenReturn(1);
    when(mstUserRepository.save(any(MstUser.class)))
        .thenThrow(new RuntimeException("Database error"));

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      mstUserService.saveUser(form);
    });

    assertEquals("ユーザの登録・更新に失敗しました。", thrown.getMessage());
    verify(mstUserRepository, times(1)).save(any(MstUser.class));
  }
}
