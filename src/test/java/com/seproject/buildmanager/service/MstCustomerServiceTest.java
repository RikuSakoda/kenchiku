package com.seproject.buildmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.seproject.buildmanager.entity.MstCode;
import com.seproject.buildmanager.entity.MstCustomer;
import com.seproject.buildmanager.form.MstCustomerForm;
import com.seproject.buildmanager.repository.MstCodeRepository;
import com.seproject.buildmanager.repository.MstCustomerRepository;


@ExtendWith(MockitoExtension.class)
class MstCustomerServiceTest {

  @Mock
  private MstCustomerRepository mstCustomerRepository;

  @Mock
  private MstCodeRepository mstCodeRepository;

  @InjectMocks
  private MstCustomerService mstCustomerService;

  private MstCustomer customer;
  private MstCustomerForm customerForm;
  private MstCode mstCode;

  @BeforeEach
  public void setUp() {
    customer = new MstCustomer();
    customer.setCustKind(1);
    customer.setCorpName("株式会社エス・イー・プロジェクト");
    customer.setCorpKana("カブシキガイシャエス・イー・プロジェクト");
    customer.setDepartment("開発部");
    customer.setLName("test");
    customer.setLNameKana("テスト");
    customer.setFName("customer");
    customer.setFNameKana("顧客");
    customer.setZip("0000000");
    customer.setPrefecture(1);
    customer.setAddress1("aaa");
    customer.setAddress2("bbb");
    customer.setTel("00-000-0000");
    customer.setMobile("000-0000-0000");
    customer.setMail("mail@mail");
    customer.setMailFlg(1);
    customer.setStatus(1);

    customerForm = new MstCustomerForm();
    customerForm.setId(1);
    customerForm.setCust_kind("1");
    customerForm.setCorpName("test");
    customerForm.setCorpKana("テスト");
    customerForm.setDepartment("開発部");
    customerForm.setLName("test");
    customerForm.setLNameKana("テスト");
    customerForm.setFName("customer");
    customerForm.setFNameKana("顧客");
    customerForm.setZip("0000000");
    customerForm.setPrefectures("1");
    customerForm.setAddress1("aaa");
    customerForm.setAddress2("bbb");
    customerForm.setTel("00-000-0000");
    customerForm.setMobile("000-0000-0000");
    customerForm.setMail("mail@mail");
    customerForm.setMailFlg(true);
    customerForm.setStatus("1");

    mstCode = new MstCode();
    mstCode.setId(1);
    mstCode.setCodeKind(3);
    mstCode.setCodeBranchNum(1);
    mstCode.setCodeName("北海道");
    mstCode.setStatus(1);

  }

  @Test
  void testGetAllCustomers() {
    List<MstCustomer> customerList = new ArrayList<>();
    customerList.add(customer);
    when(mstCustomerRepository.findAll()).thenReturn(customerList);

    List<MstCustomer> result = mstCustomerService.getAllCustomers();
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(mstCustomerRepository, times(1)).findAll();
  }

  @Test
  void testViewCustomerForm() {
    List<MstCustomerForm> result = mstCustomerService.viewCustomerForm();
    assertNotNull(result);
  }

  @Test
  void testShowCustomerForm() {
    MstCustomerForm result = mstCustomerService.showCustomerForm();
    assertNotNull(result);
  }

  @Test
  void testSaveCustomer() {
    when(mstCustomerRepository.save(any(MstCustomer.class))).thenReturn(customer);

    MstCustomer result = mstCustomerService.saveCustomer(customerForm);
    assertNotNull(result);
    verify(mstCustomerRepository, times(1)).save(any(MstCustomer.class));
  }

  @Test
  void testGetCustomerById() {
    when(mstCustomerRepository.findById(customer.getId())).thenReturn(java.util.Optional.empty());

    MstCustomer result = mstCustomerService.getCustomerById(customer.getId());
    assertNotNull(result);
    verify(mstCustomerRepository, times(1)).findById(customer.getId());
  }

  @Test
  void testSaveCustomerUpdate() {
    when(mstCustomerRepository.save(any(MstCustomer.class))).thenReturn(customer);

    MstCustomer result = mstCustomerService.saveCustomerUpdate(customerForm);
    assertNotNull(result);
    verify(mstCustomerRepository, times(1)).save(any(MstCustomer.class));
  }

  @Test
  void testUpdateCustomerForm() {


    MstCustomerForm result = mstCustomerService.updateCustomerForm(customer);
    assertNotNull(result);

  }

}
