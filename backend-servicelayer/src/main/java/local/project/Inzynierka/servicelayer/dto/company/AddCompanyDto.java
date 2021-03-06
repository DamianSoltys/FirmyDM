package local.project.Inzynierka.servicelayer.dto.company;

import local.project.Inzynierka.servicelayer.dto.address.Address;
import local.project.Inzynierka.servicelayer.dto.branch.AddBranchDto;
import local.project.Inzynierka.servicelayer.validation.ValidWebProtocolUrl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddCompanyDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String NIP;

    @NotEmpty
    private String REGON;

    @NotNull
    @Valid
    private Address address;

    @NotEmpty
    private String description;

    @NotEmpty
    private String category;

    List<AddBranchDto> branches;

    @ValidWebProtocolUrl
    private String companyWebsiteUrl;
}
