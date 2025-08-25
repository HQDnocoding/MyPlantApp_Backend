package com.dat.plantbackend.services;

import ai.onnxruntime.*;
import com.dat.plantbackend.dto.PredictionResult;
import com.dat.plantbackend.dto.TreatmentDTO;
import com.dat.plantbackend.repositories.TreatmentRepository;
import com.dat.plantbackend.utils.CommonUtils;
import com.dat.plantbackend.utils.DiseaseMap;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ONNXService {

    private OrtEnvironment environment;
    private OrtSession session;

    @PostConstruct
    public void init() throws OrtException {
        environment = OrtEnvironment.getEnvironment();
        session = environment.createSession("model/model.onnx");
    }

    @Autowired
    private TreatmentService treatmentService;

    public PredictionResult predict(float[][][][] input) throws OrtException {
        long start = System.currentTimeMillis();

        try (OnnxTensor tensor = OnnxTensor.createTensor(environment, input)) {
            Map<String, OnnxTensor> inputs = Map.of("input", tensor);

            try (OrtSession.Result result = session.run(inputs)) {
                OnnxValue outputValue = result.get(0);
                float[][] output = (float[][]) outputValue.getValue();

                // Tìm class có confidence cao nhất
                int maxIndex = 0;
                float probabilities[] = CommonUtils.applySoftmax(output[0]);
                float maxValue = probabilities[0];



                for (int i = 1; i < probabilities.length; i++) {
                    System.out.printf("%s: %s\n",  probabilities[i] ,i);
                    if (probabilities[i] > maxValue) {
                        maxValue = probabilities[i];
                        maxIndex = i;
                    }
                }

                PredictionResult presult = new PredictionResult();

                List<TreatmentDTO> treatment = treatmentService.getTreatmentByDiseaseNameAndPlant(DiseaseMap.map.get(maxIndex+1),"Durian");

                presult.setPredictedClass(DiseaseMap.map.get(maxIndex+1));
                presult.setConfidence(maxValue);
                presult.setTreatment(treatment);
                presult.setProcessingTimeMs(System.currentTimeMillis() - start);

                return presult;
            }
        }
    }



    @PreDestroy
    public void cleanup() throws OrtException {
        if (session != null) session.close();
        if (environment != null) environment.close();
    }
}