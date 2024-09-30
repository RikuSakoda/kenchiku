package com.seproject.buildmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.entity.MstCustomer;
import com.seproject.buildmanager.entity.MstOwnerManagement;
import com.seproject.buildmanager.entity.MstYoshino;
import com.seproject.buildmanager.form.MstYoshinoForm;
import com.seproject.buildmanager.repository.MstYoshinoRepository;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

@Service
public class MstYoshinoService {

    private static final Logger logger = LoggerFactory.getLogger(MstYoshinoService.class);

    @Autowired
    private MstYoshinoRepository mstYoshinoRepository;
    
    @Autowired
    private MstCustomerService mstCustomerService;

    /**
     * 吉野管理にユーザー一覧を表示するメソッド
     * 
     * @return 全てのMstYoshinoエンティティのリスト
     */
    public List<MstYoshino> findAll() {
        return mstYoshinoRepository.findAll();
    }

    /**
     * MstYoshinoFormの顧客名を設定するメソッド
     * 
     * @param mstYoshinoForm フォームデータ
     * @return 顧客名を設定したフォームデータ
     */
    public MstYoshinoForm YoshinoForm(MstYoshinoForm mstYoshinoForm) {
        MstCustomer m = mstCustomerService.getCustomerById(mstYoshinoForm.getCustkind());
        mstYoshinoForm.setCustcorpname(m.getCorpName());
        return mstYoshinoForm;
    }

    /**
     * 吉野の新規登録を行い、DBに保存します。
     * 
     * @param mstYoshinoForm 吉野管理登録フォームオブジェクト
     * @return 登録結果
     */
    public MstYoshino saveYoshinoRegister(MstYoshinoForm mstYoshinoForm) {
        logger.info("--- MstYoshinoService.saveYoshinoRegister START ---");

        MstYoshino tmp = toMstYoshino(mstYoshinoForm);
        tmp.setId(null); // IDは自動生成
        tmp.setStatus(Constants.STATUS_TRUE);
        tmp.setCreatedat(LocalDateTime.now());
        tmp.setUpdatedat(null);

        MstYoshino result;
        try {
            result = mstYoshinoRepository.save(tmp);
        } catch (Exception e) {
            logger.error("Error saving MstYoshino: " + tmp.toString(), e);
            throw new RuntimeException("吉野の登録に失敗しました。", e);
        }

        logger.info("--- MstYoshinoService.saveYoshinoRegister END ---");
        return result;
    }

    /**
     * IDで検索します。
     * 
     * @param id ID
     * @return 検索結果
     */
    public MstYoshino getYoshinoId(int id) {
        return mstYoshinoRepository.findById(id).orElse(new MstYoshino());
    }

    /**
     * 指定されたIDの吉野データをフォーム用に変換して返します。
     * 
     * @param id 吉野のID
     * @return フォーム用データ
     */
    public MstYoshinoForm updateYoshinoForm(int id) {
        MstYoshino mstYoshino = getYoshinoId(id);
        MstYoshinoForm mstYoshinoForm = new MstYoshinoForm();
        BeanUtils.copyProperties(mstYoshino, mstYoshinoForm);
        return mstYoshinoForm;
    }

    /**
     * 吉野管理情報を更新します。
     * 
     * @param mstYoshinoForm 更新するフォームデータ
     * @return 更新結果
     */
    public MstYoshino updateYoshino(MstYoshinoForm mstYoshinoForm) {
        logger.info("--- MstYoshinoService.updateYoshino START ---");

        // 既存のデータを取得
        MstYoshino existingMstYoshino = mstYoshinoRepository.findById(mstYoshinoForm.getId())
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません。ID: " + mstYoshinoForm.getId()));

        // 変更されるフィールドを更新
        BeanUtils.copyProperties(mstYoshinoForm, existingMstYoshino, "id", "createdat");
        existingMstYoshino.setUpdatedat(LocalDateTime.now());

        MstYoshino result;
        try {
            result = mstYoshinoRepository.save(existingMstYoshino);
        } catch (Exception e) {
            logger.error("Error updating MstYoshino: " + existingMstYoshino.toString(), e);
            throw new RuntimeException("ユーザーの更新に失敗しました。", e);
        }

        logger.info("--- MstYoshinoService.updateYoshino END ---");
        return result;
    }
    
     /**
     * フォームデータをMstYoshinoエンティティに変換します。
     * 
     * @param mstYoshinoForm フォームデータ
     * @return MstYoshinoエンティティ
     */
    private MstYoshino toMstYoshino(MstYoshinoForm mstYoshinoForm) {
        MstYoshino mstYoshino = new MstYoshino();
        BeanUtils.copyProperties(mstYoshinoForm, mstYoshino);
        return mstYoshino;
    }
    
    
    public MstYoshino saveStatus(Integer id) {
      MstYoshino user_yoshino = getYoshinoId(id);
//      user_yoshino.setUpdatedAt(LocalDateTime.now());

      MstYoshino result = mstYoshinoRepository.save(user_yoshino);
      mstYoshinoRepository.toggleStatus(id);
      return result;
    }
    
}
