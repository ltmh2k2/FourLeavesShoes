package com.data.filtro.service;

import com.data.filtro.model.Account;
import com.data.filtro.model.Material;
import com.data.filtro.repository.MaterialRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    @Autowired
    MaterialRepository materialRepository;

    public Material getMaterialById(int id) {
        return materialRepository.findById(id);
    }

    public Page<Material> getAllPaging(Pageable pageable) {
        return materialRepository.findAll(pageable);
    }

    public void create(Material flavor) {
        materialRepository.save(flavor);
    }


    public void update(Material flavor) {
        Material newFlavor = getMaterialById(flavor.getId());
        newFlavor.setMaterialName(flavor.getMaterialName());
        newFlavor.setDescription(flavor.getDescription());
        newFlavor.setStatus(flavor.getStatus());
        materialRepository.save(newFlavor);
    }

    @Transactional
    public void delete(int id) {
        materialRepository.deleteById(id);
    }

    public List<Material> getAll() {
        return materialRepository.findAll();
    }
    public List<Material> getActiveMaterial(int status){
        return materialRepository.activeMaterials(status);
    }
}
