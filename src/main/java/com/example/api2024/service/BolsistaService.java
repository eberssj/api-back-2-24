package com.example.api2024.service;

import com.example.api2024.repository.BolsistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BolsistaService {

    @Autowired
    private BolsistaRepository bolsistaRepository;

    // Valor total pago em bolsas
    public BigDecimal getValorTotalBolsa() {
        return bolsistaRepository.totalValorBolsa();
    }

    // Quantidade de bolsistas por área de atuação
    public Map<String, Long> getBolsistasPorArea() {
        List<Object[]> results = bolsistaRepository.countBolsistasPorArea();
        Map<String, Long> bolsistasPorArea = new HashMap<>();

        for (Object[] result : results) {
            String areaAtuacao = (String) result[0];
            Long count = (Long) result[1];
            bolsistasPorArea.put(areaAtuacao, count);
        }

        return bolsistasPorArea;
    }
}
