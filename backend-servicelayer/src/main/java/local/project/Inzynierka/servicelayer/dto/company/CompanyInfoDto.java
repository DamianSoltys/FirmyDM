package local.project.Inzynierka.servicelayer.dto.company;

import com.fasterxml.jackson.annotation.JsonInclude;
import local.project.Inzynierka.servicelayer.dto.address.Address;
import local.project.Inzynierka.servicelayer.dto.social.SocialProfileConnectionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class CompanyInfoDto {

    private Long companyId;
    private String companyName;
    private String registererFullname;
    private String category;
    private String companyWebsiteUrl;
    private String description;
    private Address address;
    private String REGON;
    private String NIP;
    private List<Long> branchesIDs;
    private String getLogoURL;
    private String putLogoURL;
    private String logoKey;
    private Boolean hasLogoAdded;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private List<SocialProfileConnectionDto> socialProfileConnectionDtos;

}
