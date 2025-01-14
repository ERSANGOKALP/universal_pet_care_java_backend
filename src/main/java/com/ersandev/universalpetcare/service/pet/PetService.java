package com.ersandev.universalpetcare.service.pet;

import com.ersandev.universalpetcare.exception.ResourceNotFoundException;
import com.ersandev.universalpetcare.model.Pet;
import com.ersandev.universalpetcare.repository.PetRepository;
import com.ersandev.universalpetcare.utils.FeedBackMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService implements IPetService {

    private final PetRepository petRepository;

    @Override
    public List<Pet> savePetForAppointment(List<Pet> pets) {
        return petRepository.saveAll(pets);
    }

    @Override
    public Pet updatePet(Pet pet, Long petId) {
        Pet existingPet = getPetById(petId);
        existingPet.setName(pet.getName());
        existingPet.setAge(pet.getAge());
        existingPet.setType(pet.getType());
        existingPet.setColor(pet.getColor());
        existingPet.setBreed(pet.getBreed());

        return petRepository.save(existingPet);
    }

    @Override
    public void deletePet(Long petId) {
        petRepository.findById(petId)
                .ifPresentOrElse(petRepository::delete,
                        ()-> {
                    throw new ResourceNotFoundException(FeedBackMessage.NOT_FOUND);
                        });
    }

    @Override
    public Pet getPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(()-> new ResourceNotFoundException(FeedBackMessage.NOT_FOUND));
    }
}
