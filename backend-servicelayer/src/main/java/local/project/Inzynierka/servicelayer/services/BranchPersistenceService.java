package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Address;
import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.Voivoideship;
import local.project.Inzynierka.persistence.repository.AddressRepository;
import local.project.Inzynierka.persistence.repository.BranchRepository;
import local.project.Inzynierka.persistence.repository.VoivodeshipRepository;
import local.project.Inzynierka.servicelayer.dto.AddBranchDto;
import local.project.Inzynierka.servicelayer.dto.PersistedBranchDto;
import local.project.Inzynierka.servicelayer.dto.mapper.AddressMapper;
import local.project.Inzynierka.servicelayer.dto.mapper.BranchMapper;
import local.project.Inzynierka.servicelayer.errors.InvalidVoivodeshipException;
import local.project.Inzynierka.shared.utils.EntityName;
import local.project.Inzynierka.shared.utils.LogoFilePathCreator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class BranchPersistenceService {

    private final VoivodeshipRepository voivodeshipRepository;

    private final AddressRepository addressRepository;

    private final BranchRepository branchRepository;

    private final BranchMapper branchMapper;

    private final AddressService addressService;

    BranchPersistenceService(VoivodeshipRepository voivodeshipRepository, AddressRepository addressRepository, BranchRepository branchRepository, BranchMapper branchMapper, AddressService addressService) {
        this.voivodeshipRepository = voivodeshipRepository;
        this.addressRepository = addressRepository;
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
        this.addressService = addressService;
    }

    void buildAllCompanyBranches(List<Branch> branches, Company createdCompany) {
        branches.forEach(branch -> {
            branch.setCompany(createdCompany);
            branch.setRegisterer(createdCompany.getRegisterer());
            branch.setPhotoPath(LogoFilePathCreator.buildEntityLogoURL(EntityName.BRANCH));
            Voivoideship branchVoivodeship = this.voivodeshipRepository.findByName(branch.getAddress().getVoivodeship_id().getName()).orElseThrow(InvalidVoivodeshipException::new);
            branch.getAddress().setVoivodeship_id(branchVoivodeship);
            Address branchAddress = this.addressRepository.save(branch.getAddress());
            branch.setAddress(branchAddress);
        });
    }

    Iterable<Branch> saveAll(List<Branch> branches) {
        return this.branchRepository.saveAll(branches);
    }

    List<Long> getCompanyBranchesIds(Long id) {
        return this.branchRepository.getAllByCompanyId(id);
    }

    Optional<Branch> getPersistedBranch(Long branchId) {
        return this.branchRepository.findById(branchId);
    }

    Optional<PersistedBranchDto> saveBranch(AddBranchDto addBranchDto, Long companyId, Long personId) {

        Voivoideship voivoideship = addressService.getVoivodeshipByName(
                addBranchDto.getAddress().getVoivodeship().toString())
                .orElseThrow(InvalidVoivodeshipException::new);
        Address address = addressRepository.save(new AddressMapper().map(addBranchDto.getAddress(), voivoideship));
        Branch branch = branchMapper.mapAddBranchDto(addBranchDto, companyId, personId, address);
        return Optional.of(this.branchRepository.save(branch)).map(branchMapper::mapPersistedBranch);
    }
}
