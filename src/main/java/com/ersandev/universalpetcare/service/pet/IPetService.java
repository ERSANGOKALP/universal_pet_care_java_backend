package com.ersandev.universalpetcare.service.pet;

import com.ersandev.universalpetcare.model.Pet;

import java.util.List;

public interface IPetService {

    List<Pet> savePetForAppointment(List<Pet> pets);
    Pet updatePet(Pet pet,Long petId);
    void deletePet(Long petId);
    Pet getPetById(Long petId);
}
