package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.servicelayer.dto.rating.CommentGetDto;
import local.project.Inzynierka.servicelayer.dto.rating.CreateCommentDto;
import local.project.Inzynierka.servicelayer.dto.rating.EditCommentDto;
import local.project.Inzynierka.servicelayer.rating.CommentService;
import local.project.Inzynierka.servicelayer.rating.event.CommentCreatedEvent;
import local.project.Inzynierka.servicelayer.rating.event.CommentDeletedEvent;
import local.project.Inzynierka.servicelayer.rating.event.CommentEditedEvent;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api")
public class CommentResource {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuthFacade authFacade;
    private final CommentService commentService;

    public CommentResource(ApplicationEventPublisher applicationEventPublisher, AuthFacade authFacade, CommentService commentService) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.authFacade = authFacade;
        this.commentService = commentService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/comment")
    public ResponseEntity<?> createComment(@RequestBody final CreateCommentDto createCommentDto) {

        UserAccount userAccount = this.authFacade.getAuthenticatedUser();

        CommentCreatedEvent commentCreatedEvent = CommentCreatedEvent.builder()
                .branchId(createCommentDto.getBranchId())
                .userName(userAccount.getLoginName())
                .commentContent(createCommentDto.getComment())
                .build();

        Long commentId = this.commentService.createComment(commentCreatedEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/comment/{id}")
    public ResponseEntity<?> editComment(@RequestBody final EditCommentDto editCommentDto,
                                         @PathVariable(value = "id") Long commentId) {

        if (this.authFacade.hasPrincipalHavePermissionToCommentResource(commentId)) {
            CommentEditedEvent commentEditedEvent = CommentEditedEvent.builder()
                    .commentId(commentId)
                    .newCommentContent(editCommentDto.getComment())
                    .build();
            applicationEventPublisher.publishEvent(commentEditedEvent);

            return ResponseEntity.ok("");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable(value = "id") Long commentId) {

        if (this.authFacade.hasPrincipalHavePermissionToCommentResource(commentId)) {
            applicationEventPublisher.publishEvent(new CommentDeletedEvent(commentId));

            return ResponseEntity.ok(null);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/comment/{id}")
    public ResponseEntity<?> getComment(@PathVariable(value = "id") Long commentId) {

        Optional<CommentGetDto> commentGetDto = this.commentService.getComment( commentId);

        if (commentGetDto.isPresent()) {
            return ResponseEntity.ok(commentGetDto.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/comment")
    public Page<CommentGetDto> getComments(@RequestParam(value = "userId", required = false) Long userId,
                                           @RequestParam(value = "branchId", required = false) Long branchId,
                                           Pageable pageable) {
        Page<CommentGetDto> result;

        if (branchId != null && userId != null) {
            result = this.commentService.getCommentsByBranchAndUser( branchId, userId, pageable);
        } else if (branchId == null && userId != null) {
            result = this.commentService.getCommentsByUser(userId, pageable);
        } else if (branchId != null && userId == null) {
            result = this.commentService.getCommentsByBranch(branchId, pageable);
        } else {
            result = this.commentService.getComments(pageable);
        }
        return result;
    }

}
