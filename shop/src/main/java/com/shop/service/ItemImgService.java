package com.shop.service;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")
    // @Value : application.properties 파일에 등록한 itemImgLocation 값을 불러와서 itemImgLocation 변수에 넣음
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // 파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            // 사용자가 상품의 이미지를 등록했다면 저장할 경로와 파일의 이름, 파일을 파일의 바이트 배열을 파일 업로드 파라미터로 uploadFile 메소드를 호출
            // 호출 결과 토컬에 저장된 파일의 이름을 imgName 변수에 저장
            imgUrl = "/images/item/" + imgName;
            // 저장한 상품 이미지를 불러올 경로를 설정
        }

        // 파일 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }
    // imgName : 실제 로컬에 저장된 상품 이미지 파일의 이름
    // oriImgName : 업로드했던 상품 이미지 파일의 원래 이름
    // imgUrl : 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로

    // 상품 이미지 수정
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if (!itemImgFile.isEmpty()) { // 상품 이미지를 수정한 경우 상품 이미지를 업데이트 함
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);
            // 상품 이미지 아이디를 이용하여 기존에 저장했던 상품 이미지 엔티티를 조회

            // 기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());  // 기존에 등록된 상품 이미지 파일이 있을 경우 해당 파일을 삭제
            }
            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            // 업데이트한 상품 이미지 파일을 업로드
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);

        }
    }
}
