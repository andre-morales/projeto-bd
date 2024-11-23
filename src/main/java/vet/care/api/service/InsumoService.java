package vet.care.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vet.care.api.model.entities.Insumo;
import vet.care.api.repository.entities.InsumoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsumoService {

    private final InsumoRepository insumoRepository;

    public Insumo getInsumoByCd(String ean) {
        return insumoRepository.findByCd(ean);
    }

    public List<Insumo> getAllInsumos() {
        return insumoRepository.findAll();
    }

    public int addInsumo(Insumo insumo) {
        return insumoRepository.save(insumo);
    }

    public int updateInsumo(Insumo insumo) {
        return insumoRepository.update(insumo);
    }

    public int deleteInsumo(String ean) {
        return insumoRepository.deleteByCd(ean);
    }

    public List<Insumo> getInsumosProximosDeVencer() {
        List<Insumo> insumos = insumoRepository.findInsumosProximosDeVencer();
        if (insumos.isEmpty()) {
            return null;
        }
        return insumos;
    }
}