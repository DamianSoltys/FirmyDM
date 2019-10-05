package local.project.Inzynierka.servicelayer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommentGetDto {
    private String comment;
    private Long commentId;
    private Long userId;
    private Long branchId;
}
