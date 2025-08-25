package com.dat.plantbackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Setter
@Getter
@Builder
public class CreateHistoryDTO {

    Integer treatmentId;
    MultipartFile predictedImage;

}
