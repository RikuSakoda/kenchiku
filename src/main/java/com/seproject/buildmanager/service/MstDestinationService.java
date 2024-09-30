package com.seproject.buildmanager.service;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.entity.MstDestination;
import com.seproject.buildmanager.form.MstDestinationForm;
import com.seproject.buildmanager.repository.MstDestinationRepository;

@Service
public class MstDestinationService {

    private static final Logger logger = LoggerFactory.getLogger(MstDestinationService.class);
  
    @Autowired
    private MstDestinationRepository mstDestinationRepository;
  
    @Autowired
    private CommonService commonService;
  
    /**
     * 出向先管理に勤怠一覧を表示するメソッド
     * 
     * @return 全てのMstDestinationエンティティのリスト
     */
    public List<MstDestination> findAll() {
        return mstDestinationRepository.findAll();
    }
  
    /**
     * 出向先管理の新規登録を行い、DBに保存します。
     * 
     * @param mstDestinationForm 吉野管理登録フォームオブジェクト
     * @return 登録結果
     */
    public MstDestination saveDestinationRegister(MstDestinationForm mstDestinationForm) {
        logger.info("--- MstDestinationService.saveDestinationRegister START ---");

        MstDestination tmp = toMstDestination(mstDestinationForm);
        tmp.setId(null); // IDは自動生成
        tmp.setCreatedAt(LocalDateTime.now());
        tmp.setUpdatedAt(null);
        

        MstDestination result;
        try {
            result = mstDestinationRepository.save(tmp);
        } catch (Exception e) {
            logger.error("Error saving MstDestination: " + tmp.toString(), e);
            throw new RuntimeException("出向先管理の登録に失敗しました。", e);
        }

        logger.info("--- MstDestinationService.saveDestinationRegister END ---");
        return result;
    }
  
    /**
     * IDで検索します。
     * 
     * @param id ID
     * @return 検索結果
     */
    public MstDestination getDestinationId(int id) {
        return mstDestinationRepository.findById(id).orElse(new MstDestination());
    }

    /**
     * 指定されたIDのデータをフォーム用に変換して返します。
     * 
     * @param id 出向先管理のID
     * @return フォーム用データ
     */
    public MstDestinationForm updateDestinationForm(int id) {
        MstDestination mstDestination = getDestinationId(id);
        MstDestinationForm mstDestinationForm = new MstDestinationForm();
        BeanUtils.copyProperties(mstDestination, mstDestinationForm);
        return mstDestinationForm;
    }

    /**
     * 出向先管理情報を更新します。
     * 
     * @param mstDestinationForm 更新するフォームデータ
     * @return 更新結果
     */
    public MstDestination updateDestination(MstDestinationForm mstDestinationForm) {
        logger.info("--- MstDestinationService.updateDestination START ---");

        // 既存のデータを取得
        MstDestination existingMstDestination = mstDestinationRepository.findById(mstDestinationForm.getId())
                .orElseThrow(() -> new RuntimeException("出向先情報が見つかりません。ID: " + mstDestinationForm.getId()));

        // 変更されるフィールドを更新
        BeanUtils.copyProperties(mstDestinationForm, existingMstDestination, "id", "createdAt");
        existingMstDestination.setUpdatedAt(LocalDateTime.now());
        existingMstDestination.setUpdatedMstUserId(this.commonService.getLoginUserId()); // 最終更新者IDを設定

        MstDestination result;
        try {
            result = mstDestinationRepository.save(existingMstDestination);
        } catch (Exception e) {
            logger.error("Error updating MstDestination: " + existingMstDestination.toString(), e);
            throw new RuntimeException("出向先の更新に失敗しました。", e);
        }

        logger.info("--- MstDestinationService.updateDestination END ---");
        return result;
    }

    /**
     * フォームデータをMstDestinationエンティティに変換します。
     * 
     * @param mstDestination フォームデータ
     * @return MstDestinationエンティティ
     */
    private MstDestination toMstDestination(MstDestinationForm mstDestinationForm) {
        MstDestination mstDestination = new MstDestination();
        BeanUtils.copyProperties(mstDestinationForm, mstDestination);
        logger.debug("Mapped MstDestination: {}", mstDestination);
        return mstDestination;
    }
}
